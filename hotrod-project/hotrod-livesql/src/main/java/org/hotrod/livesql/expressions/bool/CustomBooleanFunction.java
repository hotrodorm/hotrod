package org.hotrod.livesql.expressions.bool;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.functions.LiveSQLFunctionTemplate;
import org.hotrod.livesql.functions.PreparedFunction;
import org.hotrod.livesql.queries.QueryWriter;

public abstract class CustomBooleanFunction extends BooleanFunction {

  private PreparedFunction preparedFunction;

  protected CustomBooleanFunction(final LiveSQLFunctionTemplate template, final ComparableExpression... arguments) {
    super(arguments);
    this.preparedFunction = template.prepare(arguments);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.preparedFunction.renderTo(w);
  }

}
