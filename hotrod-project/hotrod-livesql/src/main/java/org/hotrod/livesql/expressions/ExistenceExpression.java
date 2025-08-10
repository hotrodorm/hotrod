package org.hotrod.livesql.expressions;

import org.hotrod.livesql.expressions.bool.BooleanSyntaxExpression;
import org.hotrod.livesql.expressions.bool.IsNotNull;
import org.hotrod.livesql.expressions.bool.IsNull;
import org.hotrod.livesql.ordering.OrderingTerm;

public abstract class ExistenceExpression extends UnaliasedExpression implements OrderingTerm {

  protected ExistenceExpression(int precedence) {
    super(precedence);
  }

  // Is Null and Is Not Null

  public BooleanSyntaxExpression isNotNull() {
    return new IsNotNull(this);
  }

  public BooleanSyntaxExpression isNull() {
    return new IsNull(this);
  }

}
