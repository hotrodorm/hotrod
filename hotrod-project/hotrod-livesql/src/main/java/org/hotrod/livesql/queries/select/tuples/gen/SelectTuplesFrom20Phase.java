package org.hotrod.livesql.queries.select.tuples.gen;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.dynamicsql.Cursor;
import org.hotrod.livesql.expressions.ComparableExpression;
import org.hotrod.livesql.expressions.bool.GeneralBooleanExpression;
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

public class SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> {

  private TuplesMetadata metadata;

  SelectTuplesFrom20Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <TV extends TableOrView<U>, U> SelectTuplesFrom21Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> join(TV t, final GeneralBooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom21Phase<>(this.metadata);
  }

  public <TV extends TableOrView<U>, U> SelectTuplesFrom21Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> join(TV t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom21Phase<>(this.metadata);
  }

  public <TV extends TableOrView<U>, U> SelectTuplesFrom21Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> leftJoin(TV t,
      final GeneralBooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom21Phase<>(this.metadata);
  }

  public <TV extends TableOrView<U>, U> SelectTuplesFrom21Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> leftJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom21Phase<>(this.metadata);
  }

  public <TV extends TableOrView<U>, U> SelectTuplesFrom21Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> rightJoin(TV t,
      final GeneralBooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom21Phase<>(this.metadata);
  }

  public <TV extends TableOrView<U>, U> SelectTuplesFrom21Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> rightJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom21Phase<>(this.metadata);
  }

  public <TV extends TableOrView<U>, U> SelectTuplesFrom21Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> fullJoin(TV t,
      final GeneralBooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom21Phase<>(this.metadata);
  }

  public <TV extends TableOrView<U>, U> SelectTuplesFrom21Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> fullJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom21Phase<>(this.metadata);
  }

  public <TV extends TableOrView<U>, U> SelectTuplesFrom21Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> crossJoin(TV t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom21Phase<>(this.metadata);
  }

  public <TV extends TableOrView<U>, U> SelectTuplesFrom21Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> naturalJoin(TV t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom21Phase<>(this.metadata);
  }

  public <TV extends TableOrView<U>, U> SelectTuplesFrom21Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> naturalLeftJoin(TV t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom21Phase<>(this.metadata);
  }

  public <TV extends TableOrView<U>, U> SelectTuplesFrom21Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> naturalRightJoin(TV t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom21Phase<>(this.metadata);
  }

  public <TV extends TableOrView<U>, U> SelectTuplesFrom21Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U> naturalFullJoin(TV t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom21Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> join(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> leftJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> rightJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> fullJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom20Phase<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T>> where(final GeneralBooleanExpression predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T>> execute() {
    CombinedSelectObject<Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T> executeOne() throws SQLException {
    CombinedSelectObject<Tuple20<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }

}
