package org.hotrod.runtime.livesql.queries.subqueries;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.util.SubqueryUtil;

public class AllSubqueryColumns implements ResultSetColumn {

  private Subquery subquery;

  protected AllSubqueryColumns(final Subquery subquery) {
    this.subquery = subquery;
  }

  @SuppressWarnings("unused")
  private List<ResultSetColumn> listColumns() throws IllegalAccessException {
    List<ResultSetColumn> cols = this.subquery.getSelect().listColumns();
    List<ResultSetColumn> fcols = new ArrayList<>();
    for (ResultSetColumn c : cols) {
//      System.out.println("##> c=" + c);
      fcols.add(SubqueryUtil.castPersistenceColumnAsSubqueryColumn(this.subquery, c));
    }
    return fcols;
  }

  @Override
  public void renderTo(QueryWriter w) {
    throw new UnsupportedOperationException("The columns must be expanded and rendered separately");
  }

  @Override
  public void validateTableReferences(TableReferences tableReferences, AliasGenerator ag) {
    this.subquery.validateTableReferences(tableReferences, ag);
  }

}
