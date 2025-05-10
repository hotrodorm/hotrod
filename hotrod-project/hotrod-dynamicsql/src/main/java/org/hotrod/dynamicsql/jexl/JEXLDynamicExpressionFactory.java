package org.hotrod.dynamicsql.jexl;

import org.apache.commons.jexl3.ObjectContext;
import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;

public class JEXLDynamicExpressionFactory extends DynamicExpressionFactory {

  public DynamicExpression expression(String expression) {
    return JEXLDynamicExpression.of(expression);
  }

  public Parameters newParameterContext() {
    return new JEXLParameterContext();
  }

  @Override
  public Parameters newObjectContext(Object wrapped) {
    return JEXLObjectParameterContext.of(new ObjectContext<>(JEXLDynamicExpression.JEXL_ENGINE, wrapped));
  }

}
