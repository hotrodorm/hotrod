package org.hotrod.generator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.structuredcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.InvalidSQLException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.ResultSetColumnsRetriever.RetrievalContext;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.SelectMethodMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.runtime.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrod.utils.SQLUtil;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.utils.JdbcUtil;

public class CreateViewColumnsRetriever implements ColumnsRetriever {

  private static final Logger log = LogManager.getLogger(CreateViewColumnsRetriever.class);

  private HotRodConfigTag config;
  private DatabaseLocation dloc;
  private SelectGenerationTag selectGenerationTag;
  private DatabaseAdapter adapter;
  private JdbcDatabase db;
  private Connection conn;
  private Map<String, RetrievalContext> contexts;

  private Connection conn2;

  public CreateViewColumnsRetriever(final HotRodConfigTag config, final DatabaseLocation dloc,
      final SelectGenerationTag selectGenerationTag, final DatabaseAdapter adapter, final JdbcDatabase db,
      final Connection conn) throws SQLException {
    this.config = config;
    this.dloc = dloc;
    this.selectGenerationTag = selectGenerationTag;
    this.adapter = adapter;
    this.db = db;
    this.conn = conn;
    this.contexts = new HashMap<String, RetrievalContext>();

    this.conn2 = null;
  }

  private class CreateViewParameterRenderer implements ParameterRenderer {

    private List<JDBCType> parameterJDBCTypes = new ArrayList<JDBCType>();

    @Override
    public String render(final SQLParameter parameter) {
      log.debug("prepare view 0.1 -- parameter=" + parameter.getDefinition());
      parameterJDBCTypes.add(parameter.getDefinition().getJDBCType());
      return "#{" + parameter.getName() + "}";
    }

  }

  // TODO: Flat <select> (just a marker)

