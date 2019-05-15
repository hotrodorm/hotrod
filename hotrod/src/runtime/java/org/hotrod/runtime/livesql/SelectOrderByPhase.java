package org.hotrod.runtime.livesql;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.AbstractSelect.TableReferencesValidator;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;

public class SelectOrderByPhase implements ExecutableSelect {

  // Properties

  private AbstractSelect<Map<String, Object>> select;

  // Constructor

  SelectOrderByPhase(final AbstractSelect<Map<String, Object>> select, final OrderingTerm... orderingTerms) {
    this.select = select;
    this.select.setColumnOrderings(Arrays.asList(orderingTerms));
  }

  // Same stage

  // Next stages

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
  public void validateTableReferences(final TableReferencesValidator tableReferences, final AliasGenerator ag) {
    this.select.validateTableReferences(tableReferences, ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.select.designateAliases(ag);
  }

}
