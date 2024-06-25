package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.expressions.analytics.WindowableFunction;
import org.hotrod.runtime.livesql.queries.QueryWriter;
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

  public static void validateTableReferences(final Expression expr, final TableReferences tableReferences,
      final AliasGenerator ag) {
    expr.validateTableReferences(tableReferences, ag);
  }

  public static void renderTo(final Expression expr, final QueryWriter w) {
    expr.renderTo(w);
  }

  public static void renderTo(final WindowableFunction function, final QueryWriter w) {
    try {
      Expression expr = (Expression) function;
      expr.renderTo(w);
    } catch (ClassCastException e) {
      throw new RuntimeException("Could not render function " + function.getClass().getName());
    }
  }

}
