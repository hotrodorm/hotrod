package org.hotrod.dynamic;

import java.util.List;

import org.hotrod.dynamic.segments.QuerySegment;

public class DynamicInsertQuery extends DynamicQuery {

  public DynamicInsertQuery(List<QuerySegment> segments) {
    super(segments);
  }

  public <T> PreparedInsertQuery prepare(ParameterContext context) throws DynamicExpressionException {
    PreparedInsertQuery pq = new PreparedInsertQuery();
    for (QuerySegment s : this.segments) {
      s.prepare(pq, context);
    }
    return pq;
  }

}
