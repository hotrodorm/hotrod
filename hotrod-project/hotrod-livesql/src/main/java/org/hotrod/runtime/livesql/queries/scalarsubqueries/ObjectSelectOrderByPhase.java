package org.hotrod.runtime.livesql.queries.scalarsubqueries;

import java.util.Arrays;

import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.expressions.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject;

public class ObjectSelectOrderByPhase extends ObjectSelectExpression {

  // Constructor

  ObjectSelectOrderByPhase(final AbstractSelectObject<Row> select, final OrderingTerm... orderingTerms) {
    super(select);
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
  }

  // Same stage

  // Next stages

  public ObjectSelectOffsetPhase offset(final int offset) {
    return new ObjectSelectOffsetPhase(this.select, offset);
  }

  public ObjectSelectLimitPhase limit(final int limit) {
    return new ObjectSelectLimitPhase(this.select, limit);
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
