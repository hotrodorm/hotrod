package org.hotrod.runtime.livesql;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;

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

  // Apply aliases

  @Override
  public void gatherAliases(final AliasGenerator ag) {
    this.select.gatherAliases(ag);
  }

  @Override
  public void designateAliases(final AliasGenerator ag) {
    this.select.designateAliases(ag);
  }

}
