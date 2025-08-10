package org.hotrod.livesql.expressions.asymmetric;

import org.hotrod.livesql.expressions.EquatableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.Select;

public abstract class AsymmetricOperator extends BooleanSyntaxExpression {

  private EquatableExpression value;
  private String operator;
  private Select<?> subquery;

  protected AsymmetricOperator(final EquatableExpression value, final String operator, final Select<?> subquery) {
    super(Expression.PRECEDENCE_ANY_ALL_EQ_NE_LT_LE_GT_GE);
    this.value = value;
    this.operator = operator;
    this.subquery = subquery;
    super.register(this.value);
    super.register(this.subquery);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    super.renderInner(this.value, w);
    w.write(" " + this.operator + " (\n");
    w.enterLevel();
    SShield.getCombinedSelect(this.subquery).renderTo(w);
    w.exitLevel();
    w.write("\n)");
  }

}
