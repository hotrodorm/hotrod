package org.hotrod.dynamicsql.jexl;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlException;
import org.apache.commons.jexl3.JexlExpression;
import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.InvalidJexlExpressionException;
import org.hotrod.dynamicsql.Parameters;

public class JEXLDynamicExpression extends DynamicExpression {

  private String txt;
  private JexlExpression expr;

  private JEXLDynamicExpression(JexlEngine JexlEngine, String expression) {
    this.txt = expression;
    try {
      this.expr = JexlEngine.createExpression(this.txt);
    } catch (JexlException e) {
      throw new InvalidJexlExpressionException("Invalid test expression: " + this.txt + " (" + e.getMessage() + ")");
    }

  }

  public static JEXLDynamicExpression of(JexlEngine JexlEngine, String expression) {
    return new JEXLDynamicExpression(JexlEngine, expression);
  }

  @Override
  public <T> T evaluate(Parameters context, Class<T> targetClass) throws DynamicExpressionException {
    Object obj = evaluate(context);
    if (obj == null) {
      return null;
    }
    try {
      return targetClass.cast(obj);
    } catch (ClassCastException e) {
      throw new DynamicExpressionException(
          "Invalid result of expression '" + this.txt + "': expected a result of type '" + targetClass.getName()
              + "' but encountered '" + obj.getClass().getName() + "'.");
    }
  }

  @Override
  public Object evaluate(Parameters context) throws DynamicExpressionException {
    JexlContext jexlContext = (JexlContext) context;
    try {
      return this.expr.evaluate(jexlContext);
    } catch (Exception e) {
      throw new DynamicExpressionException("Could not evaluate the expression '" + this.txt + "'", e);
    }
  }


}
