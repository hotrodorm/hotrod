package org.hotrod.livesql.queries.select.tuples.gen;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.livesql.LiveSQLLogging;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.select.CrossJoin;
import org.hotrod.livesql.queries.select.FullOuterJoin;
import org.hotrod.livesql.queries.select.InnerJoin;
import org.hotrod.livesql.queries.select.LeftOuterJoin;
import org.hotrod.livesql.queries.select.LockableSelectLimitPhase;
import org.hotrod.livesql.queries.select.LockableSelectOffsetPhase;
import org.hotrod.livesql.queries.select.LockableSelectOrderByPhase;
import org.hotrod.livesql.queries.select.NaturalFullOuterJoin;
import org.hotrod.livesql.queries.select.NaturalInnerJoin;
import org.hotrod.livesql.queries.select.NaturalLeftOuterJoin;
import org.hotrod.livesql.queries.select.NaturalRightOuterJoin;
import org.hotrod.livesql.queries.select.RightOuterJoin;
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.SelectGroupByPhase;
import org.hotrod.livesql.queries.select.SelectWherePhase;
import org.hotrod.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.livesql.queries.select.tuples.TuplesMetadata;
import org.hotrod.livesql.queries.select.tuples.TuplesSelectObject;
import org.hotrod.livesql.queries.subqueries.Subquery;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public class SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> {

  private TuplesMetadata metadata;

  public SelectTuplesFrom17Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom18Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> join(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom18Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom18Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> join(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom18Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom18Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> leftJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom18Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom18Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> leftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom18Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom18Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> rightJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom18Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom18Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> rightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom18Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom18Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> fullJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom18Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom18Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> fullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom18Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom18Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> crossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom18Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom18Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> naturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom18Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom18Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> naturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom18Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom18Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> naturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom18Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom18Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> naturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom18Phase<>(this.metadata);
  }

  // semi-joining TableOrView

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> semiJoin(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on), false);
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> semiJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using), false);
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> semiLeftJoin(T0 t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on), false);
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> semiLeftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using), false);
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> semiRightJoin(T0 t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on), false);
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> semiRightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using), false);
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> semiFullJoin(T0 t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on), false);
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> semiFullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using), false);
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> semiCrossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t), false);
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> semiNaturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t), false);
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> semiNaturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t), false);
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> semiNaturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t), false);
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<R>, R> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> semiNaturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t), false);
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> join(Subquery t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> leftJoin(Subquery t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> rightJoin(Subquery t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> fullJoin(Subquery t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> where(final Predicate predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> execute() {
    CombinedSelectObject<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public Cursor<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public Cursor<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING, fetchSize);
  }

  public Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> executeOne() throws SQLException {
    CombinedSelectObject<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public List<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> execute(LiveSQLLogging loggingAdapter) {
    CombinedSelectObject<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext(), loggingAdapter);
  }

  public Cursor<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> executeCursor(LiveSQLLogging loggingAdapter) throws SQLException {
    CombinedSelectObject<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), loggingAdapter);
  }

  public Cursor<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> executeCursor(LiveSQLLogging loggingAdapter, int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), loggingAdapter, fetchSize);
  }

  public Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> executeOne(LiveSQLLogging loggingAdapter) throws SQLException {
    CombinedSelectObject<Tuple17<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext(), loggingAdapter);
  }

}
