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

public class SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> {

  private TuplesMetadata metadata;

  public SelectTuplesFrom19Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> join(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom20Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> join(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom20Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> leftJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom20Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> leftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom20Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> rightJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom20Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> rightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom20Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> fullJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom20Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> fullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom20Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> crossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom20Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> naturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom20Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> naturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom20Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> naturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom20Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> naturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom20Phase<>(this.metadata);
  }

  // semi-joining TableOrView

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> semiJoin(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on), false);
    return new SelectTuplesFrom19Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> semiJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using), false);
    return new SelectTuplesFrom19Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> semiLeftJoin(T0 t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on), false);
    return new SelectTuplesFrom19Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> semiLeftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using), false);
    return new SelectTuplesFrom19Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> semiRightJoin(T0 t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on), false);
    return new SelectTuplesFrom19Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> semiRightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using), false);
    return new SelectTuplesFrom19Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> semiFullJoin(T0 t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on), false);
    return new SelectTuplesFrom19Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> semiFullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using), false);
    return new SelectTuplesFrom19Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> semiCrossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t), false);
    return new SelectTuplesFrom19Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> semiNaturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t), false);
    return new SelectTuplesFrom19Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> semiNaturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t), false);
    return new SelectTuplesFrom19Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> semiNaturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t), false);
    return new SelectTuplesFrom19Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<T>, T> SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> semiNaturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t), false);
    return new SelectTuplesFrom19Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> join(Subquery t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> leftJoin(Subquery t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> rightJoin(Subquery t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> fullJoin(Subquery t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom19Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> where(final Predicate predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> execute() {
    CombinedSelectObject<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public Cursor<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public Cursor<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING, fetchSize);
  }

  public Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> executeOne() throws SQLException {
    CombinedSelectObject<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public List<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> execute(LiveSQLLogging loggingAdapter) {
    CombinedSelectObject<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext(), loggingAdapter);
  }

  public Cursor<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> executeCursor(LiveSQLLogging loggingAdapter) throws SQLException {
    CombinedSelectObject<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), loggingAdapter);
  }

  public Cursor<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> executeCursor(LiveSQLLogging loggingAdapter, int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), loggingAdapter, fetchSize);
  }

  public Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> executeOne(LiveSQLLogging loggingAdapter) throws SQLException {
    CombinedSelectObject<Tuple19<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext(), loggingAdapter);
  }

}
