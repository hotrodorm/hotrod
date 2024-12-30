package org.hotrod.dynamicsql;

import org.hotrod.dynamicsql.jexl.JEXLDynamicExpressionFactory;

public class DynamicExpressionFactoryConfig {

  private static final JEXLDynamicExpressionFactory JEXL_FACTORY = new JEXLDynamicExpressionFactory();

  public static DynamicExpressionFactory getFactory() {
    return JEXL_FACTORY;
  }

}
