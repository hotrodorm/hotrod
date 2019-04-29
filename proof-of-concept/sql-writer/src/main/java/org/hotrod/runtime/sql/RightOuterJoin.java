package org.hotrod.runtime.sql;

import org.hotrod.runtime.sql.expressions.predicates.Predicate;
import org.hotrod.runtime.sql.metadata.TableOrView;

public class RightOuterJoin extends PredicatedJoin {

  RightOuterJoin(final TableOrView table, final Predicate on) {
    super(table, on);
  }

}
