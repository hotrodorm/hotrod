package org.hotrod.runtime.livesql;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class RightOuterJoin extends PredicatedJoin {

  RightOuterJoin(final TableOrView table, final Predicate on) {
    super(table, on);
  }

}
