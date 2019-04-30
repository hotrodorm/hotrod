package org.hotrod.runtime.sql.expressions.numbers;

import org.hotrod.runtime.sql.QueryWriter;

public class Log extends CustomNumericFunction {

  public Log(final NumberExpression base, final NumberExpression x) {
    super(base, x);
  }

  @Override
  protected String getName(final QueryWriter w) {
    return w.getSqlDialect().getFunctionTranslator().getLog();
  }

}
