package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;

public class AllColumns implements ResultSetColumn {

  private TableOrView tableOrView;

  public AllColumns(TableOrView tableOrView) {
    super();
    this.tableOrView = tableOrView;
  }

  @Override
  public void renderTo(QueryWriter w) {
    if (this.tableOrView.getAlias() != null) {
      w.write(this.tableOrView.getAlias());
      w.write(".");
    }
    w.write("*");
  }

}
