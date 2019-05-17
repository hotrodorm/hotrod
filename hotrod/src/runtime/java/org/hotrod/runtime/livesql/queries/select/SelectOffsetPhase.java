package org.hotrod.runtime.livesql.queries.select;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.queries.select.AbstractSelect.AliasGenerator;
import org.hotrod.runtime.livesql.queries.select.AbstractSelect.TableReferences;

public class SelectOffsetPhase implements ExecutableSelect {

  // Properties

  private AbstractSelect<Map<String, Object>> select;

  // Constructor

  SelectOffsetPhase(final AbstractSelect<Map<String, Object>> select, final int offset) {
    this.select = select;
    this.select.setOffset(offset);
  }

  // Next stages

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
