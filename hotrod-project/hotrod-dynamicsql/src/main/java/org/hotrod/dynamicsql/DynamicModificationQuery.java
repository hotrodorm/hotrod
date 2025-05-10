package org.hotrod.dynamicsql;

import java.sql.Connection;
import java.sql.SQLException;
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


  public PreparedModificationQuery prepare(Parameters params) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, params, 0);
    }
    return new PreparedModificationQuery(sc);
  }

  public int execute(Connection conn) throws SQLException, DynamicExpressionException {
    return this.prepare(new DynamicSQL().newParameters()).execute(conn);
  }

  public int execute(Connection conn,Parameters params) throws SQLException, DynamicExpressionException {
    return this.prepare(params).execute(conn);
  }

}
