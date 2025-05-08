package org.hotrod.dynamicsql;

import java.util.List;
import java.util.Map;

import org.hotrod.dynamicsql.segments.QuerySegment;

public class DynamicSelectQuery extends DynamicQuery {

  public DynamicSelectQuery(List<QuerySegment> segments) {
    super(segments);
  }

  public <T> PreparedSelectQuery<T> prepare(ParameterContext context, RowReader<T> rr)
      throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<T>(sc, rr);
  }

  public PreparedSelectQuery<Map<String, Object>> prepare(ParameterContext context) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<Map<String, Object>>(sc, new MapRowReader());
  }

  public <R> List<Map<String, Object>> p2() {
    return null;
  }

}
