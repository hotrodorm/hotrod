package org.hotrod.dynamic;

public abstract class DynamicExpression {

  public abstract <T> T evaluate(ParameterContext context, Class<T> targetClass);

}
