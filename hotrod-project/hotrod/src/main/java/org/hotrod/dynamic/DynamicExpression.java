package org.hotrod.dynamic;

public abstract class DynamicExpression {

  public abstract Object evaluate(ParameterContext context) throws DynamicExpressionException;

  public abstract <T> T evaluate(ParameterContext context, Class<T> targetClass) throws DynamicExpressionException;

}
