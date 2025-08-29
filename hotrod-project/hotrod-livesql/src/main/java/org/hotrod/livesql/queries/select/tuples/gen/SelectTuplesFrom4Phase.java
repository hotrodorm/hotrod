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

public class SelectTuplesFrom4Phase<A, B, C, D> {

  private TuplesMetadata metadata;

  public SelectTuplesFrom4Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <TV extends TableOrView<E>, E> SelectTuplesFrom5Phase<A, B, C, D, E> join(TV t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom5Phase<A, B, C, D, E> join(TV t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom5Phase<A, B, C, D, E> leftJoin(TV t,
      final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom5Phase<A, B, C, D, E> leftJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom5Phase<A, B, C, D, E> rightJoin(TV t,
      final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom5Phase<A, B, C, D, E> rightJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom5Phase<A, B, C, D, E> fullJoin(TV t,
      final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom5Phase<A, B, C, D, E> fullJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom5Phase<A, B, C, D, E> crossJoin(TV t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom5Phase<A, B, C, D, E> naturalJoin(TV t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom5Phase<A, B, C, D, E> naturalLeftJoin(TV t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom5Phase<A, B, C, D, E> naturalRightJoin(TV t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom5Phase<A, B, C, D, E> naturalFullJoin(TV t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  // semi-joining TableOrView

  public <TV extends TableOrView<E>, E> SelectTuplesFrom4Phase<A, B, C, D> semiJoin(TV t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on), false);
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom4Phase<A, B, C, D> semiJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using), false);
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom4Phase<A, B, C, D> semiLeftJoin(TV t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on), false);
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom4Phase<A, B, C, D> semiLeftJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using), false);
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom4Phase<A, B, C, D> semiRightJoin(TV t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on), false);
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom4Phase<A, B, C, D> semiRightJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using), false);
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom4Phase<A, B, C, D> semiFullJoin(TV t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on), false);
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom4Phase<A, B, C, D> semiFullJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using), false);
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom4Phase<A, B, C, D> semiCrossJoin(TV t) {
    this.metadata.join(new CrossJoin(t), false);
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom4Phase<A, B, C, D> semiNaturalJoin(TV t) {
    this.metadata.join(new NaturalInnerJoin(t), false);
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom4Phase<A, B, C, D> semiNaturalLeftJoin(TV t) {
    this.metadata.join(new NaturalLeftOuterJoin(t), false);
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom4Phase<A, B, C, D> semiNaturalRightJoin(TV t) {
    this.metadata.join(new NaturalRightOuterJoin(t), false);
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<E>, E> SelectTuplesFrom4Phase<A, B, C, D> semiNaturalFullJoin(TV t) {
    this.metadata.join(new NaturalFullOuterJoin(t), false);
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom4Phase<A, B, C, D> join(Subquery t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom4Phase<A, B, C, D> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom4Phase<A, B, C, D> leftJoin(Subquery t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom4Phase<A, B, C, D> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom4Phase<A, B, C, D> rightJoin(Subquery t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom4Phase<A, B, C, D> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom4Phase<A, B, C, D> fullJoin(Subquery t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom4Phase<A, B, C, D> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom4Phase<A, B, C, D> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom4Phase<A, B, C, D> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom4Phase<A, B, C, D> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom4Phase<A, B, C, D> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom4Phase<A, B, C, D> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple4<A, B, C, D>> where(final Predicate predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple4<A, B, C, D>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple4<A, B, C, D>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple4<A, B, C, D>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple4<A, B, C, D>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple4<A, B, C, D>> execute() {
    CombinedSelectObject<Tuple4<A, B, C, D>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple4<A, B, C, D>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple4<A, B, C, D>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple4<A, B, C, D>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple4<A, B, C, D>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple4<A, B, C, D> executeOne() throws SQLException {
    CombinedSelectObject<Tuple4<A, B, C, D>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }

}
