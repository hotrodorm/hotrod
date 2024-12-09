package org.hotrod.dynamic.jexl;

import org.hotrod.dynamic.DynamicExpression;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;

public class JEXLDynamicExpressionFactory extends DynamicExpressionFactory {

  public DynamicExpression expression(String expression) {
    return JEXLDynamicExpression.of(expression);
  }

  public ParameterContext newParameterContext() {
    return new JEXLParameterContext();
  }

}
