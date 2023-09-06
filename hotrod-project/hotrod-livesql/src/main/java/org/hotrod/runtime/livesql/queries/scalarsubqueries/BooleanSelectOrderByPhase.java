package org.hotrod.runtime.livesql.queries.scalarsubqueries;

import java.util.Arrays;

import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject;

public class BooleanSelectOrderByPhase extends BooleanSelectExpression {

  // Constructor

  BooleanSelectOrderByPhase(final AbstractSelectObject<Row> select, final OrderingTerm... orderingTerms) {
    super(select);
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
  }

  // Same stage

  // Next stages

  public BooleanSelectOffsetPhase offset(final int offset) {
    return new BooleanSelectOffsetPhase(this.select, offset);
  }

  public BooleanSelectLimitPhase limit(final int limit) {
    return new BooleanSelectLimitPhase(this.select, limit);
  }

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
