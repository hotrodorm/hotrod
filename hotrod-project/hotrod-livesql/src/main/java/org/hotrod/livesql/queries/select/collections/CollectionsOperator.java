package org.hotrod.livesql.queries.select.collections;

import java.util.List;

import org.hotrod.livesql.expressions.LiveSQLExpression;
import org.hotrod.livesql.metadata.TableOrView;

public abstract class CollectionsOperator implements LiveSQLExpression {

  protected String property;
  protected TableOrView<?> tableOrView;
  protected List<LiveSQLExpression> liveSQLExpressions;

  public CollectionsOperator(String property, TableOrView<?> tableOrView, List<LiveSQLExpression> liveSQLExpressions) {
    super();
    this.property = property;
    this.tableOrView = tableOrView;
    this.liveSQLExpressions = liveSQLExpressions;
  }

}
