package org.hotrod.runtime.livesql;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;

public interface ExecutableSelect {

  void renderTo(final QueryWriter w);

  List<Map<String, Object>> execute();

  void gatherAliases(final AliasGenerator ag);

  void designateAliases(final AliasGenerator ag);

}
