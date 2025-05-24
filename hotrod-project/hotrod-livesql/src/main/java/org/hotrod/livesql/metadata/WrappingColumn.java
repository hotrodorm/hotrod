package org.hotrod.livesql.metadata;

import java.util.List;

import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.ResultSetColumn;

public abstract class WrappingColumn extends ResultSetColumn {

  protected abstract List<Expression> unwrap();

}
