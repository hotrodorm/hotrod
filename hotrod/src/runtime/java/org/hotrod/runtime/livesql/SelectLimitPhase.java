package org.hotrod.runtime.livesql;

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.AbstractSelect.AliasGenerator;

public class SelectLimitPhase implements ExecutableSelect {

  // Properties

  private AbstractSelect<Map<String, Object>> select;

  // Constructor

  SelectLimitPhase(final AbstractSelect<Map<String, Object>> select, final int limit) {
    this.select = select;
    this.select.setLimit(limit);
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
