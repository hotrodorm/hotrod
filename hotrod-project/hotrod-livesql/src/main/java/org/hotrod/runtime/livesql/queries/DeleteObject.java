package org.hotrod.runtime.livesql.queries;

import java.util.LinkedHashMap;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.QueryWriter.LiveSQLPreparedQuery;

public class DeleteObject implements QueryObject {

  private String mapperStatement; // DAO.delete(t, predicate)

  private TableOrView from;
  private Predicate wherePredicate;

  DeleteObject() {
    super();
    this.mapperStatement = null;
  }

  DeleteObject(final String mapperStatement) {
    super();
    this.mapperStatement = mapperStatement;
  }

  void setFrom(final TableOrView from) {
    this.from = from;
  }

  void setWherePredicate(final Predicate predicate) {
    this.wherePredicate = predicate;
  }

  public String getPreview(final LiveSQLContext context) {
    LiveSQLPreparedQuery pq = this.prepareQuery(context);
    return pq.render();
  }

  public int execute(final LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    if (this.mapperStatement != null) {
      return context.getSQLSession().delete(this.mapperStatement, q.getConsolidatedParameters());
    } else {
      return context.getLiveSQLMapper().delete(parameters);
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
      this.wherePredicate.renderTo(w);
    }
    LiveSQLPreparedQuery pq = w.getPreparedQuery(null);
    return pq;
  }

}
