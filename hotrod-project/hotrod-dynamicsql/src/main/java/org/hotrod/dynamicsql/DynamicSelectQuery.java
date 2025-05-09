package org.hotrod.dynamicsql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hotrod.data.RowReader;
import org.hotrod.dynamicsql.segments.QuerySegment;
import org.hotrod.dynamicsql.tuples.Tuple2;
import org.hotrod.dynamicsql.tuples.Tuple3;

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

  public <A> PreparedSelectQuery<A> prepare(ParameterContext context, Class<A> a) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<A>(sc, new RowReader<A>() {

      @Override
      public A readRowFrom(ResultSet rs, Connection conn) throws SQLException {
        return rs.getObject(1, a);
      }

    });
  }

  public <A, B> PreparedSelectQuery<Tuple2<A, B>> prepare(ParameterContext context, Class<A> a, Class<B> b)
      throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<Tuple2<A, B>>(sc, new RowReader<Tuple2<A, B>>() {

      @Override
      public Tuple2<A, B> readRowFrom(ResultSet rs, Connection conn) throws SQLException {
        Tuple2<A, B> tuple = new Tuple2<>();
        tuple.setA(rs.getObject(1, a));
        tuple.setB(rs.getObject(2, b));
        return tuple;
      }

    });
  }

  public <A, B, C> PreparedSelectQuery<Tuple3<A, B, C>> prepare(ParameterContext context, Class<A> a, Class<B> b,
      Class<C> c) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<Tuple3<A, B, C>>(sc, new RowReader<Tuple3<A, B, C>>() {

      @Override
      public Tuple3<A, B, C> readRowFrom(ResultSet rs, Connection conn) throws SQLException {
        Tuple3<A, B, C> tuple = new Tuple3<>();
        tuple.setA(rs.getObject(1, a));
        tuple.setB(rs.getObject(2, b));
        tuple.setC(rs.getObject(3, c));
        return tuple;
      }

    });
  }

}
