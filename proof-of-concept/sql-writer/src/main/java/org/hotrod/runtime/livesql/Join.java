package org.hotrod.runtime.livesql;

import org.hotrod.runtime.livesql.exceptions.InvalidSQLClauseException;
import org.hotrod.runtime.livesql.metadata.TableOrView;

public abstract class Join {

  private TableOrView table;

  Join(final TableOrView table) {
    if (table == null) {
      throw new InvalidSQLClauseException("The table or view on a join cannot be null");
    }
    this.table = table;
  }

  TableOrView getTable() {
    return this.table;
  }

}
