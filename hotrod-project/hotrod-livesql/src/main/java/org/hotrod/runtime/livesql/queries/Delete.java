package org.hotrod.runtime.livesql.queries;

import java.util.LinkedHashMap;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.select.AssembledQuery;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.QueryWriter.LiveSQLStructure;

public class Delete extends AssembledQuery {

  private String mapperStatement; // DAO.delete(t, predicate)

  private TableOrView from;
  private Predicate wherePredicate;

  Delete(final LiveSQLContext context) {
    super(context);
    this.mapperStatement = null;
  }

  Delete(final LiveSQLContext context, final String mapperStatement) {
    super(context);
    this.mapperStatement = mapperStatement;
  }

  void setFrom(final TableOrView from) {
    this.from = from;
  }

  void setWherePredicate(final Predicate predicate) {
    this.wherePredicate = predicate;
  }

  public String getPreview() {
    LiveSQLStructure pq = this.prepareQuery();
    return pq.render();
  }

  public void execute() {
    LiveSQLStructure q = this.prepareQuery();
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    if (this.mapperStatement != null) {
      this.context.getSQLSession().delete(this.mapperStatement, q.getConsolidatedParameters());
    } else {
      this.context.getLiveSQLMapper().delete(parameters);
    }
  }

  private LiveSQLStructure prepareQuery() {
    QueryWriter w = new QueryWriter(this.context.getLiveSQLDialect());
    w.write("DELETE FROM ");

    String renderedAlias = this.from.getAlias() == null ? null
        : this.context.getLiveSQLDialect()
            .canonicalToNatural(this.context.getLiveSQLDialect().naturalToCanonical(this.from.getAlias()));
    w.write(this.context.getLiveSQLDialect().canonicalToNatural(this.from)
        + (renderedAlias != null ? (" " + renderedAlias) : ""));
    if (this.wherePredicate != null) {
      w.write("\nWHERE ");
      this.wherePredicate.renderTo(w);
    }
    LiveSQLStructure pq = w.getPreparedQuery();
    return pq;
  }

}
