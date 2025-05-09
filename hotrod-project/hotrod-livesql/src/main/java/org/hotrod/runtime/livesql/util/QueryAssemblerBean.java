package org.hotrod.runtime.livesql.util;

import org.hotrod.dynamicsql.DynamicExpressionFactoryConfig;
import org.hotrod.dynamicsql.assembler.DynamicSQL;
import org.springframework.stereotype.Component;

@Component
public class QueryAssemblerBean {

  private DynamicSQL assembler;

  public QueryAssemblerBean() {
    this.assembler = new DynamicSQL(DynamicExpressionFactoryConfig.getFactory());
  }

  public DynamicSQL getAssembler() {
    return assembler;
  }

}
