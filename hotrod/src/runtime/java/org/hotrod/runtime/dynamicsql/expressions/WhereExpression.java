package org.hotrod.runtime.dynamicsql.expressions;

public class WhereExpression extends TrimExpression {

  public WhereExpression(final DynamicSQLExpression... expressions) {
    super("where ", "and |or ", null, null, expressions);
  }

}
