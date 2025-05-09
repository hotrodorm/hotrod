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
  private PartialQuery query;
  private List<WhenSegment> whens = new ArrayList<>();
  private OtherwiseSegment otherwise = null;

  public ChooseAssembler(DynamicExpressionFactory factory, PartialQuery query) {
    this.factory = factory;
    this.query = query;
  }

  // When Segments

  public ChooseAssembler when(String test, SegmentList segmentList) {
    this.whens.add(new WhenSegment(test, segmentList.getSegments(), this.factory));
    return this;
  }

  public PartialQuery otherwise(SegmentList segmentList) {
    this.query.segments.add(new ChooseSegment(this.whens, new OtherwiseSegment(segmentList, this.factory)));
    return this.query;
  }

  // end

  public PartialQuery end() {
    this.query.segments.add(new ChooseSegment(this.whens, this.otherwise));
    return this.query;
  }

}
