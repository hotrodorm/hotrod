package org.hotrod.runtime.livesql.queries.select.sets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.ColumnsCollector;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.QueryWriter.LiveSQLPreparedQuery;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.util.PreviewRenderer;

public abstract class MultiSet<R> {

  private static final Logger log = Logger.getLogger(MultiSet.class.getName());

  private CombinedSelectObject<R> parent;

  public void setParent(final CombinedSelectObject<R> parent) {
    this.parent = parent;
  }

  public CombinedSelectObject<R> getParent() {
    return this.parent;
  }

  public abstract void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

  public abstract List<ResultSetColumn> listColumns();

  public abstract void renderTo(QueryWriter w, boolean inline);

  // Execution

  public abstract List<R> execute(final LiveSQLContext context);

  public abstract Cursor<R> executeCursor(final LiveSQLContext context);

  public abstract R executeOne(final LiveSQLContext context);

  public String getPreview(final LiveSQLContext context) {
    log.fine("previewing");
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return PreviewRenderer.render(q);
  }

  protected LiveSQLPreparedQuery prepareQuery(final LiveSQLContext context) {

    // Validate

    TableReferences tableReferences = new TableReferences();
    AliasGenerator ag = new AliasGenerator();
    this.validateTableReferences(tableReferences, ag);

    // Flatten levels

    this.flatten();

    // Render

    ColumnsCollector columnsCollector = new ColumnsCollector();
    QueryWriter w = new QueryWriter(context, columnsCollector);
    renderTo(w, false);
    return w.getPreparedQuery();

  }

  public abstract void flatten();

  // Utilities

  @SuppressWarnings("unchecked")
  protected List<R> executeLiveSQL(final LiveSQLContext context, final LiveSQLPreparedQuery q) {
    if (context.usePlainJDBC()) {
      List<Row> rows = new ArrayList<>();
      try {
        Connection conn = context.getDataSource().getConnection();
        PreparedStatement ps = conn.prepareStatement(q.getSQL());

        int n = 1;
        for (Object obj : q.getParameters().values()) {
          int i = n++;
          log.info("set parameter #" + i + ": " + obj);
          ps.setObject(i, obj);
        }

        List<String> labels = new ArrayList<>();
        ResultSetMetaData rm = ps.getMetaData();
        for (int i = 1; i <= rm.getColumnCount(); i++) {
          labels.add(rm.getColumnLabel(i));
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          Row r = new Row();
          int i = 1;
          for (String label : labels) {
            r.put(label, rs.getObject(i++));
          }
          rows.add(r);
        }
        return (List<R>) rows;

      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

    } else {
      LinkedHashMap<String, Object> parameters = q.getParameters();
      parameters.put("sql", q.getSQL());
      return (List<R>) context.getLiveSQLMapper().select(parameters);
    }
  }

  @SuppressWarnings("unchecked")
  protected Cursor<R> executeLiveSQLCursor(final LiveSQLContext context, final LiveSQLPreparedQuery q) {
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    return (Cursor<R>) context.getLiveSQLMapper().selectCursor(parameters);
  }

  @SuppressWarnings("unchecked")
  protected R executeLiveSQLOne(final LiveSQLContext context, final LiveSQLPreparedQuery q) {
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    return (R) context.getLiveSQLMapper().selectOne(parameters);
  }

}
