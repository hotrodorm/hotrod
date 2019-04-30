package org.hotrod.runtime.sql.expressions.numbers;

import org.hotrod.runtime.sql.QueryWriter;

public class Signum extends CustomNumericFunction {

  public Signum(final NumberExpression x) {
    super(x);
  }

  @Override
  protected String getName(final QueryWriter w) {
    return w.getSqlDialect().getFunctionTranslator().getSignum();
  }

}
