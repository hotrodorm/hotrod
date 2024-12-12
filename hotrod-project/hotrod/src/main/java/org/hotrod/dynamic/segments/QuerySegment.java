package org.hotrod.dynamic.segments;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.ParameterContext;

public abstract class QuerySegment {

  public abstract boolean prepare(StaticSegmentConsumer sc, ParameterContext context) throws DynamicExpressionException;

}
