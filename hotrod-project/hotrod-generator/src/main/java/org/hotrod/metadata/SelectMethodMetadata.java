package org.hotrod.metadata;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.EnhancedSQLPart.SQLFormatter;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.SelectClassTag;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.InvalidSQLException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.generator.mybatis.DataSetLayout;
import org.hotrod.metadata.VOMetadata.DuplicatePropertyNameException;
import org.hotrod.metadata.VOMetadata.VOMember;
import org.hotrod.metadata.VORegistry.SelectVOClass;
import org.hotrod.metadata.VORegistry.StructuredVOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOProperty;
import org.hotrod.metadata.VORegistry.VOProperty.EnclosingTagType;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.hotrod.utils.identifiers.Id;
import org.hotrod.utils.identifiers.ObjectId;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.utils.JdbcUtil;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class SelectMethodMetadata implements DataSetMetadata, Serializable {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(SelectMethodMetadata.class);

  // Properties

  private boolean structured;

  private transient HotRodGenerator generator;
  private transient DataSetLayout layout;
  private transient JdbcDatabase db;
  private HotRodConfigTag config;
  private transient DatabaseAdapter adapter;
  private transient DatabaseLocation loc;
  private SelectMethodTag tag;
  private transient SelectGenerationTag selectGenerationTag;
  private transient ColumnsPrefixGenerator columnsPrefixGenerator;

  private transient HotRodFragmentConfigTag fragmentConfig;
  private List<ColumnMetadata> nonStructuredColumns;
  private StructuredColumnsMetadata structuredColumns;

  private transient String cleanedUpFoundation;
  private transient String tempViewName;
  private transient String createView;

  private ObjectId id;

  private ClassPackage classPackage;

  private SelectMethodReturnType selectMethodReturnType;

  // Constructor

  public SelectMethodMetadata(final HotRodGenerator generator, final SelectMethodTag tag, final HotRodConfigTag config,
      final SelectGenerationTag selectGenerationTag, final ColumnsPrefixGenerator columnsPrefixGenerator,
      final DataSetLayout layout) throws InvalidIdentifierException {
    this.generator = generator;
    this.layout = layout;
    this.db = generator.getJdbcDatabase();
    this.config = config;
    this.adapter = generator.getAdapter();
    this.loc = generator.getLoc();
    this.tag = tag;
    this.id = new ObjectId(null, null, Id.fromJavaMember(tag.getMethod()), adapter);
    this.selectGenerationTag = selectGenerationTag;
    this.columnsPrefixGenerator = columnsPrefixGenerator;

    this.fragmentConfig = tag.getFragmentConfig();
    this.structured = this.tag.getStructuredColumns() != null;
    this.nonStructuredColumns = null;
    this.structuredColumns = null;

    ClassPackage fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage()
        : null;
    this.classPackage = layout.getDAOPackage(fragmentPackage);

    this.selectMethodReturnType = null;

  }

  // Behavior

  public void gatherMetadataPhase1(final Connection conn1) throws InvalidConfigurationFileException {

    if (!this.structured) {

      // Unstructured columns

      try {
        prepareUnstructuredColumnsRetrieval(conn1);
      } catch (InvalidSQLException e) {
        throw new InvalidConfigurationFileException(this.tag, //
            "Could not retrieve metadata for <" + new SelectClassTag().getTagName()
                + "> tag while creating a temporary SQL view for it.\n" + "* " + e.getCause().getMessage() + "\n"
                + "* Is the create view SQL code below valid?\n" + "--- begin SQL ---\n" + e.getInvalidSQL()
                + "\n--- end SQL ---", //
            "Error in " + this.tag.getSourceLocation().render() + ":\n" + "Could not retrieve metadata for <"
                + new SelectClassTag().getTagName() + "> tag while creating a temporary SQL view for it.\n" + "* "
                + e.getCause().getMessage() + "\n" + "* Is the create view SQL code below valid?\n"
                + "--- begin SQL ---\n" + e.getInvalidSQL() + "\n--- end SQL ---");
      }

    } else {

      // Structured columns

      try {
        log.debug("Phase 1 - method=" + this.getMethod());
        this.tag.getStructuredColumns().gatherMetadataPhase1(this.tag, this.selectGenerationTag,
            this.columnsPrefixGenerator, conn1);
      } catch (InvalidSQLException e) {
        throw new InvalidConfigurationFileException(this.tag, //
            "Could not retrieve metadata for <" + this.tag.getTagName()
                + "> tag while creating the temporary SQL view for it.\n" + "* " + e.getCause().getMessage() + "\n"
                + "* Is the create view SQL code below valid?\n" + "--- begin SQL ---\n" + e.getInvalidSQL()
                + "\n--- end SQL ---", //
            "Error in " + this.tag.getSourceLocation().render() + ":\n" + "Could not retrieve metadata for <"
                + this.tag.getTagName() + "> tag while creating the temporary SQL view for it.\n" + "* "
                + e.getCause().getMessage() + "\n" + "* Is the create view SQL code below valid?\n"
                + "--- begin SQL ---\n" + e.getInvalidSQL() + "\n--- end SQL ---");
      }

    }

  }

  public void gatherMetadataPhase2(final Connection conn2, final VORegistry voRegistry)
      throws UncontrolledException, InvalidConfigurationFileException {

    if (!this.structured) {

      // Non-structured columns

      try {
        retrieveFlatColumnsMetadata(conn2);

      } catch (SQLException e) {
        throw new UncontrolledException("Could not retrieve metadata for <" + new SelectMethodTag().getTagName()
            + "> at " + this.tag.getSourceLocation().render(), e);
      } catch (UnresolvableDataTypeException e) {
        String msg = "Could not retrieve metadata for <" + new SelectMethodTag().getTagName()
            + ">: could not find suitable Java type for column '" + e.getColumnName() + "' ";
        throw new InvalidConfigurationFileException(this.tag, msg, msg);
      } catch (InvalidIdentifierException e) {
        String msg = "Invalid retrieved column name: " + e.getMessage();
        throw new InvalidConfigurationFileException(this.tag, msg, msg);
      }

      List<VOProperty> properties = new ArrayList<VOProperty>();

      for (ColumnMetadata cm : this.nonStructuredColumns) {
        StructuredColumnMetadata m = new StructuredColumnMetadata(cm, "entityPrefix1", "columnAlias", false, this.tag);
        properties
            .add(new VOProperty(m.getId().getJavaMemberName(), m, EnclosingTagType.NON_STRUCTURED_SELECT, this.tag));
      }

      List<VOMember> associations = new ArrayList<VOMember>();
      List<VOMember> collections = new ArrayList<VOMember>();

      SelectVOClass vo = null;
      try {
        vo = new SelectVOClass(this.classPackage, this.tag.getVOClassName(), null, properties, associations,
            collections, this.tag);
        log.debug("--> Adding VO: " + vo);
        voRegistry.addVO(vo);
      } catch (VOAlreadyExistsException e) {
        throw new InvalidConfigurationFileException(this.tag, //
            "Duplicate VO name '" + vo.getName() + "' in package '" + vo.getClassPackage().getPackage()
                + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render(), //
            "Duplicate VO name '" + vo.getName() + "' in package '" + vo.getClassPackage().getPackage()
                + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render()
                + ".");
      } catch (StructuredVOAlreadyExistsException e) {
        throw new InvalidConfigurationFileException(this.tag, //
            "Duplicate VO name '" + vo.getName() + "' in package '" + vo.getClassPackage().getPackage()
                + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render(), //
            "Duplicate VO name '" + vo.getName() + "' in package '" + vo.getClassPackage().getPackage()
                + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render()
                + ".");
      } catch (DuplicatePropertyNameException e) {
        throw new InvalidConfigurationFileException(e.getInitial().getTag(), //
            e.renderMessage(), //
            e.renderMessage());
      }

    } else {

      // Structured columns

      try {
        log.debug("Phase 2");
        this.tag.getStructuredColumns().gatherMetadataPhase2(conn2, this.config.getTypeSolverTag());
        this.structuredColumns = this.tag.getStructuredColumns().getMetadata();
        this.structuredColumns.registerVOs(this.classPackage, voRegistry);

      } catch (InvalidSQLException e) {
        String msg = "Could not create temporary SQL view to retrieve metadata.\n" + "[ " + e.getMessage() + " ]\n"
            + "* Do all resulting columns have different and valid names?\n"
            + "* Is the create view SQL code below valid?\n" + "--- begin SQL ---\n" + e.getInvalidSQL()
            + "\n--- end SQL ---";
        throw new InvalidConfigurationFileException(this.tag, msg, msg);
      } catch (UnresolvableDataTypeException e) {
        String msg = "Could not retrieve metadata: could not find suitable Java type for column '" + e.getColumnName()
            + "' ";
        throw new InvalidConfigurationFileException(this.tag, msg, msg);
      } catch (VOAlreadyExistsException e) {
        throw new InvalidConfigurationFileException(e.getTag(), //
            "Duplicate VO name '" + e.getThisName() + "' in package '" + e.getThisPackage().getPackage()
                + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render(), //
            "Duplicate VO name '" + e.getThisName() + "' in package '" + e.getThisPackage().getPackage()
                + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render()
                + ".");
      } catch (StructuredVOAlreadyExistsException e) {
        throw new InvalidConfigurationFileException(e.getThisTag(), //
            "Duplicate VO name '" + e.getThisName() + "' in package '" + e.getThisPackage().getPackage()
                + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render(), //
            "Duplicate VO name '" + e.getThisName() + "' in package '" + e.getThisPackage().getPackage()
                + "'. This VO name is already being used in " + e.getOtherOne().getTag().getSourceLocation().render()
                + ".");
      } catch (DuplicatePropertyNameException e) {
        throw new InvalidConfigurationFileException(e.getDuplicate().getTag(), //
            e.renderMessage(), //
            e.renderMessage());
      }

    }

    this.selectMethodReturnType = new SelectMethodReturnType(this, this.classPackage, this.tag, this.layout);

  }

  public void prepareUnstructuredColumnsRetrieval(final Connection conn) throws InvalidSQLException {

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

      } catch (SQLException e) {
        throw new InvalidSQLException(this.createView, e);
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

    // Remove trailing semicolons

    String reassembled = lw.toString();
    while (reassembled.endsWith(";")) {
      reassembled = reassembled.substring(0, reassembled.length() - 1);
    }

    return reassembled;
  }

  public void retrieveFlatColumnsMetadata(final Connection conn2)
      throws SQLException, UnresolvableDataTypeException, InvalidIdentifierException {

    // 1. Retrieve the temp view column's metadata

    ResultSet rs = null;

    try {
      String viewJdbcName = this.adapter.formatJdbcTableName(this.tempViewName);
      rs = conn2.getMetaData().getColumns(this.loc.getDefaultCatalog(), this.loc.getDefaultSchema(), viewJdbcName,
          null);
      this.nonStructuredColumns = new ArrayList<ColumnMetadata>();
      while (rs.next()) {
        JdbcColumn c = this.db.retrieveSelectColumn(rs);
        ColumnTag columnTag = this.tag.findColumnTag(c.getName(), this.adapter);
        log.debug("c=" + c.getName() + " / col: " + columnTag);
        ColumnMetadata cm = new ColumnMetadata(this, c, this.tag.getMethod(), this.adapter, columnTag, false, false,
            this.config.getTypeSolverTag());
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

  // Indexable

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

  public String getVOClassName() {
    return this.tag.getVOClassName();
  }

  public String getAbstractVOClassName() {
    return this.tag.getAbstractVOClassName();
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
  public ObjectId getId() {
    return this.id;
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

  @Override
  public String renderXML(final ParameterRenderer parameterRenderer) {
    SQLFormatter formatter = new SQLFormatter();
    this.tag.renderXML(formatter, parameterRenderer);
    return formatter.toString();
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
  public String generateDAOName(final ObjectId identifier) {
    return this.config.getGenerators().getSelectedGeneratorTag().getDaos().generateDAOName(identifier);
  }

  @Override
  @Deprecated
  public String generatePrimitivesName(final ObjectId identifier) {
    return this.config.getGenerators().getSelectedGeneratorTag().getDaos().generatePrimitivesName(identifier);
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
    return this.selectMethodReturnType;
  }

  public boolean metadataComplete() {
    return this.selectMethodReturnType != null;
  }

  // Classes

  public static class SelectMethodReturnType implements Serializable {

    private static final long serialVersionUID = 1L;

    private SelectVOClass soloVO;
    private SelectVOClass abstractSoloVO;
    private transient VOMetadata connectedVO;

    private boolean multipleRows;

    public SelectMethodReturnType(final SelectMethodMetadata sm, final ClassPackage voClassPackage,
        final AbstractConfigurationTag tag, final DataSetLayout layout) throws InvalidConfigurationFileException {

      if (sm.isStructured()) { // structured columns

        StructuredColumnsMetadata structCols = sm.getStructuredColumns();
        if (structCols.getSoloVOClass() == null) { // it's a connected VO
          log.trace(">>> it's a connected VO (1)");
          this.soloVO = null;
          this.abstractSoloVO = null;

          this.connectedVO = structCols.getVOs().get(0);

        } else { // solo VO from a <columns> tag
          log.info(">>> solo VO from a <columns> tag (2)");
          List<VOMember> associations = new ArrayList<VOMember>();
          for (VOMetadata vo : sm.getStructuredColumns().getVOs()) {
            VOMember m;
            try {
              m = new VOMember(vo.getProperty(), vo.getClassPackage(), vo.getName(), vo.getTag());
            } catch (InvalidIdentifierException e) {
              String msg = "Invalid property '" + vo.getProperty() + "':" + e.getMessage();
              throw new InvalidConfigurationFileException(tag, msg, msg);
            }
            associations.add(m);
          }
          this.soloVO = structCols.getSoloVOClass();
          this.connectedVO = null;
        }

      } else { // solo VO from non-structured columns
        log.trace(">>> solo VO (3)");

        List<VOProperty> properties = new ArrayList<VOProperty>();
        for (ColumnMetadata cm : sm.getNonStructuredColumns()) {
          StructuredColumnMetadata m = new StructuredColumnMetadata(cm, "entityPrefix2", "columnAlias", false, null);
          VOProperty p = new VOProperty(cm.getId().getJavaMemberName(), m, EnclosingTagType.NON_STRUCTURED_SELECT,
              sm.tag);
          properties.add(p);
        }
        List<VOMember> associations = new ArrayList<VOMember>();
        List<VOMember> collections = new ArrayList<VOMember>();
        try {
          this.soloVO = new SelectVOClass(voClassPackage, sm.getVOClassName(), null, properties, associations,
              collections, tag);
          this.abstractSoloVO = new SelectVOClass(voClassPackage, sm.getAbstractVOClassName(), null, properties,
              associations, collections, tag);
        } catch (DuplicatePropertyNameException e) {
          // swallow this exception
        }
        this.connectedVO = null;

      }
      this.multipleRows = sm.isMultipleRows();
      log.trace(">>> sm.getVOClassName()=" + sm.getVOClassName() + " sm.getAbstractVOClassName()="
          + sm.getAbstractVOClassName());
      log.trace("this.soloVO.getName()=" + (this.soloVO == null ? "null" : this.soloVO.getName())
          + " this.connectedVO.getName()=" + (this.connectedVO == null ? "null" : this.connectedVO.getName()));

    }

    public SelectVOClass getSoloVO() {
      return soloVO;
    }

    public SelectVOClass getAbstractSoloVO() {
      return this.abstractSoloVO;
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

  @Override
  public AbstractDAOTag getDaoTag() {
    throw new UnsupportedOperationException("This operation is not supported ina select method tag.");
  }

}