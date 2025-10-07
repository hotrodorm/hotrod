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

public class SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> {

  private TuplesMetadata metadata;

  public SelectTuplesFrom14Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> join(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> join(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> leftJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> leftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> rightJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> rightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> fullJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> fullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> crossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> naturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> naturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> naturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> naturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  // semi-joining TableOrView

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> semiJoin(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on), false);
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> semiJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using), false);
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> semiLeftJoin(T0 t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on), false);
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> semiLeftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using), false);
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> semiRightJoin(T0 t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on), false);
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> semiRightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using), false);
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> semiFullJoin(T0 t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on), false);
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> semiFullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using), false);
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> semiCrossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t), false);
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> semiNaturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t), false);
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> semiNaturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t), false);
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> semiNaturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t), false);
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<O>, O> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> semiNaturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t), false);
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> join(Subquery t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> leftJoin(Subquery t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> rightJoin(Subquery t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> fullJoin(Subquery t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> where(final Predicate predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> execute() {
    CombinedSelectObject<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public Cursor<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public Cursor<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING, fetchSize);
  }

  public Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> executeOne() throws SQLException {
    CombinedSelectObject<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public List<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> execute(LiveSQLLogging liveSQLLogging) {
    CombinedSelectObject<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext(), liveSQLLogging);
  }

  public Cursor<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> executeCursor(LiveSQLLogging liveSQLLogging) throws SQLException {
    CombinedSelectObject<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), liveSQLLogging);
  }

  public Cursor<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> executeCursor(LiveSQLLogging liveSQLLogging, int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), liveSQLLogging, fetchSize);
  }

  public Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> executeOne(LiveSQLLogging liveSQLLogging) throws SQLException {
    CombinedSelectObject<Tuple14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext(), liveSQLLogging);
  }

}
