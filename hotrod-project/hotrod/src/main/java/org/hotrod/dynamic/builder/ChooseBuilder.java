package org.hotrod.dynamic.builder;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.segments.ChooseSegment;
import org.hotrod.dynamic.segments.OtherwiseSegment;
import org.hotrod.dynamic.segments.SegmentList;
import org.hotrod.dynamic.segments.WhenSegment;

public class ChooseBuilder {

  private DynamicExpressionFactory factory;
  private List<WhenSegment> whens = new ArrayList<>();
  private OtherwiseSegment otherwise = null;

  public ChooseBuilder(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  // IF Segments

  public ChooseBuilder when(String test, SegmentList segmentList) {
    this.whens.add(new WhenSegment(test, segmentList.getSegments(), this.factory));
    return this;
  }

  public ChooseSegment otherwise(SegmentList segmentList) {
    return new ChooseSegment(this.whens, new OtherwiseSegment(segmentList, this.factory));
  }

  // end

  public ChooseSegment end() {
    return new ChooseSegment(this.whens, this.otherwise);
  }

}
