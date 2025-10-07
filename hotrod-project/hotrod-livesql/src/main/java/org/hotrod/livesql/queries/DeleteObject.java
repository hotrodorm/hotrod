package org.hotrod.livesql.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class DeleteObject {

  private TableOrView<?> from;
  private Predicate wherePredicate;

  DeleteObject() {
    super();
  }

  void setFrom(final TableOrView<?> from) {
    this.from = from;
  }

  void setWherePredicate(final Predicate predicate) {
    this.wherePredicate = predicate;
  }

  public String getPreview(final LiveSQLContext context, boolean includeParameters) {
    LiveSQLPreparedQuery pq = this.prepareQuery(context);
    return pq.getPreview(includeParameters);
  }

  public int execute(final LiveSQLContext context) {
    return execute(context, LiveSQLLogging.NO_LOGGING);
  }

  public int execute(final LiveSQLContext context, final LiveSQLLogging loggingAdapter) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);

    if (loggingAdapter != null && loggingAdapter.enabled()) {
      loggingAdapter.log(q.getPreview(true));
    }

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
      Shield.renderTo(this.wherePredicate, w);
    }
    LiveSQLPreparedQuery pq = w.getPreparedQuery(null, false);
    return pq;
  }

}
