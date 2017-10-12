package org.hotrod.metadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.sqlcolumns.VOTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.utils.identifiers.DataSetIdentifier;
import org.hotrod.utils.identifiers.Identifier;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.utils.JdbcUtil;

public class SelectMethodDataSetMetadata implements DataSetMetadata {

  private static final Logger log = Logger.getLogger(SelectMethodDataSetMetadata.class);

  private JdbcDatabase db;
  private HotRodConfigTag config;
  private DatabaseAdapter adapter;
  private DatabaseLocation loc;
  private SelectMethodTag tag;
  private List<ColumnMetadata> unstructuredColumns;
  private String tempViewName;
  private HotRodFragmentConfigTag fragmentConfig;

  private String cleanedUpFoundation;
  private String createView;

  public SelectMethodDataSetMetadata(final JdbcDatabase db, final DatabaseAdapter adapter, final DatabaseLocation loc,
      final SelectMethodTag tag, final String tempViewName, final HotRodConfigTag config) {
    this.db = db;
    this.config = config;
    this.adapter = adapter;
    this.loc = loc;
    this.tag = tag;
    this.tempViewName = tempViewName;
    this.fragmentConfig = tag.getFragmentConfig();
  }

  public void prepareUnstructuredView(final Connection conn) throws SQLException {

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

  public void prepareSubsetView(final Connection conn, final VOTag voTag) throws SQLException {

    log.debug("prepare view 0");

    ParameterRenderer JDBCParameterRenderer = new ParameterRenderer() {
      @Override
      public String render(final SQLParameter parameter) {
        return "?";
      }
    };

    log.debug("prepare view 1");

    String tempViewName = this.config.getGenerators().getSelectedGeneratorTag().getSelectGeneration()
        .getNextTempViewName();

    // String subsetSQL = this.tag.getSQLFoundation(JDBCParameterRenderer);
    String subsetSQL = this.tag.renderSQLSentence(JDBCParameterRenderer);

    String cleanedUpSubset = cleanUpSQL(subsetSQL);
    String createView = this.adapter.createOrReplaceView(tempViewName, cleanedUpSubset);
    String dropView = this.adapter.dropView(tempViewName);

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

  public void retrieveColumnsMetadata(final Connection conn) throws SQLException, UnresolvableDataTypeException {

    // 1. Retrieve the temp view column's metadata

    ResultSet rs = null;

    try {
      String viewJdbcName = this.adapter.formatJdbcTableName(this.tempViewName);
      rs = conn.getMetaData().getColumns(this.loc.getDefaultCatalog(), this.loc.getDefaultSchema(), viewJdbcName, null);
      this.unstructuredColumns = new ArrayList<ColumnMetadata>();
      while (rs.next()) {
        JdbcColumn c = this.db.retrieveColumn(rs, this.tempViewName);
        ColumnTag columnTag = this.tag.findColumnTag(c.getName(), this.adapter);
        log.debug("c=" + c.getName() + " / col: " + columnTag);
        ColumnMetadata cm = new ColumnMetadata(this, c, this.tag.getMethod(), this.adapter, columnTag, false, false);
        log.debug(" --> type=" + cm.getType());
        this.unstructuredColumns.add(cm);
      }

    } finally {
      JdbcUtil.closeDbResources(rs);
    }

    // 2. Drop the temp view

    PreparedStatement ps = null;

    try {
      String dropView = this.adapter.dropView(this.tempViewName);
      ps = conn.prepareStatement(dropView);
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
    SelectMethodDataSetMetadata other = (SelectMethodDataSetMetadata) obj;
    if (tag == null) {
      if (other.tag != null)
        return false;
    } else if (!tag.equals(other.tag))
      return false;
    return true;
  }

  // Getters

  public String getReducedSelect() {
    return cleanedUpFoundation;
  }

  public SelectMethodTag getSelectMethodTag() {
    return tag;
  }

  public String getCreateView() {
    return createView;
  }

  @Override
  public List<ColumnMetadata> getColumns() {
    return this.unstructuredColumns;
  }

  @Override
  public List<ColumnMetadata> getNonPkColumns() {
    return this.unstructuredColumns;
  }

  @Override
  public KeyMetadata getPK() {
    return null;
  }

  @Override
  public DataSetIdentifier getIdentifier() {
    throw new UnsupportedOperationException("Identifier is not supported for a <select> method tag.");
    // return new DataSetIdentifier("s", this.tag.getJavaClassName(), false);
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
    return this.tag.renderXML(parameterRenderer);
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

}
