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

public class SelectTuplesFrom6Phase<A, B, C, D, E, F> {

  private TuplesMetadata metadata;

  public SelectTuplesFrom6Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> join(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> join(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> leftJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> leftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> rightJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> rightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> fullJoin(T0 t,
      final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> fullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> crossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> naturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> naturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> naturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom7Phase<A, B, C, D, E, F, G> naturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom7Phase<>(this.metadata);
  }

  // semi-joining TableOrView

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom6Phase<A, B, C, D, E, F> semiJoin(T0 t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on), false);
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom6Phase<A, B, C, D, E, F> semiJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using), false);
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom6Phase<A, B, C, D, E, F> semiLeftJoin(T0 t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on), false);
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom6Phase<A, B, C, D, E, F> semiLeftJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using), false);
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom6Phase<A, B, C, D, E, F> semiRightJoin(T0 t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on), false);
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom6Phase<A, B, C, D, E, F> semiRightJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using), false);
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom6Phase<A, B, C, D, E, F> semiFullJoin(T0 t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on), false);
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom6Phase<A, B, C, D, E, F> semiFullJoin(T0 t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using), false);
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom6Phase<A, B, C, D, E, F> semiCrossJoin(T0 t) {
    this.metadata.join(new CrossJoin(t), false);
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom6Phase<A, B, C, D, E, F> semiNaturalJoin(T0 t) {
    this.metadata.join(new NaturalInnerJoin(t), false);
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom6Phase<A, B, C, D, E, F> semiNaturalLeftJoin(T0 t) {
    this.metadata.join(new NaturalLeftOuterJoin(t), false);
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom6Phase<A, B, C, D, E, F> semiNaturalRightJoin(T0 t) {
    this.metadata.join(new NaturalRightOuterJoin(t), false);
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  public <T0 extends TableOrView<G>, G> SelectTuplesFrom6Phase<A, B, C, D, E, F> semiNaturalFullJoin(T0 t) {
    this.metadata.join(new NaturalFullOuterJoin(t), false);
    return new SelectTuplesFrom6Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom6Phase<A, B, C, D, E, F> join(Subquery t, final Predicate on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom6Phase<A, B, C, D, E, F> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom6Phase<A, B, C, D, E, F> leftJoin(Subquery t, final Predicate on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom6Phase<A, B, C, D, E, F> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom6Phase<A, B, C, D, E, F> rightJoin(Subquery t, final Predicate on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom6Phase<A, B, C, D, E, F> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom6Phase<A, B, C, D, E, F> fullJoin(Subquery t, final Predicate on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom6Phase<A, B, C, D, E, F> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom6Phase<A, B, C, D, E, F> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom6Phase<A, B, C, D, E, F> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom6Phase<A, B, C, D, E, F> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom6Phase<A, B, C, D, E, F> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom6Phase<A, B, C, D, E, F> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple6<A, B, C, D, E, F>> where(final Predicate predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple6<A, B, C, D, E, F>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple6<A, B, C, D, E, F>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple6<A, B, C, D, E, F>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple6<A, B, C, D, E, F>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple6<A, B, C, D, E, F>> execute() {
    CombinedSelectObject<Tuple6<A, B, C, D, E, F>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public Cursor<Tuple6<A, B, C, D, E, F>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple6<A, B, C, D, E, F>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public Cursor<Tuple6<A, B, C, D, E, F>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple6<A, B, C, D, E, F>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING, fetchSize);
  }

  public Tuple6<A, B, C, D, E, F> executeOne() throws SQLException {
    CombinedSelectObject<Tuple6<A, B, C, D, E, F>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext(), LiveSQLLogging.NO_LOGGING);
  }

  public List<Tuple6<A, B, C, D, E, F>> execute(LiveSQLLogging liveSQLLogging) {
    CombinedSelectObject<Tuple6<A, B, C, D, E, F>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext(), liveSQLLogging);
  }

  public Cursor<Tuple6<A, B, C, D, E, F>> executeCursor(LiveSQLLogging liveSQLLogging) throws SQLException {
    CombinedSelectObject<Tuple6<A, B, C, D, E, F>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), liveSQLLogging);
  }

  public Cursor<Tuple6<A, B, C, D, E, F>> executeCursor(LiveSQLLogging liveSQLLogging, int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple6<A, B, C, D, E, F>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), liveSQLLogging, fetchSize);
  }

  public Tuple6<A, B, C, D, E, F> executeOne(LiveSQLLogging liveSQLLogging) throws SQLException {
    CombinedSelectObject<Tuple6<A, B, C, D, E, F>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext(), liveSQLLogging);
  }

}
