package org.hotrod.dynamicsql;

public abstract class DynamicExpressionFactory {

  // Factory methods

  public abstract DynamicExpression expression(String expression);

  // An empty context where properties can be added, removed, and updated
  public abstract Parameters newParameterContext();

  // A read-only context to access an existing object's properties
  public abstract Parameters newObjectContext(Object wrapped);

}
