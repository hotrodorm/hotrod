package org.hotrod.runtime.livesql.queries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.dialects.UpdateRenderer;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.metadata.MDHelper;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.queries.QueryWriter.LiveSQLPreparedQuery;
import org.hotrod.runtime.livesql.queries.SQLParameterWriter.RenderedParameter;

public class UpdateObject implements QueryObject {

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
    LiveSQLPreparedQuery pq = this.prepareQuery(context);
    return pq.render();
  }

  public int execute(final LiveSQLContext context) {
    LiveSQLPreparedQuery q = this.prepareQuery(context);
    LinkedHashMap<String, Object> parameters = q.getParameters();
    parameters.put("sql", q.getSQL());
    parameters.put("extraSets", this.extraSets);

    if (this.mapperStatement != null) {
      return context.getSQLSession().update(this.mapperStatement, q.getConsolidatedParameters());
    } else {
      return context.getLiveSQLMapper().update(parameters);
    }
  }

  private LiveSQLPreparedQuery prepareQuery(final LiveSQLContext context) {
    QueryWriter w = new QueryWriter(context);
    w.write("UPDATE ");

    UpdateRenderer ur = context.getLiveSQLDialect().getUpdateRenderer();
    if (ur.removeMainTableAlias()) {
      MDHelper.removeAlias(this.tableOrView);
    }

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
      w.write(w.getSQLDialect().canonicalToNatural(s.getColumn().getName()));

      w.write(" = ");
      Helper.renderTo(s.getExpression(), w);
      w.write("\n");
    }
    for (String colName : this.extraSets.keySet()) {
      w.write("    ");
      if (first) {
        first = false;
      } else {
        w.write(", ");
      }
      RenderedParameter p = w.registerParameter(this.extraSets.get(colName));
      w.write(colName + " = " + p.getPlaceholder());
      w.write("\n");
    }

    if (this.wherePredicate != null) {
      w.write("WHERE ");
      Helper.renderTo(this.wherePredicate, w);
    }
    LiveSQLPreparedQuery pq = w.getPreparedQuery(null);
    return pq;
  }

  private static class Assignment {
    private Column c;
    private Expression e;

    public Assignment(final Column c, final Expression e) {
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
