package org.hotrod.livesql.queries.select.tuples.gen;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.bool.BooleanExpression;
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

public class SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> {

  private TuplesMetadata metadata;

  SelectTuplesFrom13Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <TV extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> join(TV t, final BooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <TV extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> join(TV t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <TV extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> leftJoin(TV t,
      final BooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <TV extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> leftJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <TV extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> rightJoin(TV t,
      final BooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <TV extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> rightJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <TV extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> fullJoin(TV t,
      final BooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <TV extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> fullJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <TV extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> crossJoin(TV t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <TV extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> naturalJoin(TV t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <TV extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> naturalLeftJoin(TV t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <TV extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> naturalRightJoin(TV t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  public <TV extends TableOrView<N>, N> SelectTuplesFrom14Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N> naturalFullJoin(TV t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom14Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> join(Subquery t, final BooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> leftJoin(Subquery t, final BooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> rightJoin(Subquery t, final BooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom13Phase<A, B, C, D, E, F, G, H, I, J, K, L, M> fullJoin(Subquery t, final BooleanExpression on) {
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

  public SelectWherePhase<Tuple13<A, B, C, D, E, F, G, H, I, J, K, L, M>> where(final BooleanExpression predicate) {
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
