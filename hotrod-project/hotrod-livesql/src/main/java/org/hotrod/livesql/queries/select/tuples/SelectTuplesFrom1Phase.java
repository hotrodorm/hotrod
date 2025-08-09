package org.hotrod.livesql.queries.select.tuples;

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

public class SelectTuplesFrom1Phase<A> {

  private TuplesMetadata metadata;

  SelectTuplesFrom1Phase(final TuplesMetadata metadata) {
    this.metadata = metadata;
  }

  // joining TableOrView

  public <T extends TableOrView<B>, B> SelectTuplesFrom2Phase<A, B> join(T t, final GeneralBooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return new SelectTuplesFrom2Phase<>(this.metadata);
  }

  public <T extends TableOrView<B>, B> SelectTuplesFrom2Phase<A, B> join(T t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return new SelectTuplesFrom2Phase<>(this.metadata);
  }

  public <T extends TableOrView<B>, B> SelectTuplesFrom2Phase<A, B> leftJoin(T t, final GeneralBooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return new SelectTuplesFrom2Phase<>(this.metadata);
  }

  public <T extends TableOrView<B>, B> SelectTuplesFrom2Phase<A, B> leftJoin(T t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return new SelectTuplesFrom2Phase<>(this.metadata);
  }

  public <T extends TableOrView<B>, B> SelectTuplesFrom2Phase<A, B> rightJoin(T t, final GeneralBooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return new SelectTuplesFrom2Phase<>(this.metadata);
  }

  public <T extends TableOrView<B>, B> SelectTuplesFrom2Phase<A, B> rightJoin(T t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return new SelectTuplesFrom2Phase<>(this.metadata);
  }

  public <T extends TableOrView<B>, B> SelectTuplesFrom2Phase<A, B> fullJoin(T t, final GeneralBooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return new SelectTuplesFrom2Phase<>(this.metadata);
  }

  public <T extends TableOrView<B>, B> SelectTuplesFrom2Phase<A, B> fullJoin(T t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return new SelectTuplesFrom2Phase<>(this.metadata);
  }

  public <T extends TableOrView<B>, B> SelectTuplesFrom2Phase<A, B> crossJoin(T t) {
    this.metadata.join(new CrossJoin(t));
    return new SelectTuplesFrom2Phase<>(this.metadata);
  }

  public <T extends TableOrView<B>, B> SelectTuplesFrom2Phase<A, B> naturalJoin(T t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return new SelectTuplesFrom2Phase<>(this.metadata);
  }

  public <T extends TableOrView<B>, B> SelectTuplesFrom2Phase<A, B> naturalLeftJoin(T t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return new SelectTuplesFrom2Phase<>(this.metadata);
  }

  public <T extends TableOrView<B>, B> SelectTuplesFrom2Phase<A, B> naturalRightJoin(T t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return new SelectTuplesFrom2Phase<>(this.metadata);
  }

  public <T extends TableOrView<B>, B> SelectTuplesFrom2Phase<A, B> naturalFullJoin(T t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return new SelectTuplesFrom2Phase<>(this.metadata);
  }

  // joining Subquery

  public SelectTuplesFrom1Phase<A> join(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new InnerJoin(t, on));
    return this;
  }

  public SelectTuplesFrom1Phase<A> join(Subquery t, final EntityColumn... using) {
    this.metadata.join(new InnerJoin(t, using));
    return this;
  }

  public SelectTuplesFrom1Phase<A> leftJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom1Phase<A> leftJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new LeftOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom1Phase<A> rightJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new RightOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom1Phase<A> rightJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new RightOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom1Phase<A> fullJoin(Subquery t, final GeneralBooleanExpression on) {
    this.metadata.join(new FullOuterJoin(t, on));
    return this;
  }

  public SelectTuplesFrom1Phase<A> fullJoin(Subquery t, final EntityColumn... using) {
    this.metadata.join(new FullOuterJoin(t, using));
    return this;
  }

  public SelectTuplesFrom1Phase<A> crossJoin(Subquery t) {
    this.metadata.join(new CrossJoin(t));
    return this;
  }

  public SelectTuplesFrom1Phase<A> naturalJoin(Subquery t) {
    this.metadata.join(new NaturalInnerJoin(t));
    return this;
  }

  public SelectTuplesFrom1Phase<A> naturalLeftJoin(Subquery t) {
    this.metadata.join(new NaturalLeftOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom1Phase<A> naturalRightJoin(Subquery t) {
    this.metadata.join(new NaturalRightOuterJoin(t));
    return this;
  }

  public SelectTuplesFrom1Phase<A> naturalFullJoin(Subquery t) {
    this.metadata.join(new NaturalFullOuterJoin(t));
    return this;
  }

  // next phases

  public SelectWherePhase<Tuple1<A>> where(final GeneralBooleanExpression predicate) {
    return SShield.getSelectWherePhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), predicate);
  }

  public SelectGroupByPhase<Tuple1<A>> groupBy(final ComparableExpression... columns) {
    return SShield.getSelectGroupByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), columns);
  }

  public LockableSelectOrderByPhase<Tuple1<A>> orderBy(final OrderingTerm... orderingTerms) {
    return SShield.getSelectOrderByPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata),
        orderingTerms);
  }

  public LockableSelectOffsetPhase<Tuple1<A>> offset(final int offset) {
    return SShield.getSelectOffsetPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), offset);
  }

  public LockableSelectLimitPhase<Tuple1<A>> limit(final int limit) {
    return SShield.getSelectLimitPhase(this.metadata.getContext(), new TuplesSelectObject<>(this.metadata), limit);
  }

  // execute

  public List<Tuple1<A>> execute() {
    CombinedSelectObject<Tuple1<A>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.execute(this.metadata.getContext());
  }

  public Cursor<Tuple1<A>> executeCursor() throws SQLException {
    CombinedSelectObject<Tuple1<A>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext());
  }

  public Cursor<Tuple1<A>> executeCursor(int fetchSize) throws SQLException {
    CombinedSelectObject<Tuple1<A>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeCursor(this.metadata.getContext(), fetchSize);
  }

  public Tuple1<A> executeOne() throws SQLException {
    CombinedSelectObject<Tuple1<A>> combined = new CombinedSelectObject<>(new TuplesSelectObject<>(this.metadata));
    return combined.executeOne(this.metadata.getContext());
  }

}
