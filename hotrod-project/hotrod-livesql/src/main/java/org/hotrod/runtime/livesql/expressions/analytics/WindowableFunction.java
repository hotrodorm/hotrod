package org.hotrod.runtime.livesql.expressions.analytics;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public interface WindowableFunction {

  void renderTo(final QueryWriter w);

}
