package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public class BooleanConstant extends Predicate {

  // Properties

  private Boolean value;

  // Constructor

  public BooleanConstant(final Boolean value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.value = value;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    w.write("" + this.value);
  }

}
