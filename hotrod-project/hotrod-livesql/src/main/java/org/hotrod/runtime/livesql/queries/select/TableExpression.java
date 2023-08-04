package org.hotrod.runtime.livesql.queries.select;

import java.util.List;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public interface TableExpression {

  String getAlias();

  void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

  void designateAliases(AliasGenerator ag);

  void renderTo(QueryWriter w, LiveSQLDialect dialect);

  List<ResultSetColumn> getColumns() throws IllegalAccessException;

}
