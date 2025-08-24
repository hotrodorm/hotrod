package org.hotrod.livesql.queries.select.collections;

import java.util.List;

import org.hotrod.livesql.expressions.LiveSQLExpression;
import org.hotrod.livesql.metadata.TableOrView;

public abstract class Association extends CollectionsOperator {

  public Association(String property, TableOrView<?> tableOrView, List<LiveSQLExpression> liveSQLExpressions) {
    super(property, tableOrView, liveSQLExpressions);
  }

}
