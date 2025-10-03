package org.hotrod.livesql.queries.select.sets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.livesql.exceptions.LiveSQLException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.queries.LiveSQLContext;
import org.hotrod.livesql.queries.LiveSQLPreparedQuery;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.TableReferences;
import org.hotrod.livesql.queries.select.UnarySelectObject.AliasGenerator;
import org.hotrod.livesql.util.ToString;

public abstract class SelectObject<T> {

  private static final Logger log = Logger.getLogger(SelectObject.class.getName());

  private CombinedSelectObject<T> parent;

  public void setParent(final CombinedSelectObject<T> parent) {
    this.parent = parent;
  }

  public CombinedSelectObject<T> getParent() {
    return this.parent;
  }

  public abstract void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

  // Rendering

  public final void compileColumns() {
    this.prepareColumnCompilation();
    this.computeColumnsCompilation();
  }

  protected abstract void prepareColumnCompilation();

  protected abstract void computeColumnsCompilation();

  public abstract List<Expression> getCompiledColumns();

  public abstract boolean excludeTuplesFromUniqueNames();

  public abstract void renderTo(QueryWriter w, boolean inline);

  // Execution

  public abstract List<T> execute(final LiveSQLContext context);

  public abstract List<T> execute(final LiveSQLContext context, RowReader<T> rowReader);

  public abstract Cursor<T> executeCursor(final LiveSQLContext context) throws SQLException;

  public abstract Cursor<T> executeCursor(final LiveSQLContext context, Integer fetchSize) throws SQLException;

  public abstract Cursor<T> executeCursor(final LiveSQLContext context, RowReader<T> rowReader, Integer fetchSize)
      throws SQLException;

  public abstract T executeOne(final LiveSQLContext context);

  public abstract T executeOne(final LiveSQLContext context, RowReader<T> rowReader);

  public String getPreview(final LiveSQLContext context, final boolean includeParameters) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    return q.getPreview(includeParameters);
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

//    log.info("");
//    ToString t = new ToString();
//    this.log(t);

    this.compileColumns();
    List<Expression> columns = this.getCompiledColumns();
    renderTo(w, false);

//    log.info("");
//    t = new ToString();
//    this.log(t);
//    log.info("");

//    log.info(">> this=" + this.getClass().getName());
    return w.getPreparedQuery(columns, this.excludeTuplesFromUniqueNames());

  }

  public abstract void flatten();

  public abstract RowReader<T> getRowReader();

  protected List<T> executeLiveSQL(final LiveSQLContext context, final LiveSQLPreparedQuery q,
      final RowReader<T> rowReader) {
//    logExecution(context, q);

    List<T> rows = new ArrayList<>();
    try (Connection conn = context.getDataSource().getConnection()) {

      try (PreparedStatement ps = conn.prepareStatement(q.getSQL())) {

        // 1. Apply parameters

        int n = 1;
        for (Object obj : q.getParameters().values()) {
          int i = n++;
          ps.setObject(i, obj);
        }

        // 2. Run the query

        try (ResultSet rs = ps.executeQuery()) {

          final RowReader<T> effectiveRowReader = rowReader != null ? rowReader : new UnaryRowReader<>(context, q, rs);

          while (rs.next()) {
            T row = effectiveRowReader.readRowFrom(rs, conn);
            rows.add(row);
          }
          return rows;
        }

      }

    } catch (SQLException e) {
      log.log(Level.SEVERE, e.getMessage());
      throw new RuntimeException(e);
    }

  }

  protected T executeLiveSQLOne(final LiveSQLContext context, final LiveSQLPreparedQuery q,
      final RowReader<T> rowReader) {
//    logExecution(context, q);

    try (Connection conn = context.getDataSource().getConnection()) {

      try (PreparedStatement ps = conn.prepareStatement(q.getSQL())) {

        // 1. Apply parameters

        int n = 1;
        for (Object obj : q.getParameters().values()) {
          int i = n++;
          ps.setObject(i, obj);
        }

        // 2. Run the query

        try (ResultSet rs = ps.executeQuery()) {

          final RowReader<T> effectiveRowReader = rowReader != null ? rowReader : new UnaryRowReader<>(context, q, rs);
          T row = null;

          int count = 0;
          while (rs.next()) {
            count++;
            if (count > 1) {
              throw new LiveSQLException("A single row at most was expected by this query but received at least two");
            }
            row = effectiveRowReader.readRowFrom(rs, conn);
          }
          return row;
        }

      }

    } catch (SQLException e) {
      log.log(Level.SEVERE, e.getMessage());
      throw new RuntimeException(e);
    }
  }

  protected Cursor<T> executeLiveSQLCursor(final LiveSQLContext context, final LiveSQLPreparedQuery q)
      throws SQLException {
    return executeLiveSQLCursor(context, q, null, null);
  }

  protected Cursor<T> executeLiveSQLCursor(final LiveSQLContext context, final LiveSQLPreparedQuery q,
      final RowReader<T> rowReader, Integer fetchSize) throws SQLException {
    return new RowCursor<>(context, q, rowReader, fetchSize);
  }

  protected abstract void log(ToString t);

//  void logExecution(final LiveSQLContext context, final LiveSQLPreparedQuery q) {
//    if (context.getLogger().isLoggable(Level.FINER)) {
//      context.getLogger().finest("SQL: " + q.getPreview(true));
//    } else if (context.getLogger().isLoggable(Level.FINE)) {
//      context.getLogger().fine("SQL: " + q.getPreview(false));
//    }
//  }

}
