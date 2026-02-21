package org.hotrod.livesql.queries.select.collections;

import java.util.List;

import org.hotrod.livesql.expressions.LiveSQLExpression;
import org.hotrod.livesql.metadata.EntityColumnMetadata;
import org.hotrod.livesql.metadata.TableOrView;

public abstract class Collection extends CollectionsOperator {

  protected List<EntityColumnMetadata> parentId;

  public Collection(String property, TableOrView<?> tableOrView, List<LiveSQLExpression> liveSQLExpressions,
      List<EntityColumnMetadata> parentId) {
    super(property, tableOrView, liveSQLExpressions);
    this.parentId = parentId;
  }

}
