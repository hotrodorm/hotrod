package org.hotrod.dynamicsql;

import java.util.List;

import org.hotrod.dynamicsql.segments.QuerySegment;

public class DynamicModificationQuery extends DynamicQuery {

  public DynamicModificationQuery(DynamicExpressionFactory factory, List<QuerySegment> segments) {
    super(factory, segments);
  }

  public PreparedModificationQuery prepare() throws DynamicExpressionException {
    return this.prepare(super.factory.newParameterContext());
  }

  public PreparedModificationQuery prepare(Parameters params) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, params, 0);
    }
    return new PreparedModificationQuery(sc);
  }

}
