package org.hotrod.dynamicsql;

public abstract class DynamicExpression {

  public abstract Object evaluate(Parameters context) throws DynamicExpressionException;

  public abstract <T> T evaluate(Parameters context, Class<T> targetClass) throws DynamicExpressionException;

}
