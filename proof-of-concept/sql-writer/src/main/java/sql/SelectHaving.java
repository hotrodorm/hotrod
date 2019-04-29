package sql;

import java.util.List;
import java.util.Map;

import sql.expressions.OrderingTerm;
import sql.expressions.predicates.Predicate;
import sql.expressions.predicates.PredicateBuilder;

public class SelectHaving implements ExecutableSelect {

  // Properties

  private AbstractSelect select;
  private PredicateBuilder predicateBuilder;

  // Constructor

  SelectHaving(final AbstractSelect select, final Predicate predicate) {
    this.select = select;
    this.predicateBuilder = new PredicateBuilder(predicate);
    this.select.setHavingCondition(this.predicateBuilder.getAssembled());
  }

  // Same stage

  public SelectHaving and(final Predicate predicate) {
    this.predicateBuilder.and(predicate);
    this.select.setHavingCondition(this.predicateBuilder.getAssembled());
    return this;
  }

  public SelectHaving or(final Predicate predicate) {
    this.predicateBuilder.or(predicate);
    this.select.setHavingCondition(this.predicateBuilder.getAssembled());
    return this;
  }

  // Next stages

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
