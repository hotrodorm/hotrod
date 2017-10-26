package org.hotrod.metadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.generator.mybatis.DataSetLayout;
import org.hotrod.metadata.VOMetadata.VOMember;
import org.hotrod.metadata.VORegistry.StructuredVOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOClass;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ColumnsMetadataRetriever.InvalidSQLException;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.hotrod.utils.identifiers.DataSetIdentifier;
import org.hotrod.utils.identifiers.Identifier;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.utils.JdbcUtil;

public class SelectMethodMetadata implements DataSetMetadata {

  // Constants

  private static final Logger log = Logger.getLogger(SelectMethodMetadata.class);

  // Properties

  private boolean structured;

  private HotRodGenerator generator;
  private JdbcDatabase db;
  private HotRodConfigTag config;
  private DatabaseAdapter adapter;
  private DatabaseLocation loc;
  private SelectMethodTag tag;
  private SelectGenerationTag selectGenerationTag;
  private ColumnsPrefixGenerator columnsPrefixGenerator;

  private HotRodFragmentConfigTag fragmentConfig;
  private List<ColumnMetadata> nonStructuredColumns;
  private StructuredColumnsMetadata structuredColumns;

  private String cleanedUpFoundation;
  private String tempViewName;
  private String createView;

  private ClassPackage classPackage;

  // Constructor

  public SelectMethodMetadata(final HotRodGenerator generator, final SelectMethodTag tag, final HotRodConfigTag config,
      final SelectGenerationTag selectGenerationTag, final ColumnsPrefixGenerator columnsPrefixGenerator,
      final DataSetLayout layout) {
    this.generator = generator;
    this.db = generator.getJdbcDatabase();
    this.config = config;
    this.adapter = generator.getAdapter();
    this.loc = generator.getLoc();
    this.tag = tag;
    this.selectGenerationTag = selectGenerationTag;
    this.columnsPrefixGenerator = columnsPrefixGenerator;

    this.fragmentConfig = tag.getFragmentConfig();
    this.structured = this.tag.getStructuredColumns() != null;
    this.nonStructuredColumns = null;
    this.structuredColumns = null;

    ClassPackage fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage() : null;
    this.classPackage = layout.getDAOPrimitivePackage(fragmentPackage);

  }

  // Behavior

  public void gatherMetadataPhase1(final Connection conn1) throws UncontrolledException, ControlledException {

    if (!this.structured) {

      // Unstructured columns

      try {
        prepareUnstructuredColumnsRetrieval(conn1);
      } catch (SQLException e) {
        throw new UncontrolledException("Failed to retrieve metadata for <" + new SelectMethodTag().getTagName()
            + "> with method '" + this.tag.getMethod() + "'.", e);
      }

    } else {

      // Structured columns

      try {
        log.debug("Phase 1");
        this.tag.getStructuredColumns().gatherMetadataPhase1(this.tag, this.selectGenerationTag,
            this.columnsPrefixGenerator, conn1);
      } catch (InvalidSQLException e) {
        throw new ControlledException("Could not retrieve metadata for <" + new SelectMethodTag().getTagName() + "> on "
            + this.tag.getSourceLocation().render() + "; could not create temporary SQL view for it.\n" + "[ "
            + e.getMessage() + " ]\n" + "* Do all resulting columns have different and valid names?\n"
            + "* Is the create view SQL code below valid?\n" + "--- begin SQL ---\n" + e.getInvalidSQL()
            + "\n--- end SQL ---");
      }

    }

  }

