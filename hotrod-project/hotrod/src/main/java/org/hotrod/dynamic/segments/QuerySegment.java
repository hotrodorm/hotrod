package org.hotrod.dynamic.segments;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.dynamic.PreparedQuery;

public abstract class QuerySegment {

  public abstract void prepare(PreparedQuery pq, ParameterContext context) throws DynamicExpressionException;

}
