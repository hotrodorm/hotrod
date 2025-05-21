package org.hotrod.runtime.livesql;

import org.hotrod.dynamicsql.DynamicExpressionFactoryConfig;
import org.hotrod.dynamicsql.assembler.DynamicSQL;
import org.springframework.stereotype.Component;

@Component
public class DynamicSQLBean {

  private DynamicSQL dyn;

  public DynamicSQLBean() {
    this.dyn = new DynamicSQL(DynamicExpressionFactoryConfig.getFactory());
  }

  public DynamicSQL getDynamicSQL() {
    return dyn;
  }

}
