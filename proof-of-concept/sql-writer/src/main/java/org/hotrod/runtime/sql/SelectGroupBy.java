package org.hotrod.runtime.sql;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.expressions.OrderingTerm;
import org.hotrod.runtime.sql.expressions.predicates.Predicate;

public class SelectGroupBy implements ExecutableSelect {

  // Properties

  private AbstractSelect select;

  // Constructor

  SelectGroupBy(final AbstractSelect select, final Expression... expressions) {
    this.select = select;
    this.select.setGroupBy(Arrays.asList(expressions));
  }

  // Next stages

  public SelectHaving having(final Predicate predicate) {
    return new SelectHaving(this.select, predicate);
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
