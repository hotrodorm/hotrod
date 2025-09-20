package org.hotrod.livesql.queries.select.tuples.gen;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.Cursor;
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

public class SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> {

  private TuplesMetadata metadata;

  public SelectTuplesFrom13Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> join(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> join(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> leftJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> leftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> rightJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> rightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> fullJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> fullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> crossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> naturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> naturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> naturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> naturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  // semi-joining TableOrView

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> semiJoin(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on), false);
    return new SelectTuplesFrom13Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> semiJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using), false);
    return new SelectTuplesFrom13Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> semiLeftJoin(T0 t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on), false);
    return new SelectTuplesFrom13Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> semiLeftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using), false);
    return new SelectTuplesFrom13Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> semiRightJoin(T0 t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on), false);
    return new SelectTuplesFrom13Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> semiRightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using), false);
    return new SelectTuplesFrom13Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> semiFullJoin(T0 t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on), false);
    return new SelectTuplesFrom13Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> semiFullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using), false);
    return new SelectTuplesFrom13Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> semiCrossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t), false);
    return new SelectTuplesFrom13Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> semiNaturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t), false);
    return new SelectTuplesFrom13Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> semiNaturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t), false);
    return new SelectTuplesFrom13Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> semiNaturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t), false);
    return new SelectTuplesFrom13Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<N>, N> SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> semiNaturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t), false);
    return new SelectTuplesFrom13Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> join(Subquery t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> leftJoin(Subquery t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> rightJoin(Subquery t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> fullJoin(Subquery t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>> where(final Predicate predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>> execute() {
    CombinedSelectObject<Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M> executeOne() throws SQLException {
    CombinedSelectObject<Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }

}
