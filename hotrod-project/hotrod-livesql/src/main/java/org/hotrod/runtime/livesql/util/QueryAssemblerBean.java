package org.hotrod.runtime.livesql.util;

import org.hotrod.dynamicsql.DynamicExpressionFactoryConfig;
import org.hotrod.dynamicsql.assembler.QueryAssembler;
import org.springframework.stereotype.Component;

@Component
public class QueryAssemblerBean {

  private QueryAssembler assembler;

  public QueryAssemblerBean() {
    this.assembler = new QueryAssembler(DynamicExpressionFactoryConfig.getFactory());
  }

  public QueryAssembler getAssembler() {
    return assembler;
  }

}
