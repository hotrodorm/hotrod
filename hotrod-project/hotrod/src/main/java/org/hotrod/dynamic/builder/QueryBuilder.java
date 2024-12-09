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

  public WhereBuilder ifSegments() {
    return new WhereBuilder(this.factory);
  }

}
