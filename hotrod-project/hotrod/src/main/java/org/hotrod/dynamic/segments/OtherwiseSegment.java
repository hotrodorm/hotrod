package org.hotrod.dynamic.segments;

import java.util.List;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;

public class OtherwiseSegment extends DynamicSegment {

  private List<QuerySegment> segments;

  public OtherwiseSegment(SegmentList segmentList, DynamicExpressionFactory factory) {
    this.segments = segmentList.getSegments();
  }

  public OtherwiseSegment(List<QuerySegment> segments, DynamicExpressionFactory factory) {
    this.segments = segments;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context) throws DynamicExpressionException {
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context);
    }
    return true;
  }

}
