package org.hotrod.dynamicsql;

import org.hotrod.dynamicsql.jexl.JEXLDynamicExpressionFactory;

public class DynamicExpressionFactoryConfig {

  public static DynamicExpressionFactory getFactory(String[] allowedPackages) {
    return new JEXLDynamicExpressionFactory(allowedPackages);
  }

}
