package org.hotrod.runtime.livesql;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class SelectGroupByPhase implements ExecutableSelect {

  // Properties

  private AbstractSelect<Map<String, Object>> select;

  // Constructor

  SelectGroupByPhase(final AbstractSelect<Map<String, Object>> select, final Expression<?>... expressions) {
    this.select = select;
    this.select.setGroupBy(Arrays.asList(expressions));
  }

  // Next stages

  public SelectHavingPhase having(final Predicate predicate) {
    return new SelectHavingPhase(this.select, predicate);
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

}
