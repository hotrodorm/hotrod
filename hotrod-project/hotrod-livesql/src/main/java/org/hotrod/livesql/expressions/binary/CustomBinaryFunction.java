package org.hotrod.livesql.expressions.binary;

import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.functions.LiveSQLFunctionTemplate;
import org.hotrod.livesql.functions.PreparedFunction;
import org.hotrod.livesql.queries.QueryWriter;

public abstract class CustomBinaryFunction extends BinaryFunction {

  private PreparedFunction preparedFunction;

  protected CustomBinaryFunction(final LiveSQLFunctionTemplate template, final ComparableExpression... arguments) {
    super(arguments);
    this.preparedFunction = template.prepare(arguments);
  }

  @Override
  protected void renderTo(final QueryWriter w) {
    this.preparedFunction.renderTo(w);
  }

}
