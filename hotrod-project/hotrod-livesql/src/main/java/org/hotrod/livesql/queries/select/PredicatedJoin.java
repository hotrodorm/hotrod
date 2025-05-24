package org.hotrod.livesql.queries.select;

import java.util.Arrays;
import java.util.List;

import org.hotrod.livesql.exceptions.InvalidLiveSQLClauseException;
import org.hotrod.livesql.expressions.predicates.GeneralBooleanExpression;
import org.hotrod.livesql.metadata.EntityColumn;

public abstract class PredicatedJoin extends Join {

  private GeneralBooleanExpression predicate;
  private List<EntityColumn> using;

  public PredicatedJoin(final TableExpression tableExpression, final GeneralBooleanExpression predicate) {
    super(tableExpression);
    if (predicate == null) {
      throw new InvalidLiveSQLClauseException("The join predicate cannot be null");
    }
    this.predicate = predicate;
    this.using = null;
  }

  public PredicatedJoin(final TableExpression tableExpression, final EntityColumn... using) {
    super(tableExpression);
    if (using.length == 0) {
      throw new InvalidLiveSQLClauseException(
          "The USING columns for the join equality must include at least one column.");
    }
    this.predicate = null;
    this.using = Arrays.asList(using);
  }

  GeneralBooleanExpression getJoinPredicate() {
    return this.predicate;
  }

  public List<EntityColumn> getUsingColumns() {
    return this.using;
  }

}
