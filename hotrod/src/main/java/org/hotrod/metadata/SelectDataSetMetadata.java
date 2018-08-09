package org.hotrod.metadata;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.AbstractDAOTag;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.SelectClassTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.utils.identifiers2.Id;
import org.hotrod.utils.identifiers2.ObjectId;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.utils.JdbcUtil;

public class SelectDataSetMetadata implements DataSetMetadata, Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger log = Logger.getLogger(SelectDataSetMetadata.class);

  private transient JdbcDatabase db;
  private transient HotRodConfigTag config;
  private transient DatabaseAdapter adapter;
  private transient DatabaseLocation loc;
  private SelectClassTag tag;
  private List<ColumnMetadata> columns;
  private String tempViewName;
  private transient HotRodFragmentConfigTag fragmentConfig;

  private ObjectId id;

  private String reducedSelect;
  private String createView;

  public SelectDataSetMetadata(final JdbcDatabase db, final DatabaseAdapter adapter, final DatabaseLocation loc,
      final SelectClassTag tag, final String tempViewName, final HotRodConfigTag config)
      throws InvalidIdentifierException {
    this.db = db;
    this.config = config;
    this.adapter = adapter;
    this.loc = loc;
    this.tag = tag;
    this.id = new ObjectId(null, null, Id.fromJavaClass(tag.getJavaClassName()));
    this.tempViewName = tempViewName;
    this.fragmentConfig = tag.getFragmentConfig();
  }

  public void prepareViews(final Connection conn) throws SQLException {

    log.debug("prepare view 0");

    ParameterRenderer simpleParameterRenderer = new ParameterRenderer() {
      @Override
      public String render(final SQLParameter parameter) {
        return "?";
      }
    };

    log.debug("prepare view 1");

    String select = this.tag.getSQLFoundation(simpleParameterRenderer);
    this.reducedSelect = getReducedSelect(select);
    this.createView = this.adapter.createOrReplaceView(this.tempViewName, this.reducedSelect);
    String dropView = this.adapter.dropView(this.tempViewName);

    PreparedStatement ps = null;
    ResultSet rs = null;

    try {

      // 1. Drop (if exists) the view.

      log.debug("prepare view - will drop view: " + dropView);

      try {
        ps = conn.prepareStatement(dropView);
        log.debug("prepare view - will execute drop view");
        ps.execute();
      } catch (Exception e) {
        // Swallow this exception
      }

      // 2. Create or replace the view.

      log.debug("prepare view - will create view: " + this.createView);

      ps = conn.prepareStatement(this.createView);
      log.debug("prepare view - will execute create view");
      ps.execute();
      log.debug("prepare view - will close");
      ps.close();

    } finally {
      log.debug("prepare view - will close resources");

      try {
        JdbcUtil.closeDbResources(rs);
      } finally {
        if (ps != null) {
          ps.close();
        }
      }
    }

  }

  private String getReducedSelect(final String select) {
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

  public void retrieveColumnsMetadata(final Connection conn)
      throws SQLException, UnresolvableDataTypeException, InvalidConfigurationFileException {
    String dropView = this.adapter.dropView(this.tempViewName);

    PreparedStatement ps = null;
    ResultSet rs = null;

    try {

      // 1. Retrieve the view metadata.

      String viewJdbcName = this.adapter.formatJdbcTableName(this.tempViewName);

      rs = conn.getMetaData().getColumns(this.loc.getDefaultCatalog(), this.loc.getDefaultSchema(), viewJdbcName, null);
      this.columns = new ArrayList<ColumnMetadata>();
      while (rs.next()) {
        JdbcColumn c = this.db.retrieveColumn(rs, tempViewName);
        ColumnTag columnTag = this.tag.findColumnTag(c.getName(), this.adapter);
        log.debug("c=" + c.getName() + " / col: " + columnTag);
        ColumnMetadata cm;
        try {
          cm = new ColumnMetadata(this, c, this.tag.getJavaClassName(), this.adapter, columnTag, false, false);
        } catch (InvalidIdentifierException e) {
          String msg = "Invalid identifier for column '" + c.getName() + "': " + e.getMessage();
          throw new InvalidConfigurationFileException(this.tag, msg, msg);
        }
        log.debug(" --> type=" + cm.getType());
        this.columns.add(cm);
      }

      // 2. Drop the view.

      ps = conn.prepareStatement(dropView);
      ps.execute();

    } finally {
      try {
        JdbcUtil.closeDbResources(rs);
      } finally {
        if (ps != null) {
          ps.close();
        }
      }
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
    SelectDataSetMetadata other = (SelectDataSetMetadata) obj;
    if (tag == null) {
      if (other.tag != null)
        return false;
    } else if (!tag.equals(other.tag))
      return false;
    return true;
  }

  // Getters

  public String getReducedSelect() {
    return reducedSelect;
  }

  public SelectClassTag getSelectTag() {
    return tag;
  }

  public String getCreateView() {
    return createView;
  }

  @Override
  public List<ColumnMetadata> getColumns() {
    return this.columns;
  }

  @Override
  public List<ColumnMetadata> getNonPkColumns() {
    return this.columns;
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
  public String generateDAOName(final ObjectId identifier) {
    return this.config.getGenerators().getSelectedGeneratorTag().getDaos().generateDAOName(identifier);
  }

  @Override
  @Deprecated
  public String generatePrimitivesName(final ObjectId identifier) {
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

  @Override
  public List<SelectMethodMetadata> getSelectsMetadata() {
    // Nothing to return
    return null;
  }

  @Override
  public AbstractDAOTag getDaoTag() {
    return this.tag;
  }

}
