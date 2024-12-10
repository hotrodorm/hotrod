package org.hotrod.dynamic.builder;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.segments.IfSegment;
import org.hotrod.dynamic.segments.SegmentList;

public class IfsBuilder {

  private DynamicExpressionFactory factory;
  private List<IfSegment> ifSegments = new ArrayList<>();

  public IfsBuilder(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  // IF Segments

  public IfsBuilder ifSegment(String test, SegmentList querySegments) {
    this.ifSegments.add(new IfSegment(test, querySegments.getSegments(), this.factory));
    return this;
  }

  // end

  public List<IfSegment> end() {
    return this.ifSegments;
  }

}
