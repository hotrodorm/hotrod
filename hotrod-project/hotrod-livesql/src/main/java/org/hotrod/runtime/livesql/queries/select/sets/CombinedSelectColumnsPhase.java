package org.hotrod.runtime.livesql.queries.select.sets;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.select.Select;
import org.hotrod.runtime.livesql.queries.select.SelectObject;
import org.hotrod.runtime.livesql.queries.select.TableExpression;

public class CombinedSelectColumnsPhase<R> implements Select<R> {

  // Properties

  private LiveSQLContext context;
  private SelectObject<R> select;

  // Constructor

  public CombinedSelectColumnsPhase(final LiveSQLContext context, final List<CTE> ctes, final boolean distinct,
      final ResultSetColumn... resultSetColumns) {
    this.context = context;
    this.select = new SelectObject<R>(ctes, distinct, false);
    this.select.setResultSetColumns(Arrays.asList(resultSetColumns).stream().collect(Collectors.toList()));
  }

  // Next stages

  public CombinedSelectFromPhase<R> from(final TableExpression tableViewOrSubquery) {
    return new CombinedSelectFromPhase<R>(this.context, this.select, tableViewOrSubquery);
  }

  // Set operations

  // .select() .selectDistinct()
  public CombinedSelectLinkingPhase<R> union() {
    UnionOperator<R> op = new UnionOperator<R>();
    op.add(this.select);
    return new CombinedSelectLinkingPhase<R>(op, this.context);
  }

//  // .select() .selectDistinct()
//  public CombinedSelectLinkingPhase<R> intersect() {
//    IntersectOperator<R> op = new IntersectOperator<R>(this.select);
//    return new CombinedSelectLinkingPhase<R>(op);
//  }
//
//  // .union()
//  public CombinedSelectPhase<R> union(final ExecutableSelect<R> select) {
//    UnionOperator<R> op = new UnionOperator<R>(this.select);
//    op.setRight(select.getSelect());
//    return new CombinedSelectPhase<R>(op);
//  }

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

  // Execute

  public List<R> execute() {
    return this.select.execute(this.context);
  }

  @Override
  public Cursor<R> executeCursor() {
    return this.select.executeCursor(this.context);
  }

  // Utilities

  @Override
  public String getPreview() {
    return this.select.getPreview(this.context);
  }

  // Executable Select

  @Override
  public SelectObject<R> getSelect() {
    return this.select;
  }

}