  @Override
  public void phase1Flat(final String key, final SelectMethodTag tag, final SelectMethodMetadata sm)
      throws InvalidSQLException {

    log.debug("prepare view 0");

    RetrievalContext ctx = new RetrievalContext(tag, sm);
    this.contexts.put(key, ctx);

    log.debug("prepare view 1");

    CreateViewParameterRenderer pr = new CreateViewParameterRenderer();
    String foundation = tag.renderSQLSentence(pr);
    ctx.setCleanedUpFoundation(SQLUtil.cleanUpSQL(foundation));
    ctx.setTempViewName(this.selectGenerationTag.getNextTempViewName());
    String createView = this.adapter.createOrReplaceView(ctx.getTempViewName(), ctx.getCleanedUpFoundation());
    String dropView = this.adapter.dropView(ctx.getTempViewName());

    // 1. Drop (if exists) the view.

    log.debug("prepare view - will drop view: " + dropView);

    {
      PreparedStatement ps = null;
      try {
        ps = this.conn.prepareStatement(dropView);
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

    log.debug("prepare view - will create view: " + createView);

    {
      PreparedStatement ps = null;
      try {
        ps = this.conn.prepareStatement(createView);
        log.debug("prepare view - will execute create view");
        ps.execute();
        log.debug("prepare view - view created");

      } catch (SQLException e) {
        throw new InvalidSQLException(createView, e);
      } finally {
        log.debug("prepare view - will close resources");
        JdbcUtil.closeDbResources(ps);
      }
    }

  }

  @Override
  public List<ColumnMetadata> phase2Flat(final String key)
      throws SQLException, UnresolvableDataTypeException, InvalidIdentifierException {

    RetrievalContext ctx = this.contexts.get(key);

    // 1. Get the secondary connection if it's not ready yet

    produceExtraConnection();

    // 2. Retrieve the temp view column's metadata

    List<ColumnMetadata> nonStructuredColumns = new ArrayList<ColumnMetadata>();

    ResultSet rs = null;

    try {
      String viewJdbcName = this.adapter.formatJdbcTableName(ctx.getTempViewName());
      rs = conn2.getMetaData().getColumns(this.dloc.getCurrentCatalog(), this.dloc.getCurrentSchema(), viewJdbcName,
          null);
      while (rs.next()) {
        JdbcColumn c = this.db.retrieveSelectColumn(rs);
        ColumnTag columnTag = ctx.getTag().findColumnTag(c.getName(), this.adapter);
        log.debug("c=" + c.getName() + " / col: " + columnTag);
        ColumnMetadata cm = new ColumnMetadata(ctx.getSm(), c, ctx.getTag().getMethod(), this.adapter, columnTag, false,
            false, this.config.getTypeSolverTag());
        log.debug(" --> type=" + cm.getType());
        nonStructuredColumns.add(cm);
      }

    } finally {
      JdbcUtil.closeDbResources(rs);
    }

    // 3. Drop the temp view

    PreparedStatement ps = null;

    try {
      String dropView = this.adapter.dropView(ctx.getTempViewName());
      ps = conn2.prepareStatement(dropView);
      ps.execute();

    } finally {
      JdbcUtil.closeDbResources(ps);
    }

    return nonStructuredColumns;
  }

  // TODO: Graph <select> (just a marker)

  @Override
  public void phase1Structured(final String key, final SelectMethodTag selectTag, final String aliasPrefix,
      final String entityPrefix, final ColumnsProvider columnsProvider, final SelectMethodMetadata sm)
      throws InvalidSQLException {

    log.debug("prepare view 0");

    RetrievalContext ctx = new RetrievalContext(selectTag, sm);
    this.contexts.put(key, ctx);

    log.debug("prepare view 1");
    CreateViewParameterRenderer pr = new CreateViewParameterRenderer();
    String foundation = selectTag.renderSQLAngle(pr, columnsProvider, this.adapter);
    ctx.setCleanedUpFoundation(SQLUtil.cleanUpSQL(foundation));
    ctx.setTempViewName(this.selectGenerationTag.getNextTempViewName());
    String createView = this.adapter.createOrReplaceView(ctx.getTempViewName(), ctx.getCleanedUpFoundation());
    String dropView = this.adapter.dropView(ctx.getTempViewName());

    // 1. Drop (if exists) the view.

    log.debug("prepare view - will drop view: " + dropView);

    {
      PreparedStatement ps = null;
      try {
        ps = this.conn.prepareStatement(dropView);
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

    log.debug("prepare view - will create view: " + createView);

    {
      PreparedStatement ps = null;
      try {
        ps = this.conn.prepareStatement(createView);
        log.debug("prepare view - will execute create view");
        ps.execute();
        log.debug("prepare view - view created");

      } catch (SQLException e) {
        log.debug("prepare view - exception while creating view", e);
        throw new InvalidSQLException(createView, e);
      } finally {
        log.debug("prepare view - will close resources");
        JdbcUtil.closeDbResources(ps);
      }
    }

  }

  @Override
  public List<StructuredColumnMetadata> phase2Structured(final String key, final SelectMethodTag selectTag,
      final String aliasPrefix, final String entityPrefix, final ColumnsProvider columnsProvider)
      throws UnresolvableDataTypeException, InvalidConfigurationFileException, UncontrolledException {

    RetrievalContext ctx = this.contexts.get(key);

    // 1. Get the secondary connection if it's not ready yet

    try {
      produceExtraConnection();
    } catch (SQLException e) {
      throw new UncontrolledException("Could not retrieve database metadata", e);
    }

    String dropViewSQL = this.adapter.dropView(ctx.getTempViewName());

    List<StructuredColumnMetadata> columns = new ArrayList<StructuredColumnMetadata>();

    {
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {

        // 1. Retrieve the view meta data

        String viewJdbcName = this.adapter.formatJdbcTableName(ctx.getTempViewName());

        rs = this.conn2.getMetaData().getColumns(this.dloc.getCurrentCatalog(), this.dloc.getCurrentSchema(),
            viewJdbcName, null);
        while (rs.next()) {
          JdbcColumn c = this.db.retrieveSelectColumn(rs);
          ColumnMetadata cm;
          try {
            cm = new ColumnMetadata(null, c, ctx.getTag().getMethod(), this.adapter, null, false, false,
                this.config.getTypeSolverTag());
          } catch (InvalidIdentifierException e) {
            String msg = "Invalid identifier for column '" + c.getName() + "': " + e.getMessage();
            throw new InvalidConfigurationFileException(ctx.getTag(), msg);
          }

          String alias = aliasPrefix + cm.getName();
          StructuredColumnMetadata scm = new StructuredColumnMetadata(cm, entityPrefix, alias, false, null,
              columnsProvider);
          columns.add(scm);
        }

      } catch (SQLException e) {
        throw new UncontrolledException("Could not retrieve database metadata", e);
      } finally {
        JdbcUtil.closeDbResources(ps, rs);
      }
    }

    // 2. Drop the view.

    {
      PreparedStatement ps = null;

      try {

        ps = conn2.prepareStatement(dropViewSQL);
        ps.execute();

      } catch (SQLException e) {
        throw new UncontrolledException("Could not retrieve database metadata", e);
      } finally {
        JdbcUtil.closeDbResources(ps);
      }
    }

    // 3. Return the meta data

    return columns;

  }

  // TODO: End of graph <select> (just a marker)

  private void produceExtraConnection() throws SQLException {
    if (this.conn2 == null) {
      this.conn2 = this.dloc.getConnection();
    }
  }

  @Override
  public void close() throws Exception {
    if (this.conn2 != null) {
      this.conn2.close();
    }
  }

}
