package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public interface Rendereable {

//  void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

//  void designateAliases(AliasGenerator ag);

  void renderTo(QueryWriter w);

}
