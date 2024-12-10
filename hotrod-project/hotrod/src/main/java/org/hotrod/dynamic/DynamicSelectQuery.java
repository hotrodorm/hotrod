package org.hotrod.dynamic;

import java.util.List;

import org.hotrod.dynamic.segments.QuerySegment;

public class DynamicSelectQuery extends DynamicQuery {

  public DynamicSelectQuery(List<QuerySegment> parts) {
    super(parts);
  }

  public <T> PreparedSelectQuery<T> prepare(ParameterContext context, Class<T> t) throws DynamicExpressionException {
    PreparedSelectQuery<T> pq = new PreparedSelectQuery<T>();
    for (QuerySegment p : this.parts) {
      p.prepare(pq, context);
    }
    return pq;
  }

}
