package org.hotrod.dynamicsql.segments;

import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;

public class OtherwiseSegment extends ControlSegment {

  private List<QuerySegment> segments;
  @SuppressWarnings("unused")
  private DynamicExpressionFactory factory;

  public OtherwiseSegment(List<QuerySegment> segments, DynamicExpressionFactory factory) {
    this.segments = segments;
    this.factory = factory;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
      throws DynamicExpressionException {
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, loopNestingLevel);
    }
    return true;
  }

}
