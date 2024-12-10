package org.hotrod.dynamic.builder;

import org.hotrod.dynamic.DynamicExpressionFactory;

public class QueryBuilder {

  private DynamicExpressionFactory factory;

  public QueryBuilder(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  public PartialQuery create() {
    return new PartialQuery(this.factory);
  }

  public IfsBuilder ifsSegments() {
    return new IfsBuilder(this.factory);
  }


}
