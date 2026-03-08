package org.hotrod.livesql.queries.scalarsubqueries;

import org.hotrod.dynamicsql.Row;
import org.hotrod.livesql.queries.select.FlatSelectObject;

public class BinarySelectOffsetPhase extends BinarySelectExpression {

  // Constructor

  BinarySelectOffsetPhase(final FlatSelectObject<Row> select, final int offset) {
    super(select);
    this.select.setOffset(offset);
  }

  // Next stages

  public BinarySelectLimitPhase limit(final int limit) {
    return new BinarySelectLimitPhase(this.select, limit);
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
