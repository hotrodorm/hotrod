package org.hotrod.runtime.livesql.queries.select;

import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLClauseException;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public abstract class Join {

  private TableOrView table;

  Join(final TableOrView table) {
    if (table == null) {
      throw new InvalidLiveSQLClauseException("The table or view on a join cannot be null");
    }
    this.table = table;
  }

  TableOrView getTable() {
    return this.table;
  }

  public String renderTree() {
    return this.table.renderTree();
  }

}
