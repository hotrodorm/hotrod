package org.hotrod.dynamic;

import java.util.List;

import org.hotrod.dynamic.segments.QuerySegment;

public class DynamicModificationQuery extends DynamicQuery {

  public DynamicModificationQuery(List<QuerySegment> segments) {
    super(segments);
  }

  public PreparedModificationQuery prepare(ParameterContext context) throws DynamicExpressionException {
    PreparedModificationQuery pq = new PreparedModificationQuery();
    for (QuerySegment s : this.segments) {
      s.prepare(pq, context, 0);
    }
    return pq;
  }

}
