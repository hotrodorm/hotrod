package org.hotrod.runtime.livesql.expressions;

public class Helper {

  public static Expression getExpression(AliasedExpression ae) {
    return ae.getExpression();
  }

  public static void computeQueryColumns(final Expression expr) {
    expr.computeQueryColumns();
  }

  public static TypeHandler getTypeHandler(final Expression expr) {
    return expr.getTypeHandler();
  }

}
