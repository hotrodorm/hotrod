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

public class SelectTuplesFrom5Phase<A, B, C, D, E> {

  private TuplesMetadata metadata;

  public SelectTuplesFrom5Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <TV extends TableOrView<F>, F> SelectTuplesFrom6Phase<A, B, C, D, E, F> join(TV t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom6Phase<A, B, C, D, E, F> join(TV t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom6Phase<A, B, C, D, E, F> leftJoin(TV t,
      final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom6Phase<A, B, C, D, E, F> leftJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom6Phase<A, B, C, D, E, F> rightJoin(TV t,
      final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom6Phase<A, B, C, D, E, F> rightJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom6Phase<A, B, C, D, E, F> fullJoin(TV t,
      final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom6Phase<A, B, C, D, E, F> fullJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom6Phase<A, B, C, D, E, F> crossJoin(TV t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom6Phase<A, B, C, D, E, F> naturalJoin(TV t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom6Phase<A, B, C, D, E, F> naturalLeftJoin(TV t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom6Phase<A, B, C, D, E, F> naturalRightJoin(TV t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom6Phase<A, B, C, D, E, F> naturalFullJoin(TV t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  // semi-joining TableOrView

  public <TV extends TableOrView<F>, F> SelectTuplesFrom5Phase<A, B, C, D, E> semiJoin(TV t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on), false);
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom5Phase<A, B, C, D, E> semiJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using), false);
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom5Phase<A, B, C, D, E> semiLeftJoin(TV t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on), false);
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom5Phase<A, B, C, D, E> semiLeftJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using), false);
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom5Phase<A, B, C, D, E> semiRightJoin(TV t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on), false);
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom5Phase<A, B, C, D, E> semiRightJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using), false);
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom5Phase<A, B, C, D, E> semiFullJoin(TV t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on), false);
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom5Phase<A, B, C, D, E> semiFullJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using), false);
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom5Phase<A, B, C, D, E> semiCrossJoin(TV t) {
    this.metadata.join(new CrossJoin(t), false);
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom5Phase<A, B, C, D, E> semiNaturalJoin(TV t) {
    this.metadata.join(new NaturalInnerJoin(t), false);
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom5Phase<A, B, C, D, E> semiNaturalLeftJoin(TV t) {
    this.metadata.join(new NaturalLeftOuterJoin(t), false);
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom5Phase<A, B, C, D, E> semiNaturalRightJoin(TV t) {
    this.metadata.join(new NaturalRightOuterJoin(t), false);
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  public <TV extends TableOrView<F>, F> SelectTuplesFrom5Phase<A, B, C, D, E> semiNaturalFullJoin(TV t) {
    this.metadata.join(new NaturalFullOuterJoin(t), false);
    return new SelectTuplesFrom5Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom5Phase<A, B, C, D, E> join(Subquery t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom5Phase<A, B, C, D, E> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom5Phase<A, B, C, D, E> leftJoin(Subquery t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom5Phase<A, B, C, D, E> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom5Phase<A, B, C, D, E> rightJoin(Subquery t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom5Phase<A, B, C, D, E> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom5Phase<A, B, C, D, E> fullJoin(Subquery t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom5Phase<A, B, C, D, E> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom5Phase<A, B, C, D, E> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom5Phase<A, B, C, D, E> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom5Phase<A, B, C, D, E> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom5Phase<A, B, C, D, E> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom5Phase<A, B, C, D, E> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple5<A, B, C, D, E>> where(final Predicate predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple5<A, B, C, D, E>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple5<A, B, C, D, E>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple5<A, B, C, D, E>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple5<A, B, C, D, E>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple5<A, B, C, D, E>> execute() {
    CombinedSelectObject<Tuple5<A, B, C, D, E>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple5<A, B, C, D, E>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple5<A, B, C, D, E>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple5<A, B, C, D, E>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple5<A, B, C, D, E>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple5<A, B, C, D, E> executeOne() throws SQLException {
    CombinedSelectObject<Tuple5<A, B, C, D, E>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }

}
