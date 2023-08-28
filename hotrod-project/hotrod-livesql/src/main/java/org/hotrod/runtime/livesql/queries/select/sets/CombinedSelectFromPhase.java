package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.Available;
import org.hotrod.runtime.livesql.dialects.Const;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.ordering.CombinedOrderingTerm;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.CrossJoin;
import org.hotrod.runtime.livesql.queries.select.ExecutableSelect;
import org.hotrod.runtime.livesql.queries.select.FullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.InnerJoin;
import org.hotrod.runtime.livesql.queries.select.JoinLateral;
import org.hotrod.runtime.livesql.queries.select.LeftJoinLateral;
import org.hotrod.runtime.livesql.queries.select.LeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalFullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalInnerJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalLeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalRightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.RightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.hotrod.runtime.livesql.queries.select.TableExpression;
import org.hotrod.runtime.livesql.queries.select.UnionJoin;
import org.hotrod.runtime.livesql.queries.subqueries.Subquery;

public class CombinedSelectFromPhase<R> implements ExecutableSelect<R> {

  // Properties

  private Select<R> select;

  // Constructor

  CombinedSelectFromPhase(final Select<R> select, final TableExpression t) {
    this.select = select;
    this.select.setBaseTableExpression(t);
  }

  // This stage

  public CombinedSelectFromPhase<R> join(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new InnerJoin(tableViewOrSubquery, on));
    return this;
  }

  public CombinedSelectFromPhase<R> join(final TableExpression tableViewOrSubquery, final Column... using) {
    this.select.addJoin(new InnerJoin(tableViewOrSubquery, using));
    return this;
  }

  public CombinedSelectFromPhase<R> leftJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new LeftOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public CombinedSelectFromPhase<R> leftJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.select.addJoin(new LeftOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public CombinedSelectFromPhase<R> rightJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new RightOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public CombinedSelectFromPhase<R> rightJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.select.addJoin(new RightOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public CombinedSelectFromPhase<R> fullJoin(final TableExpression tableViewOrSubquery, final Predicate on) {
    this.select.addJoin(new FullOuterJoin(tableViewOrSubquery, on));
    return this;
  }

  public CombinedSelectFromPhase<R> fullJoin(final TableExpression tableViewOrSubquery, final Column... using) {
    this.select.addJoin(new FullOuterJoin(tableViewOrSubquery, using));
    return this;
  }

  public CombinedSelectFromPhase<R> crossJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new CrossJoin(tableViewOrSubquery));
    return this;
  }

  public CombinedSelectFromPhase<R> naturalJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalInnerJoin(tableViewOrSubquery));
    return this;
  }

  public CombinedSelectFromPhase<R> naturalLeftJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalLeftOuterJoin(tableViewOrSubquery));
    return this;
  }

  public CombinedSelectFromPhase<R> naturalRightJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalRightOuterJoin(tableViewOrSubquery));
    return this;
  }

  public CombinedSelectFromPhase<R> naturalFullJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new NaturalFullOuterJoin(tableViewOrSubquery));
    return this;
  }

  public CombinedSelectFromPhase<R> joinLateral(final Subquery subquery) {
    this.select.addJoin(new JoinLateral(subquery));
    return this;
  }

  public CombinedSelectFromPhase<R> leftJoinLateral(final Subquery subquery) {
    this.select.addJoin(new LeftJoinLateral(subquery));
    return this;
  }

  @Available(engine = Const.GENERIC, since = Const.ALL)
  @Available(engine = Const.HYPERSQL, since = Const.HS2)
  public CombinedSelectFromPhase<R> unionJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new UnionJoin(tableViewOrSubquery));
    return this;
  }

  @Available(engine = Const.POSTGRESQL, since = Const.PG15)
  public CombinedSelectFromPhase<R> unionPGJoin(final TableExpression tableViewOrSubquery) {
    this.select.addJoin(new UnionJoin(tableViewOrSubquery));
    return this;
  }

  // Next stages

  public CombinedSelectWherePhase<R> where(final Predicate predicate) {
    return new CombinedSelectWherePhase<R>(this.select, predicate);
  }

  public CombinedSelectGroupByPhase<R> groupBy(final Expression... columns) {
    return new CombinedSelectGroupByPhase<R>(this.select, columns);
  }

  public CombinedSelectOrderByPhase<R> orderBy(final CombinedOrderingTerm... orderingTerms) {
    return new CombinedSelectOrderByPhase<R>(this.select, orderingTerms);
  }

  public CombinedSelectOffsetPhase<R> offset(final int offset) {
    return new CombinedSelectOffsetPhase<R>(this.select, offset);
  }

  public CombinedSelectLimitPhase<R> limit(final int limit) {
    return new CombinedSelectLimitPhase<R>(this.select, limit);
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
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.select.validateTableReferences(tableReferences, ag);
  }

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
