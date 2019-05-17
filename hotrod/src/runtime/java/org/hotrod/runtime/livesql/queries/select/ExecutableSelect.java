package org.hotrod.runtime.livesql.queries.select;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public interface ExecutableSelect {

  void renderTo(final QueryWriter w);

  List<Map<String, Object>> execute();

  void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag);

  void designateAliases(final AliasGenerator ag);

}
