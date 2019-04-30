package org.hotrod.runtime.sql.expressions.numbers;

import org.hotrod.runtime.sql.QueryWriter;

public class Round extends CustomNumericFunction {

  public Round(final NumberExpression x, final NumberExpression places) {
    super(x, places);
  }

  @Override
  protected String getName(final QueryWriter w) {
    return w.getSqlDialect().getFunctionTranslator().getRound();
  }

}
