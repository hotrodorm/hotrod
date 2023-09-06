package org.hotrod.runtime.livesql.queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.QueryWriter.LiveSQLStructure;

public class UpdateObject extends QueryObject {

  private String mapperStatement; // DAO.update(values, [t,] predicate)

  private TableOrView tableOrView;
  private List<Assignment> sets = new ArrayList<>();
  private Predicate wherePredicate;

  private Map<String, Object> extraSets = new HashMap<>();

  UpdateObject() {
    super();
    this.mapperStatement = null;
  }

  UpdateObject(final String mapperStatement) {
    super();
    this.mapperStatement = mapperStatement;
  }

  void setTableOrView(final TableOrView from) {
    this.tableOrView = from;
  }

  void addSet(final Column c, final Expression e) {
    this.sets.add(new Assignment(c, e));
  }

  void setExtraSets(final Map<String, Object> extraSets) {
    this.extraSets = extraSets;
  }

  void setWherePredicate(final Predicate predicate) {
    this.wherePredicate = predicate;
  }

  public String getPreview(final LiveSQLContext context) {
    LiveSQLStructure pq = this.prepareQuery(context);
    return pq.render();
  }

  public void execute(final LiveSQLContext context) {
    LiveSQLStructure q = this.prepareQuery(context);
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    parameters.put("extraSets", this.extraSets);

    if (this.mapperStatement != null) {
      context.getSQLSession().update(this.mapperStatement, q.getConsolidatedParameters());
    } else {
      context.getLiveSQLMapper().update(parameters);
    }
  }

  private LiveSQLStructure prepareQuery(final LiveSQLContext context) {
    QueryWriter w = new QueryWriter(context.getLiveSQLDialect());
    w.write("UPDATE ");

    String renderedAlias = this.tableOrView.getAlias() == null ? null
        : context.getLiveSQLDialect()
            .canonicalToNatural(context.getLiveSQLDialect().naturalToCanonical(this.tableOrView.getAlias()));

    w.write(context.getLiveSQLDialect().canonicalToNatural(this.tableOrView)
        + (renderedAlias != null ? (" " + renderedAlias) : ""));

    w.write("\nSET\n");
    boolean first = true;
    for (int i = 0; i < this.sets.size(); i++) {
      w.write("    ");
      if (first) {
        first = false;
      } else {
        w.write(", ");
      }
      Assignment s = this.sets.get(i);
      s.getColumn().renderTo(w);
      w.write(" = ");
      s.getExpression().renderTo(w);
      w.write("\n");
    }
    for (String colName : this.extraSets.keySet()) {
      w.write("    ");
      if (first) {
        first = false;
      } else {
        w.write(", ");
      }
      String paramName = w.registerParameter(this.extraSets.get(colName));
      w.write(colName + " = #{" + paramName + "}");
      w.write("\n");
    }

    if (this.wherePredicate != null) {
      w.write("WHERE ");
      this.wherePredicate.renderTo(w);
    }
    LiveSQLStructure pq = w.getPreparedQuery();
    return pq;
  }

  private static class Assignment {
    private Column c;
    private Expression e;

    public Assignment(Column c, Expression e) {
      super();
      this.c = c;
      this.e = e;
    }

    public Column getColumn() {
      return c;
    }

    public Expression getExpression() {
      return e;
    }

  }

}
