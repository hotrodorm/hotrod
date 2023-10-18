package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.ctes.CTE;

@Deprecated
public class EnclosedSelectObject<R> extends AbstractSelectObject<R> {

  private AbstractSelectObject<R> select;

  public EnclosedSelectObject(final List<CTE> ctes, final boolean distinct, final AbstractSelectObject<R> select) {
    super(ctes, distinct);
    this.select = select;
  }

  @Override
  protected void writeColumns(final QueryWriter w, final TableExpression baseTableExpression, final List<Join> joins) {
    w.write("\n(\n");
    w.enterLevel();
    this.select.renderTo(w);
    w.exitLevel();
    w.write("\n)\n");
  }

//  // Set operations
//
//  public SelectHavingPhase<R> union(final CombinableSelect<R> select) {
//    this.select.setCombinedSelect(SetOperation.UNION, select);
//    return new SelectHavingPhase<R>(this.select, null);
//  }
//
//  public SelectHavingPhase<R> unionAll(final CombinableSelect<R> select) {
//    this.select.setCombinedSelect(SetOperation.UNION_ALL, select);
//    return new SelectHavingPhase<R>(this.select, null);
//  }
//
//  public SelectHavingPhase<R> intersect(final CombinableSelect<R> select) {
//    this.select.setCombinedSelect(SetOperation.INTERSECT, select);
//    return new SelectHavingPhase<R>(this.select, null);
//  }
//
//  public SelectHavingPhase<R> intersectAll(final CombinableSelect<R> select) {
//    this.select.setCombinedSelect(SetOperation.INTERSECT_ALL, select);
//    return new SelectHavingPhase<R>(this.select, null);
//  }
//
//  public SelectHavingPhase<R> except(final CombinableSelect<R> select) {
//    this.select.setCombinedSelect(SetOperation.EXCEPT, select);
//    return new SelectHavingPhase<R>(this.select, null);
//  }
//
//  public SelectHavingPhase<R> exceptAll(final CombinableSelect<R> select) {
//    this.select.setCombinedSelect(SetOperation.EXCEPT_ALL, select);
//    return new SelectHavingPhase<R>(this.select, null);
//  }

  @Override
  public List<ResultSetColumn> listColumns() {
    return this.select.listColumns();
  }

}
