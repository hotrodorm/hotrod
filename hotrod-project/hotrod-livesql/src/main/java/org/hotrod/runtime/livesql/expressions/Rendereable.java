package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.TableReferences;

public interface Rendereable {

  void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

//  void designateAliases(AliasGenerator ag);

  void renderTo(QueryWriter w);

}
