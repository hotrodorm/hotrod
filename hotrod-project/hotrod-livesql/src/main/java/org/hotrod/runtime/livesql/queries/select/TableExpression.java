package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.livesql.expressions.Rendereable;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;

public interface TableExpression extends Rendereable {

  String getName();

  List<ResultSetColumn> getColumns() throws IllegalAccessException;

}
