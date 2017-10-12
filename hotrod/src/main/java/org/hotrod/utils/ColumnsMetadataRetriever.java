package org.hotrod.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.sqlcolumns.ColumnsProvider;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.runtime.util.ListWriter;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.utils.JdbcUtil;

public class ColumnsMetadataRetriever {

  // Constants

  private static final Logger log = Logger.getLogger(ColumnsMetadataRetriever.class);

  // Properties

  private SelectMethodTag selectTag;
  private DatabaseAdapter adapter;
  private JdbcDatabase db;
  private DatabaseLocation loc;
  private SelectGenerationTag selectGenerationTag;
  private ColumnsProvider columnsProvider;
  private String aliasPrefix;

  private String tempViewName;

  // Constructor

  public ColumnsMetadataRetriever(final SelectMethodTag selectTag, final DatabaseAdapter adapter, final JdbcDatabase db,
      final DatabaseLocation loc, final SelectGenerationTag selectGenerationTag, final ColumnsProvider columnsProvider,
      final ColumnsPrefixGenerator columnsPrefixGenerator) {
    this.selectTag = selectTag;
    this.adapter = adapter;
    this.db = db;
    this.loc = loc;
    this.selectGenerationTag = selectGenerationTag;
    this.columnsProvider = columnsProvider;
    this.aliasPrefix = columnsPrefixGenerator.next();
  }

  // Behavior

  public void prepareRetrieval(final Connection conn1) throws InvalidSQLException {

    log.debug("prepare view 0");

    ParameterRenderer JDBCParameterRenderer = new ParameterRenderer() {
      @Override
      public String render(final SQLParameter parameter) {
        return "?";
      }
    };

    log.debug("prepare view 1");

    String sqlAngle = this.selectTag.renderSQLAngle(JDBCParameterRenderer, this.columnsProvider, this.adapter);
    sqlAngle = clean(sqlAngle);

    this.tempViewName = this.selectGenerationTag.getNextTempViewName();

    String createViewSQL = this.adapter.createOrReplaceView(this.tempViewName, sqlAngle);
    String dropViewSQL = this.adapter.dropView(this.tempViewName);

    // 1. Drop (if exists) the view.

    log.debug("prepare view - will drop view: " + dropViewSQL);

    {
      PreparedStatement ps = null;
      try {
        ps = conn1.prepareStatement(dropViewSQL);
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

    log.debug("prepare view - will create view: " + createViewSQL);

    {
      PreparedStatement ps = null;
      try {
        ps = conn1.prepareStatement(createViewSQL);
        log.debug("prepare view - will execute create view");
        ps.execute();
        log.debug("prepare view - view created");

      } catch (SQLException e) {
        log.debug("prepare view - exception while creating view", e);
        throw new InvalidSQLException(createViewSQL, e);
      } finally {
        log.debug("prepare view - will close resources");
        JdbcUtil.closeDbResources(ps);
      }
    }

  }

  public List<StructuredColumnMetadata> retrieve(final Connection conn2)
      throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException {

    String dropViewSQL = this.adapter.dropView(this.tempViewName);

    List<StructuredColumnMetadata> columns = new ArrayList<StructuredColumnMetadata>();

    {
      PreparedStatement ps = null;
      ResultSet rs = null;

      try {

        // 1. Retrieve the view meta data

        String viewJdbcName = this.adapter.formatJdbcTableName(this.tempViewName);

        rs = conn2.getMetaData().getColumns(this.loc.getDefaultCatalog(), this.loc.getDefaultSchema(), viewJdbcName,
            null);
        while (rs.next()) {
          JdbcColumn c = this.db.retrieveColumn(rs, this.tempViewName);
          ColumnMetadata cm = new ColumnMetadata(null, c, this.selectTag.getMethod(), this.adapter, null, false, false);

          String alias = this.aliasPrefix + cm.getColumnName();
          StructuredColumnMetadata scm = new StructuredColumnMetadata(cm, alias);
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
        throw new InvalidSQLException(dropViewSQL, e);
      } finally {
        JdbcUtil.closeDbResources(ps);
      }
    }

    // 3. Return the meta data

    return columns;

  }

  // Utilities

  private String clean(final String sql) {
    ListWriter lw = new ListWriter("\n");
    String[] lines = sql.split("\n");

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

  // Exceptions

  public static class InvalidSQLException extends Exception {

    private static final long serialVersionUID = 1L;

    private String invalidSQL;

    public InvalidSQLException(final String invalidSQL, final Throwable cause) {
      super(cause);
      this.invalidSQL = invalidSQL;
    }

    public String getInvalidSQL() {
      return invalidSQL;
    }

  }

}
