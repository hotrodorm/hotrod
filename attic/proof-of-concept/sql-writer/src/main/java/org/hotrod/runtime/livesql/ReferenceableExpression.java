package org.hotrod.runtime.livesql;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;

public interface ReferenceableExpression extends ResultSetColumn {

  void renderTo(QueryWriter w);

}
