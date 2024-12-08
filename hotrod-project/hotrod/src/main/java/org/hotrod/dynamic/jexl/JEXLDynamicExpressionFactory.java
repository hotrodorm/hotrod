package org.hotrod.dynamic.jexl;

import org.hotrod.dynamic.DynamicExpression;
import org.hotrod.dynamic.DynamicExpressionFactory;

public class JEXLDynamicExpressionFactory extends DynamicExpressionFactory {

  public DynamicExpression create(String expression) {
    return JEXLDynamicExpression.of(expression);
  }

}
