package org.hotrod.dynamicsql.segments;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;

public class OtherwiseSegment extends ControlSegment {

  private static final Logger log = Logger.getLogger(OtherwiseSegment.class.getName());

  private List<QuerySegment> segments;
  @SuppressWarnings("unused")
  private DynamicExpressionFactory factory;

  public OtherwiseSegment(List<QuerySegment> segments, DynamicExpressionFactory factory) {
    log.finer("init");
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
