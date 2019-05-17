package org.hotrod.runtime.livesql.queries.select;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.metadata.TableOrView;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public class SelectFromPhase implements ExecutableSelect {

  // Properties

  private AbstractSelect<Map<String, Object>> select;

  // Constructor

  SelectFromPhase(final AbstractSelect<Map<String, Object>> select, final TableOrView t) {
    this.select = select;
    this.select.setBaseTable(t);
  }

  // This stage

  public SelectFromPhase join(final TableOrView t, final Predicate on) {
    this.select.addJoin(new InnerJoin(t, on));
    return this;
  }

  public SelectFromPhase leftJoin(final TableOrView t, final Predicate on) {
    this.select.addJoin(new LeftOuterJoin(t, on));
    return this;
  }

  public SelectFromPhase rightJoin(final TableOrView t, final Predicate on) {
    this.select.addJoin(new RightOuterJoin(t, on));
    return this;
  }

  public SelectFromPhase fullOuterJoin(final TableOrView t, final Predicate on) {
    this.select.addJoin(new FullOuterJoin(t, on));
    return this;
  }

  public SelectFromPhase crossJoin(final TableOrView t) {
    this.select.addJoin(new CrossJoin(t));
    return this;
  }

  // Next stages

  public SelectWherePhase where(final Predicate predicate) {
    return new SelectWherePhase(this.select, predicate);
  }

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
