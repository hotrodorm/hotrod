package org.hotrod.runtime.livesql.util;

import org.hotrod.dynamicsql.assembler.DynamicSQL;
import org.springframework.stereotype.Component;

@Component
public class DynamicSQLBean {

  private DynamicSQL dyn;

  public DynamicSQLBean() {
    this.dyn = new DynamicSQL();
  }

  public DynamicSQL getDynamicSQL() {
    return dyn;
  }

}
