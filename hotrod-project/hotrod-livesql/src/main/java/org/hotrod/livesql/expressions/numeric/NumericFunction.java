package org.hotrod.livesql.expressions.numeric;

import java.util.Arrays;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.rendering.FunctionTemplate;
import org.hotrod.livesql.queries.QueryWriter;

public abstract class NumericFunction extends NumericSyntaxExpression {

  private FunctionTemplate template;

  protected NumericFunction(final ComparableExpression... parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.template = null;
    Arrays.asList(parameters).forEach(p -> super.register(p));
  }

  protected NumericFunction(final String pattern, final ComparableExpression... parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.template = new FunctionTemplate(pattern, parameters);
    Arrays.asList(parameters).forEach(p -> super.register(p));
  }

  protected NumericFunction(final NumericFunction preparedAndRegistered) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.template = preparedAndRegistered.template;
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.template.renderTo(w);
  }

}
