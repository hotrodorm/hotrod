package org.hotrod.dynamic;

import java.util.List;

import org.hotrod.dynamic.segments.QuerySegment;

public abstract class DynamicQuery {

  private List<QuerySegment> parts;

  public DynamicQuery(List<QuerySegment> parts) {
    this.parts = parts;
  }

  public PreparedQuery prepare(ParameterContext context) throws DynamicExpressionException {
    PreparedQuery pq = new PreparedQuery();
    for (QuerySegment p : this.parts) {
      p.prepare(pq, context); // the context is used to process dynamic SQL
    }
    return pq;
  }

}
