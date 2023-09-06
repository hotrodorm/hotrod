package org.hotrod.runtime.livesql.queries.scalarsubqueries;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.queries.ctes.CTE;
import org.hotrod.runtime.livesql.queries.select.SelectObject;
import org.hotrod.runtime.livesql.queries.select.TableExpression;

public class DateTimeSelectColumnsPhase extends DateTimeSelectExpression {

  // Constructor

  public DateTimeSelectColumnsPhase(final List<CTE> ctes, final boolean distinct, final DateTimeExpression expression) {
    super(new SelectObject<Row>(ctes, distinct, true, Arrays.asList(expression).stream().collect(Collectors.toList())));
  }

  // Next stages

  public DateTimeSelectFromPhase from(final TableExpression tableViewOrSubquery) {
    return new DateTimeSelectFromPhase(this.select, tableViewOrSubquery);
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

  // Validation

//  @Override
//  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
//    this.select.validateTableReferences(tableReferences, ag);
//  }
//
//  @Override
//  public void designateAliases(final AliasGenerator ag) {
//    this.select.assignNonDeclaredAliases(ag);
//  }

//  // CombinableSelect
//
//  @Override
//  public void setParent(final AbstractSelect<R> parent) {
//    this.select.setParent(parent);
//  }
//
//  @Override
//  public String getPreview() {
//    return this.select.getPreview();
//  }
//
//  @Override
//  public List<ResultSetColumn> listColumns() throws IllegalAccessException {
//    return this.select.listColumns();
//  }

}
