package org.hotrod.runtime.livesql.metadata;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;

public abstract class WrappingColumn extends ResultSetColumn {

  protected abstract List<Expression> unwrap();

}
