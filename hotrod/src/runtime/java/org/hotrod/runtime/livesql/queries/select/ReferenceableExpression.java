package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;

public interface ReferenceableExpression extends ResultSetColumn {

  void renderTo(QueryWriter w);

}
