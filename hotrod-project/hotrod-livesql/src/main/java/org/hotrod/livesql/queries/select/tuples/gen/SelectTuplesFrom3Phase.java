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

public class SelectTuplesFrom3Phase<A, B, C> {

  private TuplesMetadata metadata;

  SelectTuplesFrom3Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <TV extends TableOrView<D>, D> SelectTuplesFrom4Phase<A, B, C, D> join(TV t, final GeneralBooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<D>, D> SelectTuplesFrom4Phase<A, B, C, D> join(TV t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<D>, D> SelectTuplesFrom4Phase<A, B, C, D> leftJoin(TV t,
      final GeneralBooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<D>, D> SelectTuplesFrom4Phase<A, B, C, D> leftJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<D>, D> SelectTuplesFrom4Phase<A, B, C, D> rightJoin(TV t,
      final GeneralBooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<D>, D> SelectTuplesFrom4Phase<A, B, C, D> rightJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<D>, D> SelectTuplesFrom4Phase<A, B, C, D> fullJoin(TV t,
      final GeneralBooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<D>, D> SelectTuplesFrom4Phase<A, B, C, D> fullJoin(TV t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<D>, D> SelectTuplesFrom4Phase<A, B, C, D> crossJoin(TV t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<D>, D> SelectTuplesFrom4Phase<A, B, C, D> naturalJoin(TV t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<D>, D> SelectTuplesFrom4Phase<A, B, C, D> naturalLeftJoin(TV t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<D>, D> SelectTuplesFrom4Phase<A, B, C, D> naturalRightJoin(TV t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  public <TV extends TableOrView<D>, D> SelectTuplesFrom4Phase<A, B, C, D> naturalFullJoin(TV t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom4Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom3Phase<A, B, C> join(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom3Phase<A, B, C> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom3Phase<A, B, C> leftJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom3Phase<A, B, C> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom3Phase<A, B, C> rightJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom3Phase<A, B, C> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom3Phase<A, B, C> fullJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom3Phase<A, B, C> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom3Phase<A, B, C> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom3Phase<A, B, C> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom3Phase<A, B, C> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom3Phase<A, B, C> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom3Phase<A, B, C> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple3<A, B, C>> where(final GeneralBooleanExpression predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple3<A, B, C>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple3<A, B, C>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple3<A, B, C>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple3<A, B, C>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple3<A, B, C>> execute() {
    CombinedSelectObject<Tuple3<A, B, C>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple3<A, B, C>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple3<A, B, C>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple3<A, B, C>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple3<A, B, C>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple3<A, B, C> executeOne() throws SQLException {
    CombinedSelectObject<Tuple3<A, B, C>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }

}
