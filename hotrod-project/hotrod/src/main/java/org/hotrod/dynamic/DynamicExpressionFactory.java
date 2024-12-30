package org.hotrod.dynamic;

public abstract class DynamicExpressionFactory {

  // Factory methods

  public abstract DynamicExpression expression(String expression);

  public abstract ParameterContext newParameterContext();

}
