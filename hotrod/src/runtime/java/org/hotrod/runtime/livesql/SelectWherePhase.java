package org.hotrod.runtime.livesql;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.AbstractSelect.TableReferences;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.expressions.predicates.PredicateBuilder;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class SelectWherePhase implements ExecutableSelect {

  // Properties

  private AbstractSelect<Map<String, Object>> select;
  private PredicateBuilder predicateBuilder;

  // Constructors

  SelectWherePhase(final AbstractSelect<Map<String, Object>> select, final Predicate predicate) {
    this.select = select;
    this.predicateBuilder = new PredicateBuilder(predicate);
    this.select.setWhereCondition(this.predicateBuilder.getAssembled());
  }

  // Same stage

  public SelectWherePhase and(final Predicate predicate) {
    this.predicateBuilder.and(predicate);
    this.select.setWhereCondition(this.predicateBuilder.getAssembled());
    return this;
  }

  public SelectWherePhase or(final Predicate predicate) {
    this.predicateBuilder.or(predicate);
    this.select.setWhereCondition(this.predicateBuilder.getAssembled());
    return this;
  }

  // Next stages

  public SelectGroupByPhase groupBy(final Expression<?>... columns) {
    return new SelectGroupByPhase(this.select, columns);
  }

  public SelectOrderByPhase orderBy(final OrderingTerm... orderingTerms) {
    return new SelectOrderByPhase(this.select, orderingTerms);
  }

  public SelectOffsetPhase offset(final int offset) {
    return new SelectOffsetPhase(this.select, offset);
  }

  public SelectLimitPhase limit(final int limit) {
    return new SelectLimitPhase(this.select, limit);
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

  // Validation

  @Override
  public void validateTableReferences(final TableReferences tableReferences, final AliasGenerator ag) {
    this.select.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.select.assignNonDeclaredAliases(ag);
  }

}
