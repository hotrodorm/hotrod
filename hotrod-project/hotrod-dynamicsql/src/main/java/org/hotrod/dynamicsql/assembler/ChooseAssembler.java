package org.hotrod.dynamicsql.assembler;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.ChooseSegment;
import org.hotrod.dynamicsql.segments.OtherwiseSegment;
import org.hotrod.dynamicsql.segments.SegmentList;
import org.hotrod.dynamicsql.segments.WhenSegment;

public class ChooseAssembler {

  private DynamicExpressionFactory factory;
  private List<WhenSegment> whens = new ArrayList<>();
  private OtherwiseSegment otherwise = null;

  public ChooseAssembler(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  // When Segments

  public ChooseAssembler when(String test, SegmentList segmentList) {
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
