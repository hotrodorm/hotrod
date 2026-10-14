package org.hotrod.livesql.expressions.character;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.rendering.FunctionTemplate;
import org.hotrod.livesql.queries.QueryWriter;

public abstract class CustomCharFunction extends CharFunction {

  private FunctionTemplate template;

  protected CustomCharFunction(final String pattern, final ComparableExpression... parameters) {
    super(parameters);
    this.template = new FunctionTemplate(pattern, parameters);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.template.renderTo(w);
  }

}