  public void gatherMetadataPhase2(final Connection conn2, final VORegistry voRegistry)
      throws UncontrolledException, ControlledException, InvalidConfigurationFileException {

    if (!this.structured) {

      // Non-structured columns

      try {
        retrieveFlatColumnsMetadata(conn2);

      } catch (SQLException e) {
        throw new UncontrolledException("Could not retrieve metadata for <" + new SelectMethodTag().getTagName()
            + "> at " + this.tag.getSourceLocation().render(), e);
      } catch (UnresolvableDataTypeException e) {
        throw new ControlledException("Could not retrieve metadata for <" + new SelectMethodTag().getTagName() + "> at "
            + this.tag.getSourceLocation().render() + ": could not find suitable Java type for column '"
            + e.getColumnName() + "' ");
      }

      List<VOMember> associations = new ArrayList<VOMember>();
      VOClass vo = new VOClass(null, this.classPackage, this.tag.getVO(), this.nonStructuredColumns, associations);
      try {
        voRegistry.addVO(vo);
      } catch (VOAlreadyExistsException e) {
        throw new InvalidConfigurationFileException(this.tag.getSourceLocation(), "Duplicate VO name 'x'.");
      } catch (StructuredVOAlreadyExistsException e) {
        throw new InvalidConfigurationFileException(this.tag.getSourceLocation(), "Duplicate VO name 'x'.");
      }

    } else {

      // Structured columns

      try {
        log.debug("Phase 2");
        this.tag.getStructuredColumns().gatherMetadataPhase2(conn2);
        this.structuredColumns = this.tag.getStructuredColumns().getMetadata();

      } catch (InvalidSQLException e) {
        throw new ControlledException("Could not retrieve metadata for <" + new SelectMethodTag().getTagName() + "> at "
            + this.tag.getSourceLocation().render() + "; could not create temporary SQL view for it.\n" + "[ "
            + e.getMessage() + " ]\n" + "* Do all resulting columns have different and valid names?\n"
            + "* Is the create view SQL code below valid?\n" + "--- begin SQL ---\n" + e.getInvalidSQL()
            + "\n--- end SQL ---");
      } catch (UnresolvableDataTypeException e) {
        throw new ControlledException("Could not retrieve metadata for <" + new SelectMethodTag().getTagName() + "> at "
            + this.tag.getSourceLocation().render() + ": could not find suitable Java type for column '"
            + e.getColumnName() + "' ");
      }

    }

  }

  public void prepareUnstructuredColumnsRetrieval(final Connection conn) throws SQLException {

    log.debug("prepare view 0");

    ParameterRenderer JDBCParameterRenderer = new ParameterRenderer() {
      @Override
      public String render(final SQLParameter parameter) {
        return "?";
      }
    };

    log.debug("prepare view 1");

    // String foundation = this.tag.getSQLFoundation(JDBCParameterRenderer);
    String foundation = this.tag.renderSQLSentence(JDBCParameterRenderer);
    this.cleanedUpFoundation = cleanUpSQL(foundation);
    this.tempViewName = this.selectGenerationTag.getNextTempViewName();
    this.createView = this.adapter.createOrReplaceView(this.tempViewName, this.cleanedUpFoundation);
    String dropView = this.adapter.dropView(this.tempViewName);

    // 1. Drop (if exists) the view.

    log.debug("prepare view - will drop view: " + dropView);

    {
      PreparedStatement ps = null;
      try {
        ps = conn.prepareStatement(dropView);
        log.debug("prepare view - will execute drop view");
        ps.execute();
        log.debug("prepare view - view dropped");

      } catch (Exception e) {
        log.debug("prepare view - exception while dropping view", e);
        // Ignore this exception
      } finally {
        JdbcUtil.closeDbResources(ps);
      }
    }

    // 2. Create or replace the view.

    log.debug("prepare view - will create view: " + this.createView);

    {
      PreparedStatement ps = null;
      try {
        ps = conn.prepareStatement(this.createView);
        log.debug("prepare view - will execute create view");
        ps.execute();
        log.debug("prepare view - view created");

      } finally {
        log.debug("prepare view - will close resources");
        JdbcUtil.closeDbResources(ps);
      }
    }

  }

  private String cleanUpSQL(final String select) {
    ListWriter lw = new ListWriter("\n");
    String[] lines = select.split("\n");

    // Trim lines and remove empty ones

    for (String line : lines) {
      String trimmed = line.trim();
      if (!trimmed.isEmpty()) {
        lw.add(line);
      }
    }

    // Remove trailing semi-colons

    String reassembled = lw.toString();
    while (reassembled.endsWith(";")) {
      reassembled = reassembled.substring(0, reassembled.length() - 1);
    }

    return reassembled;
  }

