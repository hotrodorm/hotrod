package org.hotrod.livesql.queries.select.src;

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
import org.hotrod.livesql.queries.subqueries.Subquery;

public class SelectTuplesFrom2Phase<A, B> {

  private TuplesMetadata metadata;

  SelectTuplesFrom2Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <T extends TableOrView<C>, C> SelectTuplesFrom3Phase<A, B, C> join(T t, final GeneralBooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom3Phase<>(this.metadata);
  }

  public <T extends TableOrView<C>, C> SelectTuplesFrom3Phase<A, B, C> join(T t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom3Phase<>(this.metadata);
  }

  public <T extends TableOrView<C>, C> SelectTuplesFrom3Phase<A, B, C> leftJoin(T t,
      final GeneralBooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom3Phase<>(this.metadata);
  }

  public <T extends TableOrView<C>, C> SelectTuplesFrom3Phase<A, B, C> leftJoin(T t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom3Phase<>(this.metadata);
  }

  public <T extends TableOrView<C>, C> SelectTuplesFrom3Phase<A, B, C> rightJoin(T t,
      final GeneralBooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom3Phase<>(this.metadata);
  }

  public <T extends TableOrView<C>, C> SelectTuplesFrom3Phase<A, B, C> rightJoin(T t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom3Phase<>(this.metadata);
  }

  public <T extends TableOrView<C>, C> SelectTuplesFrom3Phase<A, B, C> fullJoin(T t,
      final GeneralBooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom3Phase<>(this.metadata);
  }

  public <T extends TableOrView<C>, C> SelectTuplesFrom3Phase<A, B, C> fullJoin(T t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom3Phase<>(this.metadata);
  }

  public <T extends TableOrView<C>, C> SelectTuplesFrom3Phase<A, B, C> crossJoin(T t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom3Phase<>(this.metadata);
  }

  public <T extends TableOrView<C>, C> SelectTuplesFrom3Phase<A, B, C> naturalJoin(T t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom3Phase<>(this.metadata);
  }

  public <T extends TableOrView<C>, C> SelectTuplesFrom3Phase<A, B, C> naturalLeftJoin(T t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom3Phase<>(this.metadata);
  }

  public <T extends TableOrView<C>, C> SelectTuplesFrom3Phase<A, B, C> naturalRightJoin(T t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom3Phase<>(this.metadata);
  }

  public <T extends TableOrView<C>, C> SelectTuplesFrom3Phase<A, B, C> naturalFullJoin(T t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom3Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom2Phase<A, B> join(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom2Phase<A, B> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom2Phase<A, B> leftJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom2Phase<A, B> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom2Phase<A, B> rightJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom2Phase<A, B> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom2Phase<A, B> fullJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom2Phase<A, B> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom2Phase<A, B> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom2Phase<A, B> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom2Phase<A, B> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom2Phase<A, B> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom2Phase<A, B> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple2<A, B>> where(final GeneralBooleanExpression predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple2<A, B>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple2<A, B>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple2<A, B>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple2<A, B>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple2<A, B>> execute() {
    CombinedSelectObject<Tuple2<A, B>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple2<A, B>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple2<A, B>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple2<A, B>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple2<A, B>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple2<A, B> executeOne() throws SQLException {
    CombinedSelectObject<Tuple2<A, B>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }

}
