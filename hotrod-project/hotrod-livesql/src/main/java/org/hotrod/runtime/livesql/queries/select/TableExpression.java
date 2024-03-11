package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Rendereable;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.metadata.Name;

public interface TableExpression extends Rendereable {

  Name getName();

  List<ResultSetColumn> getColumns() throws IllegalAccessException;

}
