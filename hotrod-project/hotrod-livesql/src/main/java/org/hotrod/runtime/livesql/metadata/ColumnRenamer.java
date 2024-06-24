package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.expressions.Expression;

public interface ColumnRenamer {

  String newName(Expression c);

}
