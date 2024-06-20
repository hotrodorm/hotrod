package org.hotrod.runtime.livesql.expressions.asymmetric;

import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.SHelper;
import org.hotrod.runtime.livesql.queries.select.Select;

public abstract class AsymmetricOperator extends Predicate {

  private ComparableExpression value;
  private String operator;
  private Select<?> subquery;

  protected AsymmetricOperator(final ComparableExpression value, final String operator, final Select<?> subquery) {
    super(Expression.PRECEDENCE_ANY_ALL_EQ_NE_LT_LE_GT_GE);
    this.value = value;
    this.operator = operator;
    this.subquery = subquery;
    super.register(this.value);
    super.register(this.subquery);
  }

  @Override
  public void renderTo(final QueryWriter w) {
    super.renderInner(this.value, w);
    w.write(" " + this.operator + " (\n");
    w.enterLevel();
    SHelper.getCombinedSelect(this.subquery).renderTo(w);
    w.exitLevel();
    w.write("\n)");
  }

}
