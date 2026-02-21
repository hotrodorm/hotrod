package org.hotrod.livesql.queries.select.collections;

import java.util.List;

import org.hotrod.livesql.expressions.LiveSQLExpression;
import org.hotrod.livesql.metadata.EntityColumnMetadata;
import org.hotrod.livesql.metadata.TableOrView;

public class CollectionPhase extends Collection {

  public CollectionPhase(String property, TableOrView<?> tableOrView) {
    super(property, tableOrView, null, null);
  }

  public CollectionParentIdPhase parentIds(List<EntityColumnMetadata> ids) {
    return new CollectionParentIdPhase(super.property, super.tableOrView, super.liveSQLExpressions, ids);
  }

  public CollectionColumnsPhase columns(List<LiveSQLExpression> liveSQLExpressions) {
    return new CollectionColumnsPhase(super.property, super.tableOrView, liveSQLExpressions, super.parentId);
  }

}
