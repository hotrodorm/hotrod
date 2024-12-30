package org.hotrod.dynamicsql;

public abstract class DynamicExpression {

  public abstract Object evaluate(ParameterContext context) throws DynamicExpressionException;

  public abstract <T> T evaluate(ParameterContext context, Class<T> targetClass) throws DynamicExpressionException;

}
