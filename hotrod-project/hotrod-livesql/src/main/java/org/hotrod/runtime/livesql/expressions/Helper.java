package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;

public class Helper {

  public static Expression getExpression(AliasedExpression ae) {
    return ae.getExpression();
  }

//  public static void computeQueryColumns(final Expression expr) {
//    expr.computeQueryColumns();
//  }

  public static TypeHandler getTypeHandler(final Expression expr) {
    return expr.getTypeHandler();
  }

  public static String getAlias(final Expression expr) {
    return expr.getAlias();
  }

  public static void validateTableReferences(final Expression expression, final TableReferences tableReferences,
      final AliasGenerator ag) {
    expression.validateTableReferences(tableReferences, ag);
  }

}
