package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.Arrays;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.CombinedOrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.SelectObject;

public class CombinedSelectGroupByPhase<R> extends AbstractSelectPhase<R> {

  // Constructor

  CombinedSelectGroupByPhase(final LiveSQLContext context, final SelectObject<R> select,
      final Expression... expressions) {
    super(context, select);
    this.select.setGroupBy(Arrays.asList(expressions));
  }

  // Next stages

  public CombinedSelectHavingPhase<R> having(final Predicate predicate) {
    return new CombinedSelectHavingPhase<R>(this.context, this.select, predicate);
  }

  public CombinedSelectOrderByPhase<R> orderBy(final CombinedOrderingTerm... orderingTerms) {
    return new CombinedSelectOrderByPhase<R>(this.context, this.select, orderingTerms);
  }

  public CombinedSelectOffsetPhase<R> offset(final int offset) {
    return new CombinedSelectOffsetPhase<R>(this.context, this.select, offset);
  }

  public CombinedSelectLimitPhase<R> limit(final int limit) {
    return new CombinedSelectLimitPhase<R>(this.context, this.select, limit);
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

}