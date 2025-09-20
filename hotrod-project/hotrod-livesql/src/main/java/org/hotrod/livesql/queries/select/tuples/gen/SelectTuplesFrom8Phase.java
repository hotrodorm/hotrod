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

public class SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> {

  private TuplesMetadata metadata;

  public SelectTuplesFrom8Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> join(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom9Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> join(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom9Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> leftJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom9Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> leftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom9Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> rightJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom9Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> rightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom9Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> fullJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom9Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> fullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom9Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> crossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom9Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> naturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom9Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> naturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom9Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> naturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom9Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom9Phase<A, B, C, D, E, F, G, H, I> naturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom9Phase<>(this.metadata);
  }

  // semi-joining TableOrView

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> semiJoin(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on), false);
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> semiJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using), false);
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> semiLeftJoin(T0 t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on), false);
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> semiLeftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using), false);
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> semiRightJoin(T0 t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on), false);
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> semiRightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using), false);
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> semiFullJoin(T0 t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on), false);
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> semiFullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using), false);
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> semiCrossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t), false);
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> semiNaturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t), false);
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> semiNaturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t), false);
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> semiNaturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t), false);
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<I>, I> SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> semiNaturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t), false);
    return new SelectTuplesFrom8Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> join(Subquery t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> leftJoin(Subquery t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> rightJoin(Subquery t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> fullJoin(Subquery t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom8Phase<A, B, C, D, E, F, G, H> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple8<A, B, C, D, E, F, G, H>> where(final Predicate predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple8<A, B, C, D, E, F, G, H>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple8<A, B, C, D, E, F, G, H>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple8<A, B, C, D, E, F, G, H>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple8<A, B, C, D, E, F, G, H>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple8<A, B, C, D, E, F, G, H>> execute() {
    CombinedSelectObject<Tuple8<A, B, C, D, E, F, G, H>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple8<A, B, C, D, E, F, G, H>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple8<A, B, C, D, E, F, G, H>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple8<A, B, C, D, E, F, G, H>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple8<A, B, C, D, E, F, G, H>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple8<A, B, C, D, E, F, G, H> executeOne() throws SQLException {
    CombinedSelectObject<Tuple8<A, B, C, D, E, F, G, H>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }

}
