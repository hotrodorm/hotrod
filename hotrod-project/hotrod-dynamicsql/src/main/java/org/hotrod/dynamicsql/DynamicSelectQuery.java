package org.hotrod.dynamicsql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.segments.QuerySegment;
import org.hotrod.dynamicsql.tuples.DTuple2;
import org.hotrod.dynamicsql.tuples.DTuple3;
import org.hotrod.dynamicsql.tuples.DTuple4;
import org.hotrod.dynamicsql.tuples.DTuple5;
import org.hotrod.dynamicsql.tuples.DTuple6;

public class DynamicSelectQuery extends DynamicQuery {

  private static final Logger log = Logger.getLogger(DynamicSelectQuery.class.getName());

  public DynamicSelectQuery(DynamicExpressionFactory factory, List<QuerySegment> segments) {
    super(factory, segments);
    log.fine("init");
  }

  public <T> PreparedSelectQuery<T> prepare(Parameters context, RowReader<T> rr) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<>(sc, rr);
  }

  public PreparedSelectQuery<Row> prepare(Parameters context) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<>(sc, new MapRowReader());
  }

  public <A> PreparedSelectQuery<A> prepare(Parameters context, Class<A> a) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<>(sc, new RowReader<A>() {

      @Override
      public A readRowFrom(ResultSet rs, Connection conn) throws SQLException {
        return rs.getObject(1, a);
      }

    });
  }

  public <A, B> PreparedSelectQuery<DTuple2<A, B>> prepare(Parameters context, Class<A> a, Class<B> b)
      throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<>(sc, new RowReader<DTuple2<A, B>>() {

      @Override
      public DTuple2<A, B> readRowFrom(ResultSet rs, Connection conn) throws SQLException {
        DTuple2<A, B> tuple = new DTuple2<>();
        tuple.setA(rs.getObject(1, a));
        tuple.setB(rs.getObject(2, b));
        return tuple;
      }

    });
  }

  public <A, B, C> PreparedSelectQuery<DTuple3<A, B, C>> prepare(Parameters context, Class<A> a, Class<B> b, Class<C> c)
      throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<>(sc, new RowReader<DTuple3<A, B, C>>() {

      @Override
      public DTuple3<A, B, C> readRowFrom(ResultSet rs, Connection conn) throws SQLException {
        DTuple3<A, B, C> tuple = new DTuple3<>();
        tuple.setA(rs.getObject(1, a));
        tuple.setB(rs.getObject(2, b));
        tuple.setC(rs.getObject(3, c));
        return tuple;
      }

    });
  }

  public <A, B, C, D> PreparedSelectQuery<DTuple4<A, B, C, D>> prepare(Parameters context, Class<A> a, Class<B> b,
      Class<C> c, Class<D> d) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<>(sc, new RowReader<DTuple4<A, B, C, D>>() {

      @Override
      public DTuple4<A, B, C, D> readRowFrom(ResultSet rs, Connection conn) throws SQLException {
        DTuple4<A, B, C, D> tuple = new DTuple4<>();
        tuple.setA(rs.getObject(1, a));
        tuple.setB(rs.getObject(2, b));
        tuple.setC(rs.getObject(3, c));
        tuple.setD(rs.getObject(4, d));
        return tuple;
      }

    });
  }

  public <A, B, C, D, E> PreparedSelectQuery<DTuple5<A, B, C, D, E>> prepare(Parameters context, Class<A> a, Class<B> b,
      Class<C> c, Class<D> d, Class<E> e) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<>(sc, new RowReader<DTuple5<A, B, C, D, E>>() {

      @Override
      public DTuple5<A, B, C, D, E> readRowFrom(ResultSet rs, Connection conn) throws SQLException {
        DTuple5<A, B, C, D, E> tuple = new DTuple5<>();
        tuple.setA(rs.getObject(1, a));
        tuple.setB(rs.getObject(2, b));
        tuple.setC(rs.getObject(3, c));
        tuple.setD(rs.getObject(4, d));
        tuple.setE(rs.getObject(5, e));
        return tuple;
      }

    });
  }

  public <A, B, C, D, E, F> PreparedSelectQuery<DTuple6<A, B, C, D, E, F>> prepare(Parameters context, Class<A> a,
      Class<B> b, Class<C> c, Class<D> d, Class<E> e, Class<F> f) throws DynamicExpressionException {
    SimpleStaticSegmentConsumer sc = new SimpleStaticSegmentConsumer();
    for (QuerySegment s : this.segments) {
      s.prepare(sc, context, 0);
    }
    return new PreparedSelectQuery<>(sc, new RowReader<DTuple6<A, B, C, D, E, F>>() {

      @Override
      public DTuple6<A, B, C, D, E, F> readRowFrom(ResultSet rs, Connection conn) throws SQLException {
        DTuple6<A, B, C, D, E, F> tuple = new DTuple6<>();
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
