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

public class SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> {

  private TuplesMetadata metadata;

  public SelectTuplesFrom15Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <TV extends TableOrView<P>, P> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> join(TV t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> join(TV t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> leftJoin(TV t,
      final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> leftJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> rightJoin(TV t,
      final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> rightJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> fullJoin(TV t,
      final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> fullJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> crossJoin(TV t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> naturalJoin(TV t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> naturalLeftJoin(TV t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> naturalRightJoin(TV t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom16Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> naturalFullJoin(TV t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom16Phase<>(this.metadata);
  }

  // semi-joining TableOrView

  public <TV extends TableOrView<P>, P> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> semiJoin(TV t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on), false);
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> semiJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using), false);
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> semiLeftJoin(TV t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on), false);
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> semiLeftJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using), false);
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> semiRightJoin(TV t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on), false);
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> semiRightJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using), false);
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> semiFullJoin(TV t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on), false);
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> semiFullJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using), false);
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> semiCrossJoin(TV t) {
    this.metadata.join(new CrossJoin(t), false);
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> semiNaturalJoin(TV t) {
    this.metadata.join(new NaturalInnerJoin(t), false);
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> semiNaturalLeftJoin(TV t) {
    this.metadata.join(new NaturalLeftOuterJoin(t), false);
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> semiNaturalRightJoin(TV t) {
    this.metadata.join(new NaturalRightOuterJoin(t), false);
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  public <TV extends TableOrView<P>, P> SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> semiNaturalFullJoin(TV t) {
    this.metadata.join(new NaturalFullOuterJoin(t), false);
    return new SelectTuplesFrom15Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> join(Subquery t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> leftJoin(Subquery t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> rightJoin(Subquery t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> fullJoin(Subquery t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom15Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> where(final Predicate predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> execute() {
    CombinedSelectObject<Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> executeOne() throws SQLException {
    CombinedSelectObject<Tuple15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }

}
