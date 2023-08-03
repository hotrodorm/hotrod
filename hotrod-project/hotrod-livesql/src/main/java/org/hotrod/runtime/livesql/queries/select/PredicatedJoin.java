package org.hotrod.runtime.livesql.queries.select;

import java.util.Arrays;
import java.util.List;

import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLClauseException;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.Column;

abstract class PredicatedJoin extends Join {

  private Predicate predicate;
  private List<Column> using;

  PredicatedJoin(final TableExpression tableExpression, final Predicate predicate) {
    super(tableExpression);
    if (predicate == null) {
      throw new InvalidLiveSQLClauseException("The join predicate cannot be null");
    }
    this.predicate = predicate;
    this.using = null;
  }

  PredicatedJoin(final TableExpression tableExpression, final Column... using) {
    super(tableExpression);
    if (using.length == 0) {
      throw new InvalidLiveSQLClauseException(
          "The USING columns for the join equality must include at least one column.");
    }
    this.predicate = null;
    this.using = Arrays.asList(using);
  }

  Predicate getJoinPredicate() {
    return this.predicate;
  }

  public List<Column> getUsingColumns() {
    return this.using;
  }

}
