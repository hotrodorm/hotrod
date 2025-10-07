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

public class SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> {

  private TuplesMetadata metadata;

  public SelectTuplesFrom16Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> join(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> join(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> leftJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> leftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> rightJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> rightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> fullJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> fullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> crossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> naturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> naturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> naturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom17Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> naturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom17Phase<>(this.metadata);
  }

  // semi-joining TableOrView

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> semiJoin(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on), false);
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> semiJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using), false);
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> semiLeftJoin(T0 t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on), false);
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> semiLeftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using), false);
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> semiRightJoin(T0 t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on), false);
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> semiRightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using), false);
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> semiFullJoin(T0 t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on), false);
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> semiFullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using), false);
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> semiCrossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t), false);
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> semiNaturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t), false);
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> semiNaturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t), false);
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> semiNaturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t), false);
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<Q>, Q> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> semiNaturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t), false);
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> join(Subquery t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> leftJoin(Subquery t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> rightJoin(Subquery t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> fullJoin(Subquery t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> where(final Predicate predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> execute() {
    CombinedSelectObject<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public Cursor<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public Cursor<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING, fetchSize);
  }

  public Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> executeOne() throws SQLException {
    CombinedSelectObject<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public List<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> execute(LiveSQLLogging liveSQLLogging) {
    CombinedSelectObject<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext(), liveSQLLogging);
  }

  public Cursor<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> executeCursor(LiveSQLLogging liveSQLLogging) throws SQLException {
    CombinedSelectObject<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), liveSQLLogging);
  }

  public Cursor<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> executeCursor(LiveSQLLogging liveSQLLogging, int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), liveSQLLogging, fetchSize);
  }

  public Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> executeOne(LiveSQLLogging liveSQLLogging) throws SQLException {
    CombinedSelectObject<Tuple16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext(), liveSQLLogging);
  }

}
