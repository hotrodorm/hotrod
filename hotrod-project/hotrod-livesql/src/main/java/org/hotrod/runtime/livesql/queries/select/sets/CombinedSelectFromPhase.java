package org.hotrod.runtime.livesql.queries.select.sets;

import org.hotrod.runtime.livesql.Available;
import org.hotrod.runtime.livesql.dialects.Const;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.CrossJoin;
import org.hotrod.runtime.livesql.queries.select.FullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.InnerJoin;
import org.hotrod.runtime.livesql.queries.select.JoinLateral;
import org.hotrod.runtime.livesql.queries.select.LeftJoinLateral;
import org.hotrod.runtime.livesql.queries.select.LeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalFullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalInnerJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalLeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalRightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.RightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.TableExpression;
import org.hotrod.runtime.livesql.queries.select.UnionJoin;
import org.hotrod.runtime.livesql.queries.subqueries.Subquery;

public class CombinedSelectFromPhase<R> extends CombinedSelectPhase<R> {

  // Constructor

  CombinedSelectFromPhase(final LiveSQLContext context, final CombinedSelectObject<R> combined,
      final TableExpression t) {
    super(context, combined);
    this.getLastSelect().setBaseTableExpression(t);
  }

  // This phase

  public CombinedSelectFromPhase<R> join(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.getLastSelect().addJoin(new InnerJoin(tableViewOrSubquery, on));
    return this;
  }

  public CombinedSelectFromPhase<R> join(final TableExpression tableViewOrSubquery, final Column... using) {
    this.getLastSelect().addJoin(new InnerJoin(tableViewOrSubquery, using));
    return this;
  }

  public CombinedSelectFromPhase<R> leftJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.getLastSelect().addJoin(new LeftOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public CombinedSelectFromPhase<R> leftJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.getLastSelect().addJoin(new LeftOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public CombinedSelectFromPhase<R> rightJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.getLastSelect().addJoin(new RightOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public CombinedSelectFromPhase<R> rightJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.getLastSelect().addJoin(new RightOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public CombinedSelectFromPhase<R> fullJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.getLastSelect().addJoin(new FullOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public CombinedSelectFromPhase<R> fullJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.getLastSelect().addJoin(new FullOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public CombinedSelectFromPhase<R> crossJoin(final TableExpression tableViewOrSubquery) {
    this.getLastSelect().addJoin(new CrossJoin(tableViewOrSubquery));
    return this;
  }

  public CombinedSelectFromPhase<R> naturalJoin(final TableExpression tableViewOrSubquery) {
    this.getLastSelect().addJoin(new NaturalInnerJoin(tableViewOrSubquery));
    return this;
  }

  public CombinedSelectFromPhase<R> naturalLeftJoin(final TableExpression tableViewOrSubquery) {
    this.getLastSelect().addJoin(new NaturalLeftOuterJoin(tableViewOrSubquery));
    return this;
  }

  public CombinedSelectFromPhase<R> naturalRightJoin(final TableExpression tableViewOrSubquery) {
    this.getLastSelect().addJoin(new NaturalRightOuterJoin(tableViewOrSubquery));
    return this;
  }

  public CombinedSelectFromPhase<R> naturalFullJoin(final TableExpression tableViewOrSubquery) {
    this.getLastSelect().addJoin(new NaturalFullOuterJoin(tableViewOrSubquery));
    return this;
  }

  public CombinedSelectFromPhase<R> joinLateral(final Subquery subquery) {
    this.getLastSelect().addJoin(new JoinLateral(subquery));
    return this;
  }

  public CombinedSelectFromPhase<R> leftJoinLateral(final Subquery subquery) {
    this.getLastSelect().addJoin(new LeftJoinLateral(subquery));
    return this;
  }

  @Available(engine = Const.GENERIC, since = Const.ALL)
  @Available(engine = Const.HYPERSQL, since = Const.HS2)
  public CombinedSelectFromPhase<R> unionJoin(final TableExpression tableViewOrSubquery) {
    this.getLastSelect().addJoin(new UnionJoin(tableViewOrSubquery));
    return this;
  }

  @Available(engine = Const.POSTGRESQL, since = Const.PG15)
  public CombinedSelectFromPhase<R> unionPGJoin(final TableExpression tableViewOrSubquery) {
    this.getLastSelect().addJoin(new UnionJoin(tableViewOrSubquery));
    return this;
  }

  // Next phases

  public CombinedSelectWherePhase<R> where(final Predicate predicate) {
    return new CombinedSelectWherePhase<R>(this.context, this.combined, predicate);
  }

  public CombinedSelectGroupByPhase<R> groupBy(final Expression... columns) {
    return new CombinedSelectGroupByPhase<R>(this.context, this.combined, columns);
  }

}
