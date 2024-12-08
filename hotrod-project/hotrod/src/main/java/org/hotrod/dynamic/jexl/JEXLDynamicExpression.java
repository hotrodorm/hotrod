package org.hotrod.dynamic.jexl;

import org.hotrod.dynamic.DynamicExpression;
import org.hotrod.dynamic.ParameterContext;

public class JEXLDynamicExpression extends DynamicExpression {

  private String expression;

  private JEXLDynamicExpression(String expression) {
    this.expression = expression;
  }

  public static JEXLDynamicExpression of(String expression) {
    return new JEXLDynamicExpression(expression);
  }

  @Override
  public <T> T evaluate(ParameterContext context, Class<T> targetClass) {
    return null;
  }

}
