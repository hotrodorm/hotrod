package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.Available;
import org.hotrod.runtime.livesql.dialects.Const;
import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.OrderingTerm;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.IndividualSelectPhase;
import org.hotrod.runtime.livesql.queries.subqueries.Subquery;

public class NonLockableSelectFromPhase<R> extends IndividualSelectPhase<R> {

  // Constructor

  NonLockableSelectFromPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final TableExpression t) {
    super(context, combined);
    this.getLastSelect().setBaseTableExpression(t);
  }

  // This phase

  public NonLockableSelectFromPhase<R> join(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.getLastSelect().addJoin(new InnerJoin(tableViewOrSubquery, on));
    return this;
  }

  public NonLockableSelectFromPhase<R> join(final TableExpression tableViewOrSubquery, final Column... using) {
    this.getLastSelect().addJoin(new InnerJoin(tableViewOrSubquery, using));
    return this;
  }

  public NonLockableSelectFromPhase<R> leftJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.getLastSelect().addJoin(new LeftOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public NonLockableSelectFromPhase<R> leftJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.getLastSelect().addJoin(new LeftOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public NonLockableSelectFromPhase<R> rightJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.getLastSelect().addJoin(new RightOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public NonLockableSelectFromPhase<R> rightJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.getLastSelect().addJoin(new RightOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public NonLockableSelectFromPhase<R> fullJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.getLastSelect().addJoin(new FullOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public NonLockableSelectFromPhase<R> fullJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.getLastSelect().addJoin(new FullOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public NonLockableSelectFromPhase<R> crossJoin(final TableExpression tableViewOrSubquery) {
    this.getLastSelect().addJoin(new CrossJoin(tableViewOrSubquery));
    return this;
  }

  public NonLockableSelectFromPhase<R> naturalJoin(final TableExpression tableViewOrSubquery) {
    this.getLastSelect().addJoin(new NaturalInnerJoin(tableViewOrSubquery));
    return this;
  }

  public NonLockableSelectFromPhase<R> naturalLeftJoin(final TableExpression tableViewOrSubquery) {
    this.getLastSelect().addJoin(new NaturalLeftOuterJoin(tableViewOrSubquery));
    return this;
  }

  public NonLockableSelectFromPhase<R> naturalRightJoin(final TableExpression tableViewOrSubquery) {
    this.getLastSelect().addJoin(new NaturalRightOuterJoin(tableViewOrSubquery));
    return this;
  }

  public NonLockableSelectFromPhase<R> naturalFullJoin(final TableExpression tableViewOrSubquery) {
    this.getLastSelect().addJoin(new NaturalFullOuterJoin(tableViewOrSubquery));
    return this;
  }

  public NonLockableSelectFromPhase<R> joinLateral(final Subquery subquery) {
    this.getLastSelect().addJoin(new JoinLateral(subquery));
    return this;
  }

  public NonLockableSelectFromPhase<R> leftJoinLateral(final Subquery subquery) {
    this.getLastSelect().addJoin(new LeftJoinLateral(subquery));
    return this;
  }

  @Available(engine = Const.GENERIC, since = Const.ALL)
  @Available(engine = Const.HYPERSQL, since = Const.HS2)
  public NonLockableSelectFromPhase<R> unionJoin(final TableExpression tableViewOrSubquery) {
    this.getLastSelect().addJoin(new UnionJoin(tableViewOrSubquery));
    return this;
  }

  @Available(engine = Const.POSTGRESQL, since = Const.PG15)
  public NonLockableSelectFromPhase<R> unionPGJoin(final TableExpression tableViewOrSubquery) {
    this.getLastSelect().addJoin(new UnionJoin(tableViewOrSubquery));
    return this;
  }

  // Next phases

  public NonLockableSelectWherePhase<R> where(final Predicate predicate) {
    return new NonLockableSelectWherePhase<R>(this.context, this.combined, predicate);
  }

  public SelectGroupByPhase<R> groupBy(final ComparableExpression... columns) {
    return new SelectGroupByPhase<R>(this.context, this.combined, columns);
  }

  public SelectOrderByPhase<R> orderBy(final OrderingTerm... orderingTerms) {
    return new SelectOrderByPhase<R>(this.context, this.combined, orderingTerms);
  }

  public SelectOffsetPhase<R> offset(final int offset) {
    return new SelectOffsetPhase<R>(this.context, this.combined, offset);
  }

  public SelectLimitPhase<R> limit(final int limit) {
    return new SelectLimitPhase<R>(this.context, this.combined, limit);
  }

}
