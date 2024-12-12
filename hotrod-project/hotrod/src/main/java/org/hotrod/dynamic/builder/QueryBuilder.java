package org.hotrod.dynamic.builder;

import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.segments.OtherwiseSegment;
import org.hotrod.dynamic.segments.SegmentList;
import org.hotrod.dynamic.segments.WhenSegment;

public class QueryBuilder {

  private DynamicExpressionFactory factory;

  public QueryBuilder(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  public PartialQuery create() {
    return new PartialQuery(this.factory);
  }

  public IfsBuilder ifs() {
    return new IfsBuilder(this.factory);
  }

  public ChooseBuilder choose() {
    return new ChooseBuilder(this.factory);
  }

  public WhenSegment when(String test, SegmentList segmentList) {
    return new WhenSegment(test, segmentList.getSegments(), this.factory);
  }

  public OtherwiseSegment otherwise(SegmentList segmentList) {
    return new OtherwiseSegment(segmentList.getSegments(), this.factory);
  }

}
