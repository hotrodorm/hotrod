package org.hotrod.runtime.livesql;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class SelectOrderBy implements ExecutableSelect {

  // Properties

  private AbstractSelect select;

  // Constructor

  SelectOrderBy(final AbstractSelect select, final OrderingTerm... orderingTerms) {
    this.select = select;
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
  }

  // Same stage

  // Next stages

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
