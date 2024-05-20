package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.QueryWriter;

public interface ReferenceableExpression extends ResultSetColumn {

  void renderTo(QueryWriter w);

  String getName();

}
