package org.hotrod.runtime.livesql.expressions;

import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public interface Rendereable {

  void validateTableReferences(TableReferences tableReferences, AliasGenerator ag);

//  void designateAliases(AliasGenerator ag);

  void renderTo(QueryWriter w);

}
