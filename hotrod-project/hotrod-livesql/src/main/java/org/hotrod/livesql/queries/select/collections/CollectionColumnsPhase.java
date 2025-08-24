package org.hotrod.livesql.queries.select.collections;

import java.util.List;

import org.hotrod.livesql.expressions.LiveSQLExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.TableOrView;

public class CollectionColumnsPhase extends Collection {

  public CollectionColumnsPhase(String property, TableOrView<?> tableOrView, List<LiveSQLExpression> liveSQLExpressions,
      List<EntityColumn> parentIds) {
    super(property, tableOrView, liveSQLExpressions, parentIds);
  }

}
