package org.hotrod.livesql.expressions;

import java.util.List;

import org.hotrod.livesql.expressions.analytics.WindowableFunction;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.TableReferences;
import org.hotrod.livesql.queries.select.UnarySelectObject.AliasGenerator;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public class Helper {

  @SuppressWarnings("unchecked")
  public static <R, D> TypeHandler<R, D> getTypeHandler(final Expression expr) {
    return expr.getTypeHandler();
  }

  public static <R, D> void setTypeHandler(final Expression expr, final TypeHandler<R, D> typeHandler) {
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

  public static Expression getExpressionOn(final ResultSetColumn rsc) {
    return rsc.getEmergingExpression();
  }

  public static Expression asSubqueryExpression(final Expression e, final Subquery subquery, final String alias) {
    return e.asSubqueryExpression(subquery, alias);
  }

  public static List<Expression> expand(final ResultSetColumn rsc) {
    return rsc.expand();
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
