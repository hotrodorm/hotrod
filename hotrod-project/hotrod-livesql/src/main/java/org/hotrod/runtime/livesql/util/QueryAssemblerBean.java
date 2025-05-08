package org.hotrod.runtime.livesql.util;

import org.hotrod.dynamicsql.DynamicExpressionFactoryConfig;
import org.hotrod.dynamicsql.assembler.QueryBuilder;
import org.springframework.stereotype.Component;

@Component
public class QueryAssemblerBean {

  private QueryBuilder assembler;

  public QueryAssemblerBean() {
    this.assembler = new QueryBuilder(DynamicExpressionFactoryConfig.getFactory());
  }

  public QueryBuilder getAssembler() {
    return assembler;
  }

}
