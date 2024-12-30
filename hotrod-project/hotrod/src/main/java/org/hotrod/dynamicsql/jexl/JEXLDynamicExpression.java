package org.hotrod.dynamicsql.jexl;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlException;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.introspection.JexlPermissions;
import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.ParameterContext;
import org.hotrod.exceptions.InvalidJexlExpressionException;

public class JEXLDynamicExpression extends DynamicExpression {

  private static final int JEXL_CACHE_MAX_EXPRESSIONS = 200;

  public static JexlEngine JEXL_ENGINE = new JexlBuilder().cache(JEXL_CACHE_MAX_EXPRESSIONS)
      .permissions(JexlPermissions.UNRESTRICTED).strict(true).debug(true).silent(false).create();

  private String txt;
  private JexlExpression expr;

  private JEXLDynamicExpression(String expression) {
    this.txt = expression;
    try {
      this.expr = JEXL_ENGINE.createExpression(this.txt);
    } catch (JexlException e) {
      throw new InvalidJexlExpressionException("Invalid test expression: " + this.txt + " (" + e.getMessage() + ")");
    }

  }

  public static JEXLDynamicExpression of(String expression) {
    return new JEXLDynamicExpression(expression);
  }

  @Override
  public <T> T evaluate(ParameterContext context, Class<T> targetClass) throws DynamicExpressionException {
    Object obj = evaluate(context);
    if (obj == null) {
      return null;
    }
    try {
      T t = targetClass.cast(obj);
      return t;
    } catch (ClassCastException e) {
      throw new DynamicExpressionException(
          "Invalid result of expression '" + this.txt + "': expected a result of type '" + targetClass.getName()
              + "' but encountered '" + obj.getClass().getName() + "'.");
    }
  }

  @Override
  public Object evaluate(ParameterContext context) throws DynamicExpressionException {
    JEXLParameterContext jexlContext = (JEXLParameterContext) context;
    try {
      return this.expr.evaluate(jexlContext);
    } catch (Exception e) {
      throw new DynamicExpressionException("Could not evaluate the expression '" + this.txt + "'", e);
    }
  }

}
