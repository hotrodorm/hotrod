package org.hotrod.runtime.livesql.queries.scalarsubqueries;

import org.hotrod.runtime.livesql.Row;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject;

public class ObjectSelectOffsetPhase extends ObjectSelectExpression {

  // Constructor

  ObjectSelectOffsetPhase(final AbstractSelectObject<Row> select, final int offset) {
    super(select);
    this.select.setOffset(offset);
  }

  // Next stages

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
