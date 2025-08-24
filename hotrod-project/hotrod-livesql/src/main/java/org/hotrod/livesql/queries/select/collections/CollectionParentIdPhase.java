package org.hotrod.livesql.queries.select.collections;

import java.util.List;

import org.hotrod.livesql.expressions.LiveSQLExpression;
import org.hotrod.livesql.metadata.EntityColumn;
import org.hotrod.livesql.metadata.TableOrView;

public class CollectionParentIdPhase extends Collection {

  public CollectionParentIdPhase(String property, TableOrView<?> tableOrView,
      List<LiveSQLExpression> liveSQLExpressions, List<EntityColumn> parentId) {
    super(property, tableOrView, liveSQLExpressions, parentId);
  }

}
