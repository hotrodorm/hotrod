package org.hotrod.runtime.livesql.queries.select.sets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.runtime.converter.TypeConverter;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.QueryColumn;
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

  // Rendering

  protected abstract void computeQueryColumns();

  protected abstract LinkedHashMap<String, QueryColumn> getQueryColumns();

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

    QueryWriter w = new QueryWriter(context);
    this.computeQueryColumns();
    renderTo(w, false);
    return w.getPreparedQuery(this);

  }

  public abstract void flatten();

  // Utilities

  @SuppressWarnings("unchecked")
  protected List<R> executeLiveSQL(final LiveSQLContext context, final LiveSQLPreparedQuery q) {
    log.info("### executeLiveSQL()");
    if (context.usePlainJDBC()) {
      log.info("### Using Plain JDBC");
      List<Row> rows = new ArrayList<>();
      try {
        Connection conn = context.getDataSource().getConnection();
        PreparedStatement ps = conn.prepareStatement(q.getSQL());

        // Setting parameters

        int n = 1;
        for (Object obj : q.getParameters().values()) {
          int i = n++;
          log.fine("set parameter #" + i + ": " + obj);
          ps.setObject(i, obj);
        }

        // Logging query column types

        LinkedHashMap<String, QueryColumn> queryColumns = q.getQueryColumns();
        n = 1;
        for (QueryColumn qc : queryColumns.values()) {
          int i = n++;
          log.info("- column #" + i + " '" + qc.getName() + "': " + qc.getTypeHandler());
        }

        // Running the query

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          Row r = new Row();
          int i = 1;
          for (QueryColumn qc : queryColumns.values()) {
            Object value;
            if (qc.getTypeHandler().getConverter() == null) {
              value = rs.getObject(i, qc.getTypeHandler().getJavaClass());
            } else {
              Object raw = rs.getObject(i, qc.getTypeHandler().getRawClass());
              TypeConverter<?, ?> converter = qc.getTypeHandler().getConverter();
              value = this.applyConverter(raw, converter, conn);
            }
            r.put(qc.getName(), value);
            i++;
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

  private Object applyConverter(final Object raw, final TypeConverter<?, ?> converter, final Connection conn) {

    Method m;
    try {
      m = TypeConverter.class.getMethod("decode", Object.class, Connection.class);
    } catch (NoSuchMethodException | SecurityException e) {
      throw new RuntimeException("Could not use converter", e);
    }

    Object value;
    try {
      value = m.invoke(converter, raw, conn);
    } catch (InvocationTargetException e) {
      throw new RuntimeException("Converter's decode() method threw an exception", e);
    } catch (IllegalAccessException | IllegalArgumentException e) {
      throw new RuntimeException("Could not invoke converter's decode() method", e);
    }

    return value;
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