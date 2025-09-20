package org.hotrod.dynamicsql;

import java.util.List;

import org.hotrod.dynamicsql.segments.QuerySegment;

public abstract class DynamicQuery {

  protected DynamicExpressionFactory factory;
  protected List<QuerySegment> segments;

  protected DynamicQuery(DynamicExpressionFactory factory, List<QuerySegment> segments) {
    this.factory = factory;
    this.segments = segments;
  }

}
