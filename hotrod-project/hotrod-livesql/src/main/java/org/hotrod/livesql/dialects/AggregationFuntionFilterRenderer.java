package org.hotrod.livesql.dialects;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public abstract class AggregationFuntionFilterRenderer {

  public abstract void renderTo(QueryWriter w, String name, ComparableExpression expression, boolean distinct,
      Predicate filter);

  protected void renderPreSQL2003(QueryWriter w, String name, ComparableExpression expression, boolean distinct,
      Predicate filter) {
    w.write(name);
    w.write("(");
    if (distinct) {
      w.write("DISTINCT ");
    }
    if (filter != null) {
      w.write("CASE WHEN ");
      w.write(filter);
      w.write(" THEN ");
      w.write(expression);
      w.write(" END");
    } else {
      w.write(expression);
    }
    w.write(")");
  }

  protected void renderSQL2003(QueryWriter w, String name, ComparableExpression expression, boolean distinct,
      Predicate filter) {
    w.write(name);
    w.write("(");
    if (distinct) {
      w.write("DISTINCT ");
    }
    w.write(expression);
    w.write(")");
    if (filter != null) {
      w.write(" FILTER (WHERE ");
      w.write(filter);
      w.write(")");
    }
  }

}
