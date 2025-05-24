package org.hotrod.livesql.expressions.numbers;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.rendering.FunctionTemplate;
import org.hotrod.livesql.queries.QueryWriter;

public abstract class NumberFunction extends NumberExpression {

  private FunctionTemplate template;

  protected NumberFunction(final String pattern, final ComparableExpression... parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.template = new FunctionTemplate(pattern, parameters);
    Arrays.asList(parameters).forEach(p -> super.register(p));
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.template.renderTo(w);
  }

}
