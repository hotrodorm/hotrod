package org.hotrod.livesql.expressions.binary;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.rendering.FunctionTemplate;
import org.hotrod.livesql.queries.QueryWriter;

public abstract class CustomBinaryFunction extends BinaryFunction {

  private FunctionTemplate template;

  protected CustomBinaryFunction(final String pattern, final ComparableExpression... parameters) {
    super(parameters);
    this.template = new FunctionTemplate(pattern, parameters);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.template.renderTo(w);
  }

}
