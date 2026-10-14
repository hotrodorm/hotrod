package org.hotrod.livesql.expressions.datetime;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.rendering.FunctionTemplate;
import org.hotrod.livesql.queries.QueryWriter;

public abstract class CustomDateTimeFunction extends DateTimeFunction {

  private FunctionTemplate template;

  protected CustomDateTimeFunction(final String pattern, final ComparableExpression... parameters) {
    super(parameters);
    this.template = new FunctionTemplate(pattern, parameters);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.template.renderTo(w);
  }

}
