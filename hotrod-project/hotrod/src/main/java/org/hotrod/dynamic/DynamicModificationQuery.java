package org.hotrod.dynamic;

import java.util.List;

import org.hotrod.dynamic.segments.QuerySegment;

public class DynamicModificationQuery extends DynamicQuery {

  public DynamicModificationQuery(List<QuerySegment> parts) {
    super(parts);
  }
  
  public PreparedModificationQuery prepare(ParameterContext context) throws DynamicExpressionException {
    PreparedModificationQuery pq = new PreparedModificationQuery();
    for (QuerySegment p : this.parts) {
      p.prepare(pq, context);
    }
    return pq;
  }


}
