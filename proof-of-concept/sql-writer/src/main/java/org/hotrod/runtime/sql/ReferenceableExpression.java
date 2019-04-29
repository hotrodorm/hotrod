package org.hotrod.runtime.sql;

import org.hotrod.runtime.sql.expressions.ResultSetColumn;

public interface ReferenceableExpression extends ResultSetColumn {

  void renderTo(QueryWriter w);

}
