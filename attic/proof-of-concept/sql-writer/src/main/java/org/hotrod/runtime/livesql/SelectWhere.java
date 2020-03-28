package org.hotrod.runtime.livesql;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.predicates.PredicateBuilder;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class SelectWhere implements ExecutableSelect {

  // Properties

  private AbstractSelect select;
  private PredicateBuilder predicateBuilder;

  // Constructors

  SelectWhere(final AbstractSelect select, final Predicate predicate) {
    this.select = select;
    this.predicateBuilder = new PredicateBuilder(predicate);
    this.select.setWhereCondition(this.predicateBuilder.getAssembled());
  }

  // Same stage

  public SelectWhere and(final Predicate predicate) {
    this.predicateBuilder.and(predicate);
    this.select.setWhereCondition(this.predicateBuilder.getAssembled());
    return this;
  }

  public SelectWhere or(final Predicate predicate) {
    this.predicateBuilder.or(predicate);
    this.select.setWhereCondition(this.predicateBuilder.getAssembled());
    return this;
  }

  // Next stages

  public SelectGroupBy groupBy(final Expression<?>... columns) {
    return new SelectGroupBy(this.select, columns);
  }

  public SelectOrderBy orderBy(final OrderingTerm... orderingTerms) {
    return new SelectOrderBy(this.select, orderingTerms);
  }

  public SelectOffset offset(final int offset) {
    return new SelectOffset(this.select, offset);
  }

  public SelectLimit limit(final int limit) {
    return new SelectLimit(this.select, limit);
  }

  // Rendering

  @Override
  public void renderTo(final QueryWriter w) {
    this.select.renderTo(w);
  }

  // Execute

  public List<Map<String, Object>> execute() {
    return this.select.execute();
  }

}
