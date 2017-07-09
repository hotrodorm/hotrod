package org.hotrod.runtime.dynamicsql.expressions;

public class SetExpression extends TrimExpression {

  public SetExpression(final DynamicSQLExpression... expressions) {
    super(null, null, null, ",", expressions);
  }

}
