package org.hotrod.runtime.livesql;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class LeftOuterJoin extends PredicatedJoin {

  LeftOuterJoin(final TableOrView table, final Predicate predicate) {
    super(table, predicate);
  }

}
