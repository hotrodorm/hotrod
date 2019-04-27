package sql;

import sql.exceptions.InvalidSQLClauseException;
import sql.metadata.TableOrView;

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
