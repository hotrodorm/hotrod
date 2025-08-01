package org.hotrod.livesql.queries.scalarsubqueries;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.expressions.character.GeneralCharExpression;
import org.hotrod.livesql.queries.ctes.CTE;
import org.hotrod.livesql.queries.select.UnarySelectObject;
import org.hotrod.livesql.queries.select.TableExpression;

public class CharSelectColumnsPhase extends CharSelectExpression {

  // Constructor

  public CharSelectColumnsPhase(final List<CTE> ctes, final boolean distinct, final GeneralCharExpression expression) {
    super(new UnarySelectObject<Row>(ctes, distinct, true, Arrays.asList(expression).stream().collect(Collectors.toList())));
  }

  // Next stages

  public CharSelectFromPhase from(final TableExpression tableViewOrSubquery) {
    return new CharSelectFromPhase(this.select, tableViewOrSubquery);
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
