package org.hotrod.runtime.livesql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.QueryWriter.LiveSQLPreparedQuery;
import org.hotrod.runtime.livesql.util.PreviewRenderer;

public class DeleteObject implements QueryObject {

  private TableOrView from;
  private GeneralBooleanExpression wherePredicate;

  DeleteObject() {
    super();
  }

  DeleteObject(final String mapperStatement) {
    super();
  }

  void setFrom(final TableOrView from) {
    this.from = from;
  }

  void setWherePredicate(final GeneralBooleanExpression predicate) {
    this.wherePredicate = predicate;
  }

  public String getPreview(final LiveSQLContext context) {
    LiveSQLPreparedQuery pq = this.prepareQuery(context);
    return PreviewRenderer.render(pq);
  }

  public int execute(final LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    try (Connection conn = context.getDataSource().getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(q.getSQL())) {

        // 1. Apply parameters

        int n = 1;
        for (Object obj : q.getParameters().values()) {
          int i = n++;
          ps.setObject(i, obj);
        }

        // 2. Run the query

        int count = ps.executeUpdate();
        return count;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private LiveSQLPreparedQuery prepareQuery(final LiveSQLContext context) {
    QueryWriter w = new QueryWriter(context);
    w.write("DELETE FROM ");

    String renderedAlias = this.from.getAlias() == null ? null
        : context.getLiveSQLDialect()
            .canonicalToNatural(context.getLiveSQLDialect().naturalToCanonical(this.from.getAlias()));
    w.write(context.getLiveSQLDialect().canonicalToNatural(this.from)
        + (renderedAlias != null ? (" " + renderedAlias) : ""));
    if (this.wherePredicate != null) {
      w.write("\nWHERE ");
      Helper.renderTo(this.wherePredicate, w);
    }
    LiveSQLPreparedQuery pq = w.getPreparedQuery(null);
    return pq;
  }

}
