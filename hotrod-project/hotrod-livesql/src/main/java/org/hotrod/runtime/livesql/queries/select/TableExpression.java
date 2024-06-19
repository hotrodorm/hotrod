package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Rendereable;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.metadata.Name;

public abstract class TableExpression implements Rendereable {

  public abstract Name getName();

  public abstract List<ResultSetColumn> getColumns() throws IllegalAccessException;

  protected abstract void computeQueryColumns();
}
