package org.hotrod.dynamic;

import org.hotrod.dynamic.jexl.JEXLDynamicExpressionFactory;

public abstract class DynamicExpressionFactory {

  private static final JEXLDynamicExpressionFactory JEXL_FACTORY = new JEXLDynamicExpressionFactory();

  // Factory methods

  public static DynamicExpressionFactory getFactory() {
    return JEXL_FACTORY;
  }

  public abstract DynamicExpression expression(String expression);

  public abstract ParameterContext newParameterContext();

}
