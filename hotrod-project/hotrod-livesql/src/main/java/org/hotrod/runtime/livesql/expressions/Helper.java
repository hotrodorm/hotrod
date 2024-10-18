package org.hotrod.runtime.livesql.expressions;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.analytics.WindowableFunction;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;
import org.hotrod.runtime.livesql.queries.typesolver.TypeHandler;

public class Helper {

  public static TypeHandler getTypeHandler(final Expression expr) {
    return expr.getTypeHandler();
  }

  public static void setTypeHandler(final Expression expr, final TypeHandler typeHandler) {
    expr.setTypeHandler(typeHandler);
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

  public static Expression getExpression(final ResultSetColumn wc) {
    return wc.getExpression();
  }

  public static List<Expression> unwrap(final ResultSetColumn wc) {
    return wc.unwrap();
  }

  public static String getReferenceName(final Expression expr) {
    return expr.getReferenceName();
  }

  public static String getProperty(final Expression expr) {
    return expr.getProperty();
  }

//  public static void captureTypeHandler(final Expression expr) {
//    expr.captureTypeHandler();
//  }

}
