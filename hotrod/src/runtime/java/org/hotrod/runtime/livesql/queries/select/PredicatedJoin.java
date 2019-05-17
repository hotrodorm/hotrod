package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLClauseException;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;

abstract class PredicatedJoin extends Join {

  private Predicate predicate;

  PredicatedJoin(final TableOrView table, final Predicate predicate) {
    super(table);
    if (predicate == null) {
      throw new InvalidLiveSQLClauseException("The join predicate cannot be null");
    }
    this.predicate = predicate;
  }

  Predicate getJoinPredicate() {
    return this.predicate;
  }

}
