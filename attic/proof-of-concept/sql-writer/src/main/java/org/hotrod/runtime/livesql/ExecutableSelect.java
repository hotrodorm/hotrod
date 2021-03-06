package org.hotrod.runtime.livesql;

import java.util.List;
import java.util.Map;

public interface ExecutableSelect {

  void renderTo(final QueryWriter w);

  List<Map<String, Object>> execute();

}
