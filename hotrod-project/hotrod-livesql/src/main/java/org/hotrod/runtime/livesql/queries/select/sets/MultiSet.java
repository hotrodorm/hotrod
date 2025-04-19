package org.hotrod.runtime.livesql.queries.select.sets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.cursors.Cursor;
import org.hotrod.dynamicsql.RowReader;
import org.hotrod.runtime.livesql.exceptions.LiveSQLException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.QueryWriter.LiveSQLPreparedQuery;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.select.TableExpression;
import org.hotrod.runtime.livesql.util.PreviewRenderer;

public abstract class MultiSet<T> {

  private static final Logger log = Logger.getLogger(MultiSet.class.getName());

  private CombinedSelectObject<T> parent;

  public void setParent(final CombinedSelectObject<T> parent) {
    this.parent = parent;
  }

  public CombinedSelectObject<T> getParent() {
    return this.parent;
  }

  public abstract void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

  // Rendering

  public abstract List<Expression> assembleColumnsOf(TableExpression te);

  public abstract Expression findColumnWithName(final String name);

  public abstract void renderTo(QueryWriter w, boolean inline);

  // Execution

  public abstract List<T> execute(final LiveSQLContext context);

  public abstract List<T> execute(final LiveSQLContext context, RowReader<T> rowReader);

  public abstract Cursor<T> executeCursor(final LiveSQLContext context) throws SQLException;

  public abstract Cursor<T> executeCursor(final LiveSQLContext context, RowReader<T> rowReader) throws SQLException;

  public abstract T executeOne(final LiveSQLContext context);

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
    List<Expression> columns = this.assembleColumnsOf(null);
    renderTo(w, false);
    return w.getPreparedQuery(columns);

  }

  public abstract void flatten();

  protected List<T> executeLiveSQL(final LiveSQLContext context, final LiveSQLPreparedQuery q,
      final boolean singleRow) {
    return this.executeLiveSQL(context, q, singleRow, null);
  }

  protected List<T> executeLiveSQL(final LiveSQLContext context, final LiveSQLPreparedQuery q, final boolean singleRow,
      RowReader<T> rowReader) {

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

          if (rowReader == null) {
            rowReader = new GenericRowReader<>(context, q, rs);
          }

          int count = 0;
          while (rs.next()) {
            count++;
            if (singleRow && count > 1) {
              throw new LiveSQLException("A single row at most was expected by this query but received at least two");
            }
            T row = rowReader.readRowFrom(rs, conn);
            rows.add(row);
          }
          return rows;
        }

      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  protected Cursor<T> executeLiveSQLCursor(final LiveSQLContext context, final LiveSQLPreparedQuery q)
      throws SQLException {
    return executeLiveSQLCursor(context, q, null);
  }

  protected Cursor<T> executeLiveSQLCursor(final LiveSQLContext context, final LiveSQLPreparedQuery q,
      final RowReader<T> rowReader) throws SQLException {
    return new RowCursor<>(context, q, rowReader);
  }

  protected T executeLiveSQLOne(final LiveSQLContext context, final LiveSQLPreparedQuery q) {
    List<T> rows = executeLiveSQL(context, q, true);
    if (rows.isEmpty()) {
      return null;
    } else {
      return rows.get(0);
    }
  }

}
