package org.hotrod.dynamicsql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.segments.QuerySegment;
import org.hotrod.dynamicsql.tuples.Tuple2;
import org.hotrod.dynamicsql.tuples.Tuple3;
import org.hotrod.dynamicsql.tuples.Tuple4;
import org.hotrod.dynamicsql.tuples.Tuple5;
import org.hotrod.dynamicsql.tuples.Tuple6;

public class DynamicSelectQuery extends DynamicQuery {

  public DynamicSelectQuery(List<QuerySegment> segments) {
    super(segments);
  }

  public <T> PreparedSelectQuery<T> prepare(Parameters context, RowReader<T> rr) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<T>(sc, rr);
  }

  public PreparedSelectQuery<Row> prepare(Parameters context) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<Row>(sc, new MapRowReader());
  }

  public <A> PreparedSelectQuery<A> prepare(Parameters context, Class<A> a) throws DynamicExpressionException {
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

  public <A, B> PreparedSelectQuery<Tuple2<A, B>> prepare(Parameters context, Class<A> a, Class<B> b)
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

  public <A, B, C> PreparedSelectQuery<Tuple3<A, B, C>> prepare(Parameters context, Class<A> a, Class<B> b, Class<C> c)
      throws DynamicExpressionException {
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

  public <A, B, C, D> PreparedSelectQuery<Tuple4<A, B, C, D>> prepare(Parameters context, Class<A> a, Class<B> b,
      Class<C> c, Class<D> d) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<Tuple4<A, B, C, D>>(sc, new RowReader<Tuple4<A, B, C, D>>() {

      @Override
      public Tuple4<A, B, C, D> readRowFrom(ResultSet rs, Connection conn) throws SQLException {
        Tuple4<A, B, C, D> tuple = new Tuple4<>();
        tuple.setA(rs.getObject(1, a));
        tuple.setB(rs.getObject(2, b));
        tuple.setC(rs.getObject(3, c));
        tuple.setD(rs.getObject(4, d));
        return tuple;
      }

    });
  }

  public <A, B, C, D, E> PreparedSelectQuery<Tuple5<A, B, C, D, E>> prepare(Parameters context, Class<A> a, Class<B> b,
      Class<C> c, Class<D> d, Class<E> e) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<Tuple5<A, B, C, D, E>>(sc, new RowReader<Tuple5<A, B, C, D, E>>() {

      @Override
      public Tuple5<A, B, C, D, E> readRowFrom(ResultSet rs, Connection conn) throws SQLException {
        Tuple5<A, B, C, D, E> tuple = new Tuple5<>();
        tuple.setA(rs.getObject(1, a));
        tuple.setB(rs.getObject(2, b));
        tuple.setC(rs.getObject(3, c));
        tuple.setD(rs.getObject(4, d));
        tuple.setE(rs.getObject(5, e));
        return tuple;
      }

    });
  }

  public <A, B, C, D, E, F> PreparedSelectQuery<Tuple6<A, B, C, D, E, F>> prepare(Parameters context, Class<A> a,
      Class<B> b, Class<C> c, Class<D> d, Class<E> e, Class<F> f) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<Tuple6<A, B, C, D, E, F>>(sc, new RowReader<Tuple6<A, B, C, D, E, F>>() {

      @Override
      public Tuple6<A, B, C, D, E, F> readRowFrom(ResultSet rs, Connection conn) throws SQLException {
        Tuple6<A, B, C, D, E, F> tuple = new Tuple6<>();
        tuple.setA(rs.getObject(1, a));
        tuple.setB(rs.getObject(2, b));
        tuple.setC(rs.getObject(3, c));
        tuple.setD(rs.getObject(4, d));
        tuple.setE(rs.getObject(5, e));
        return tuple;
      }

    });
  }

}
