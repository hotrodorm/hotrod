package org.hotrod.livesql.queries.select;

import java.util.Arrays;
import java.util.List;

import org.hotrod.livesql.exceptions.InvalidLiveSQLClauseException;
import org.hotrod.livesql.metadata.EntityColumnMetadata;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;

public abstract class PredicatedJoin extends Join {

  private Predicate predicate;
  private List<EntityColumnMetadata> using;

  public PredicatedJoin(final TableExpression tableExpression, final Predicate predicate) {
    super(tableExpression);
    if (predicate == null) {
      throw new InvalidLiveSQLClauseException("The join predicate cannot be null");
    }
    this.predicate = predicate;
    this.using = null;
  }

  public PredicatedJoin(final TableExpression tableExpression, final EntityColumnMetadata... using) {
    super(tableExpression);
    if (using.length == 0) {
      throw new InvalidLiveSQLClauseException(
          "The USING columns for the join equality must include at least one column.");
    }
    this.predicate = null;
    this.using = Arrays.asList(using);
  }

  public Predicate getJoinPredicate() {
    return this.predicate;
  }

  public List<EntityColumnMetadata> getUsingColumns() {
    return this.using;
  }

}
