package org.hotrod.dynamicsql;

import java.util.List;

import org.hotrod.dynamicsql.assembler.DynamicSQL;
import org.hotrod.dynamicsql.segments.QuerySegment;

public class DynamicModificationQuery extends DynamicQuery {

  public DynamicModificationQuery(List<QuerySegment> segments) {
    super(segments);
  }

  public PreparedModificationQuery prepare() throws DynamicExpressionException {
    return this.prepare(new DynamicSQL().newParameters());
  }

  public PreparedModificationQuery prepare(Parameters context) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedModificationQuery(sc);
  }

}
