package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public class FullOuterJoin extends PredicatedJoin {

  FullOuterJoin(final TableOrView table, final Predicate predicate) {
    super(table, predicate);
  }

  FullOuterJoin(final TableOrView table, final Column... using) {
    super(table, using);
  }

}