  public void retrieveFlatColumnsMetadata(final Connection conn2) throws SQLException, UnresolvableDataTypeException {

    // 1. Retrieve the temp view column's metadata

    ResultSet rs = null;

    try {
      String viewJdbcName = this.adapter.formatJdbcTableName(this.tempViewName);
      rs = conn2.getMetaData().getColumns(this.loc.getDefaultCatalog(), this.loc.getDefaultSchema(), viewJdbcName,
          null);
      this.nonStructuredColumns = new ArrayList<ColumnMetadata>();
      while (rs.next()) {
        JdbcColumn c = this.db.retrieveColumn(rs, this.tempViewName);
        ColumnTag columnTag = this.tag.findColumnTag(c.getName(), this.adapter);
        log.debug("c=" + c.getName() + " / col: " + columnTag);
        ColumnMetadata cm = new ColumnMetadata(this, c, this.tag.getMethod(), this.adapter, columnTag, false, false);
        log.debug(" --> type=" + cm.getType());
        this.nonStructuredColumns.add(cm);
      }

    } finally {
      JdbcUtil.closeDbResources(rs);
    }

    // 2. Drop the temp view

    PreparedStatement ps = null;

    try {
      String dropView = this.adapter.dropView(this.tempViewName);
      ps = conn2.prepareStatement(dropView);
      ps.execute();

    } finally {
      JdbcUtil.closeDbResources(ps);
    }

  }

  // Indexing methods

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((tag == null) ? 0 : tag.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SelectMethodMetadata other = (SelectMethodMetadata) obj;
    if (tag == null) {
      if (other.tag != null)
        return false;
    } else if (!tag.equals(other.tag))
      return false;
    return true;
  }

  // Getters

  public boolean isStructured() {
    return structured;
  }

  public List<ColumnMetadata> getNonStructuredColumns() {
    return this.nonStructuredColumns;
  }

  public StructuredColumnsMetadata getStructuredColumns() {
    return this.tag.getStructuredColumns() == null ? null : this.tag.getStructuredColumns().getMetadata();
  }

  public String getVO() {
    return this.tag.getVO();
  }

  // Other getters

  public String getMethod() {
    return this.tag.getMethod();
  }

  public String getCreateView() {
    return createView;
  }

  @Override
  public List<ColumnMetadata> getColumns() {
    return this.nonStructuredColumns;
  }

  @Override
  public List<ColumnMetadata> getNonPkColumns() {
    return this.nonStructuredColumns;
  }

  @Override
  public KeyMetadata getPK() {
    return null;
  }

  @Override
  public DataSetIdentifier getIdentifier() {
    return new DataSetIdentifier("m", this.getMethod(), false);
  }

  @Override
  public List<KeyMetadata> getUniqueIndexes() {
    return new ArrayList<KeyMetadata>();
  }

  @Override
  public List<ForeignKeyMetadata> getImportedFKs() {
    return new ArrayList<ForeignKeyMetadata>();
  }

  @Override
  public List<ForeignKeyMetadata> getExportedFKs() {
    return new ArrayList<ForeignKeyMetadata>();
  }

  @Override
  public List<SelectParameterMetadata> getParameters() {
    List<SelectParameterMetadata> pms = new ArrayList<SelectParameterMetadata>();
    for (ParameterTag p : this.tag.getParameterDefinitions()) {
      pms.add(new SelectParameterMetadata(p));
    }
    return pms;
  }

  @Override
  public List<SelectParameterMetadata> getParameterDefinitions() {
    List<SelectParameterMetadata> pms = new ArrayList<SelectParameterMetadata>();
    for (ParameterTag p : this.tag.getParameterDefinitions()) {
      pms.add(new SelectParameterMetadata(p));
    }
    return pms;
  }

