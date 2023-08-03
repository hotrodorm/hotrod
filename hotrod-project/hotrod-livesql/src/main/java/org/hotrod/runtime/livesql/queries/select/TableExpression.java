package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.dialects.LiveSQLDialect;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public interface TableExpression {

  String getAlias();

  void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

  void designateAliases(AliasGenerator ag);

  void renderTo(QueryWriter w, LiveSQLDialect dialect);

}
