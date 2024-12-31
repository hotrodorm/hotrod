package org.hotrod.dynamicsql.jexl;

import org.apache.commons.jexl3.ObjectContext;
import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.ParameterContext;

public class JEXLDynamicExpressionFactory extends DynamicExpressionFactory {

  public DynamicExpression expression(String expression) {
    return JEXLDynamicExpression.of(expression);
  }

  public ParameterContext newParameterContext() {
    return new JEXLParameterContext();
  }

  @Override
  public ParameterContext newObjectContext(Object wrapped) {
    return JEXLObjectParameterContext.of(new ObjectContext<>(JEXLDynamicExpression.JEXL_ENGINE, wrapped));
  }

}