  @Override
  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    return this.tag.renderSQLSentence(parameterRenderer);
  }

  // TODO: implement this method correctly.

  @Override
  public String renderXML(final ParameterRenderer parameterRenderer) {
    if (!this.isStructured()) {
      log.info("non-structured");
      ListWriter w = new ListWriter("    ", "", ",\n");
      for (ColumnMetadata cm : this.nonStructuredColumns) {
        w.add(cm.renderSQLIdentifier());
      }
      return w.toString();
    } else {
      log.info("structured");
      ListWriter w = new ListWriter("    ", "", ",\n");
      for (VOMetadata vo : this.structuredColumns.getVOs()) {
        renderXMLVO(vo, w);
      }
      for (ExpressionsMetadata exp : this.structuredColumns.getExpressions()) {
        for (StructuredColumnMetadata m : exp.getColumns()) {
          w.add(m.renderSQLIdentifier() + " as " + m.getColumnAlias());
        }
      }
      return w.toString();
    }
  }

  private void renderXMLVO(final VOMetadata vo, final ListWriter w) {
    for (VOMetadata a : vo.getAssociations()) {
      renderXMLVO(a, w);
    }
    for (VOMetadata c : vo.getCollections()) {
      renderXMLVO(c, w);
    }
    for (ExpressionsMetadata exp : this.structuredColumns.getExpressions()) {
      for (StructuredColumnMetadata m : exp.getColumns()) {
        w.add(m.renderSQLIdentifier() + " as " + m.getColumnAlias());
      }
    }

  }

  @Override
  public AutoGeneratedColumnMetadata getAutoGeneratedColumnMetadata() {
    return null;
  }

  @Override
  public VersionControlMetadata getVersionControlMetadata() {
    return null;
  }

  @Override
  @Deprecated
  public String generateDAOName(final Identifier identifier) {
    return this.config.getGenerators().getSelectedGeneratorTag().getDaos().generateDAOName(identifier);
  }

  @Override
  @Deprecated
  public String generatePrimitivesName(final Identifier identifier) {
    return this.config.getGenerators().getSelectedGeneratorTag().getDaos().generatePrimitivesName(identifier);
  }

  @Override
  public String renderSQLIdentifier() {
    return "N/A";
  }

  @Override
  public HotRodFragmentConfigTag getFragmentConfig() {
    return this.fragmentConfig;
  }

  public boolean isMultipleRows() {
    return this.tag.isMultipleRows();
  }

  @Override
  @Deprecated
  public List<SelectMethodMetadata> getSelectsMetadata() {
    // Should not be used
    throw new UnsupportedOperationException("This operation should not be used.");
  }

  public HotRodGenerator getGenerator() {
    return generator;
  }

  public SelectMethodReturnType getReturnType(final ClassPackage voClassPackage) {
    return new SelectMethodReturnType(this, voClassPackage);
  }

  // Classes

  public static class SelectMethodReturnType {

    private VOClass soloVO;
    private VOMetadata connectedVO;

    private boolean multipleRows;

    public SelectMethodReturnType(final SelectMethodMetadata sm, final ClassPackage voClassPackage) {
      if (sm.isStructured()) { // structured columns
        StructuredColumnsMetadata scols = sm.getStructuredColumns();
        if (scols.getVO() == null) { // inner VO
          this.soloVO = null;
          this.connectedVO = scols.getVOs().get(0);
        } else { // columns VO (specified in the <columns> tag)
          List<VOMember> associations = new ArrayList<VOMember>();
          for (VOMetadata vo : sm.getStructuredColumns().getVOs()) {
            VOMember m = new VOMember(vo.getProperty(), vo.getClassPackage(), vo.getName());
            associations.add(m);
          }
          this.soloVO = new VOClass(sm, voClassPackage, scols.getVO(),
              sm.getStructuredColumns().getExpressionsColumns(), associations);
          this.connectedVO = null;
        }
      } else { // non-structured columns
        List<VOMember> associations = new ArrayList<VOMember>();
        this.soloVO = new VOClass(sm, voClassPackage, sm.getVO(), sm.getColumns(), associations);
        this.connectedVO = null;
      }
      this.multipleRows = sm.isMultipleRows();
    }

    public VOClass getSoloVO() {
      return soloVO;
    }

    public VOMetadata getConnectedVO() {
      return connectedVO;
    }

    public boolean isMultipleRows() {
      return multipleRows;
    }

    // Simpler methods

    private ClassPackage getReturnVOPackage() { // primitives.accounting
      return this.soloVO != null ? this.soloVO.getClassPackage() : this.connectedVO.getClassPackage();
    }

    private String getReturnVOType() { // AccountPersonVO
      return this.soloVO != null ? this.soloVO.getName() : this.connectedVO.getName();
    }

    public String getReturnType() { // List<AccountPersonVO>
      return this.multipleRows ? "List<" + getReturnVOType() + ">" : getReturnVOType();
    }

    public String getVOFullClassName() { // primitives.accounting.AccountPersonVO
      return this.getReturnVOPackage().getFullClassName(getReturnVOType());
    }

  }

}
