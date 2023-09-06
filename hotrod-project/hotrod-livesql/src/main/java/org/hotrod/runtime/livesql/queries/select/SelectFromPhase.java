package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.Available;
import org.hotrod.runtime.livesql.dialects.Const;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.subqueries.Subquery;

public class SelectFromPhase<R> implements ExecutableSelect<R> {

  // Properties

  private Select<R> select;

  // Constructor

  SelectFromPhase(final Select<R> select, final TableExpression t) {
    this.select = select;
    this.select.setBaseTableExpression(t);
  }

  // This stage

  public SelectFromPhase<R> join(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new InnerJoin(tableViewOrSubquery, on));
    return this;
  }

  public SelectFromPhase<R> join(final TableExpression tableViewOrSubquery, final Column... using) {
    this.select.addJoin(new InnerJoin(tableViewOrSubquery, using));
    return this;
  }

  public SelectFromPhase<R> leftJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new LeftOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public SelectFromPhase<R> leftJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.select.addJoin(new LeftOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public SelectFromPhase<R> rightJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new RightOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public SelectFromPhase<R> rightJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.select.addJoin(new RightOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public SelectFromPhase<R> fullJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new FullOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public SelectFromPhase<R> fullJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.select.addJoin(new FullOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public SelectFromPhase<R> crossJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new CrossJoin(tableViewOrSubquery));
    return this;
  }

  public SelectFromPhase<R> naturalJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalInnerJoin(tableViewOrSubquery));
    return this;
  }

  public SelectFromPhase<R> naturalLeftJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalLeftOuterJoin(tableViewOrSubquery));
    return this;
  }

  public SelectFromPhase<R> naturalRightJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalRightOuterJoin(tableViewOrSubquery));
    return this;
  }

  public SelectFromPhase<R> naturalFullJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalFullOuterJoin(tableViewOrSubquery));
    return this;
  }

  public SelectFromPhase<R> joinLateral(final Subquery subquery) {
    this.select.addJoin(new JoinLateral(subquery));
    return this;
  }

  public SelectFromPhase<R> leftJoinLateral(final Subquery subquery) {
    this.select.addJoin(new LeftJoinLateral(subquery));
    return this;
  }

  @Available(engine = Const.GENERIC, since = Const.ALL)
  @Available(engine = Const.HYPERSQL, since = Const.HS2)
  public SelectFromPhase<R> unionJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new UnionJoin(tableViewOrSubquery));
    return this;
  }

  @Available(engine = Const.POSTGRESQL, since = Const.PG15)
  public SelectFromPhase<R> unionPGJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new UnionJoin(tableViewOrSubquery));
    return this;
  }

  // Next stages

  public SelectWherePhase<R> where(final Predicate predicate) {
    return new SelectWherePhase<R>(this.select, predicate);
  }

  public SelectGroupByPhase<R> groupBy(final Expression... columns) {
    return new SelectGroupByPhase<R>(this.select, columns);
  }

  public SelectOrderByPhase<R> orderBy(final OrderingTerm... orderingTerms) {
    return new SelectOrderByPhase<R>(this.select, orderingTerms);
  }

  public SelectOffsetPhase<R> offset(final int offset) {
    return new SelectOffsetPhase<R>(this.select, offset);
  }

  public SelectLimitPhase<R> limit(final int limit) {
    return new SelectLimitPhase<R>(this.select, limit);
  }

  // Set operations

  // public SelectHavingPhase<R> union(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.UNION, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> unionAll(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.UNION_ALL, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> intersect(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.INTERSECT, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> intersectAll(final CombinableSelect<R> select)
  // {
  // this.select.setCombinedSelect(SetOperation.INTERSECT_ALL, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> except(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.EXCEPT, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }
  //
  // public SelectHavingPhase<R> exceptAll(final CombinableSelect<R> select) {
  // this.select.setCombinedSelect(SetOperation.EXCEPT_ALL, select);
  // return new SelectHavingPhase<R>(this.select, null);
  // }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    this.select.renderTo(w);
  }

  // Execute

  public List<R> execute() {
    return this.select.execute();
  }

  @Override
  public Cursor<R> executeCursor() {
    return this.select.executeCursor();
  }

  // Validation

  @Override
  public String getPreview() {
    return this.select.getPreview();
  }

  @Override
  public List<ResultSetColumn> listColumns() throws IllegalAccessException {
    return this.select.listColumns();
  }

  // Executable Select

  @Override
  public Select<R> getSelect() {
    return this.select;
  }

}
