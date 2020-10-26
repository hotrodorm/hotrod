package org.hotrod.runtime.livesql.expressions.strings;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.rendering.FunctionTemplate;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public abstract class StringFunction extends StringExpression {

  private FunctionTemplate template;

  protected StringFunction(final String pattern, final Expression... parameters) {
    super(Expression.PRECEDENCE_FUNCTION);
    this.template = new FunctionTemplate(pattern, parameters);
    Arrays.asList(parameters).forEach(p -> super.register(p));
  }

  @Override
  public void renderTo(final QueryWriter w) {
    this.template.renderTo(w);
  }

}
