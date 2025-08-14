package org.hotrod.livesql.queries.select.tuples;

import java.util.List;

import org.hotrod.livesql.expressions.SQLExpression;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.util.ToString;

public abstract class NestedTuples extends SQLExpression {

  protected String property;
  protected TableOrView<?> tableOrView;
  protected List<SQLExpression> sqlExpressions;

  public NestedTuples(String property, TableOrView<?> tableOrView, List<SQLExpression> sqlExpressions) {
    super();
    this.property = property;
    this.tableOrView = tableOrView;
    this.sqlExpressions = sqlExpressions;
  }

  // Extends SQLExpression

  @Override
  protected void log(ToString t) {
  }

}
