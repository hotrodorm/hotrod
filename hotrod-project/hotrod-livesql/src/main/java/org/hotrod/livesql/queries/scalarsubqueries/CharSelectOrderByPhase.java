package org.hotrod.livesql.queries.scalarsubqueries;

import java.util.Arrays;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.select.UnarySelectObject;

public class CharSelectOrderByPhase extends CharSelectExpression {

  // Constructor

  CharSelectOrderByPhase(final UnarySelectObject<Row> select, final OrderingTerm... orderingTerms) {
    super(select);
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
  }

  // Same stage

  // Next stages

  public CharSelectOffsetPhase offset(final int offset) {
    return new CharSelectOffsetPhase(this.select, offset);
  }

  public CharSelectLimitPhase limit(final int limit) {
    return new CharSelectLimitPhase(this.select, limit);
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
