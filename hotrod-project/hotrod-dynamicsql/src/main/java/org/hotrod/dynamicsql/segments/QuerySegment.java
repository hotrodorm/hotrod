package org.hotrod.dynamicsql.segments;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.ParameterContext;

public abstract class QuerySegment {

  public abstract boolean prepare(StaticSegmentConsumer sc, ParameterContext context, int loopNestingLevel)
      throws DynamicExpressionException;

}
