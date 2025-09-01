package org.hotrod.runtime.livesql.ordering;

import org.hotrod.runtime.livesql.QueryWriter;

public interface OrderingTerm {

  void renderTo(QueryWriter w);

}
