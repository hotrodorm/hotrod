package org.hotrod.runtime.livesql.queries.scalarsubqueries;

import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject;

public class DateTimeSelectWherePhase extends DateTimeSelectExpression {

  // Constructors

  DateTimeSelectWherePhase(final AbstractSelectObject<Row> select, final Predicate predicate) {
    super(select);
    this.select.setWhereCondition(predicate);
  }

  // Next stages

  public DateTimeSelectGroupByPhase groupBy(final ComparableExpression... columns) {
    return new DateTimeSelectGroupByPhase(this.select, columns);
  }

  public DateTimeSelectOrderByPhase orderBy(final OrderingTerm... orderingTerms) {
    return new DateTimeSelectOrderByPhase(this.select, orderingTerms);
  }

  public DateTimeSelectOffsetPhase offset(final int offset) {
    return new DateTimeSelectOffsetPhase(this.select, offset);
  }

  public DateTimeSelectLimitPhase limit(final int limit) {
    return new DateTimeSelectLimitPhase(this.select, limit);
  }

  // Set operations

  // public SelectHavingPhase union(final CombinableSelect select) {
  // this.select.setCombinedSelect(SetOperation.UNION, select);
  // return new SelectHavingPhase(this.select, null);
  // }
  //
  // public SelectHavingPhase unionAll(final CombinableSelect select) {
  // this.select.setCombinedSelect(SetOperation.UNION_ALL, select);
  // return new SelectHavingPhase(this.select, null);
  // }
  //
  // public SelectHavingPhase intersect(final CombinableSelect select) {
  // this.select.setCombinedSelect(SetOperation.INTERSECT, select);
  // return new SelectHavingPhase(this.select, null);
  // }
  //
  // public SelectHavingPhase intersectAll(final CombinableSelect select)
  // {
  // this.select.setCombinedSelect(SetOperation.INTERSECT_ALL, select);
  // return new SelectHavingPhase(this.select, null);
  // }
  //
  // public SelectHavingPhase except(final CombinableSelect select) {
  // this.select.setCombinedSelect(SetOperation.EXCEPT, select);
  // return new SelectHavingPhase(this.select, null);
  // }
  //
  // public SelectHavingPhase exceptAll(final CombinableSelect select) {
  // this.select.setCombinedSelect(SetOperation.EXCEPT_ALL, select);
  // return new SelectHavingPhase(this.select, null);
  // }

  // Execute

//  public List execute() {
//    return this.select.execute();
//  }
//
//  @Override
//  public Cursor executeCursor() {
//    return this.select.executeCursor();
//  }
//
//  // Validation
//
//  @Override
//  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
//    this.select.validateTableReferences(tableReferences, ag);
//  }
//
//  @Override
//  public void designateAliases(final AliasGenerator ag) {
//    this.select.assignNonDeclaredAliases(ag);
//  }
//
//  // CombinableSelect
//
//  @Override
//  public void setParent(final AbstractSelect parent) {
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
