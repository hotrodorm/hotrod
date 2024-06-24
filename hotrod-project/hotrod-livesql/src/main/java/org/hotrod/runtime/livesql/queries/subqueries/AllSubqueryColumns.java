package org.hotrod.runtime.livesql.queries.subqueries;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.metadata.WrappingColumn;

public class AllSubqueryColumns extends WrappingColumn {

  private Subquery subquery;

  protected AllSubqueryColumns(final Subquery subquery) {
    this.subquery = subquery;
  }

//  @SuppressWarnings("unused")
//  private List<ResultSetColumn> listColumns() throws IllegalAccessException {
//    List<ResultSetColumn> cols = this.subquery.getSelect().listColumns();
//    List<ResultSetColumn> fcols = new ArrayList<>();
//    for (ResultSetColumn c : cols) {
////      System.out.println("##> c=" + c);
//      fcols.add(SubqueryUtil.castPersistenceColumnAsSubqueryColumn(this.subquery, c));
//    }
//    return fcols;
//  }

//  @Override
//  public void renderTo(QueryWriter w) {
//    throw new UnsupportedOperationException("The columns must be expanded and rendered separately");
//  }

  @Override
  protected List<Expression> unwrap() {
    return this.subquery.assembleColumns();
  }

}
