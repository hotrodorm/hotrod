package org.hotrod.runtime.livesql.expressions.predicates;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class BooleanConstant extends Predicate {

  // Properties

  private Boolean value;
  private boolean parameterize;

  // Constructor

  public BooleanConstant(final Boolean value) {
    super(Expression.PRECEDENCE_LITERAL);
    this.parameterize = true;
//    this.parameterize = false;
    this.value = value;
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    if (this.parameterize) {
      String name = w.registerParameter(this.value);
      w.write("#{" + name);
      w.write("}");
    } else {
      w.write("" + this.value);
    }
  }

}
