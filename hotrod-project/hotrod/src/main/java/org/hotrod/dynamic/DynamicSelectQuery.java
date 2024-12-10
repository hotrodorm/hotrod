package org.hotrod.dynamic;

import java.util.List;

import org.hotrod.dynamic.segments.QuerySegment;

public class DynamicSelectQuery extends DynamicQuery {

  public DynamicSelectQuery(List<QuerySegment> segments) {
    super(segments);
  }

  public <T> PreparedSelectQuery<T> prepare(ParameterContext context, Class<T> t) throws DynamicExpressionException {
    PreparedSelectQuery<T> pq = new PreparedSelectQuery<T>();
    for (QuerySegment s : this.segments) {
      s.prepare(pq, context);
    }
    return pq;
  }

}
