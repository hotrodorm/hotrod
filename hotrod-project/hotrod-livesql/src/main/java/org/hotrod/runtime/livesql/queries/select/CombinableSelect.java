package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public interface CombinableSelect<R> {

  void setParent(final AbstractSelect<R> parent);

  void renderTo(final QueryWriter w);

  void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag);

  void designateAliases(final AliasGenerator ag);

}
