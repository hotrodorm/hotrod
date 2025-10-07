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

public class SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> {

  private TuplesMetadata metadata;

  public SelectTuplesFrom11Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom12Phase<A, B, C, D, E, F, G, H, I, J, K, L> join(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom12Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom12Phase<A, B, C, D, E, F, G, H, I, J, K, L> join(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom12Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom12Phase<A, B, C, D, E, F, G, H, I, J, K, L> leftJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom12Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom12Phase<A, B, C, D, E, F, G, H, I, J, K, L> leftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom12Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom12Phase<A, B, C, D, E, F, G, H, I, J, K, L> rightJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom12Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom12Phase<A, B, C, D, E, F, G, H, I, J, K, L> rightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom12Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom12Phase<A, B, C, D, E, F, G, H, I, J, K, L> fullJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom12Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom12Phase<A, B, C, D, E, F, G, H, I, J, K, L> fullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom12Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom12Phase<A, B, C, D, E, F, G, H, I, J, K, L> crossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom12Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom12Phase<A, B, C, D, E, F, G, H, I, J, K, L> naturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom12Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom12Phase<A, B, C, D, E, F, G, H, I, J, K, L> naturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom12Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom12Phase<A, B, C, D, E, F, G, H, I, J, K, L> naturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom12Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom12Phase<A, B, C, D, E, F, G, H, I, J, K, L> naturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom12Phase<>(this.metadata);
  }

  // semi-joining TableOrView

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> semiJoin(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on), false);
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> semiJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using), false);
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> semiLeftJoin(T0 t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on), false);
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> semiLeftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using), false);
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> semiRightJoin(T0 t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on), false);
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> semiRightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using), false);
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> semiFullJoin(T0 t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on), false);
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> semiFullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using), false);
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> semiCrossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t), false);
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> semiNaturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t), false);
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> semiNaturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t), false);
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> semiNaturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t), false);
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<L>, L> SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> semiNaturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t), false);
    return new SelectTuplesFrom11Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> join(Subquery t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> leftJoin(Subquery t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> rightJoin(Subquery t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> fullJoin(Subquery t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom11Phase<A, B, C, D, E, F, G, H, I, J, K> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> where(final Predicate predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> execute() {
    CombinedSelectObject<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public Cursor<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public Cursor<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING, fetchSize);
  }

  public Tuple11<A, B, C, D, E, F, G, H, I, J, K> executeOne() throws SQLException {
    CombinedSelectObject<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public List<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> execute(LiveSQLLogging loggingAdapter) {
    CombinedSelectObject<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext(), loggingAdapter);
  }

  public Cursor<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> executeCursor(LiveSQLLogging loggingAdapter) throws SQLException {
    CombinedSelectObject<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), loggingAdapter);
  }

  public Cursor<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> executeCursor(LiveSQLLogging loggingAdapter, int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), loggingAdapter, fetchSize);
  }

  public Tuple11<A, B, C, D, E, F, G, H, I, J, K> executeOne(LiveSQLLogging loggingAdapter) throws SQLException {
    CombinedSelectObject<Tuple11<A, B, C, D, E, F, G, H, I, J, K>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext(), loggingAdapter);
  }

}
