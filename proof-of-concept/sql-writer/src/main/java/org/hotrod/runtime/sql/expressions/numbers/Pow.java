package org.hotrod.runtime.sql.expressions.numbers;

import org.hotrod.runtime.sql.QueryWriter;

public class Pow extends CustomNumericFunction {

  public Pow(final NumberExpression x, final NumberExpression exponent) {
    super(x, exponent);
  }

  @Override
  protected String getName(final QueryWriter w) {
    return w.getSqlDialect().getFunctionTranslator().getPow();
  }

}
