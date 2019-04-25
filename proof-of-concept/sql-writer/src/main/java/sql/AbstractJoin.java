package sql;

import metadata.TableOrView;
import sql.exceptions.InvalidSQLClauseException;

abstract class AbstractJoin {

  private TableOrView table;

  AbstractJoin(final TableOrView table) {
    if (table == null) {
      throw new InvalidSQLClauseException("The table or view on a join cannot be null");
    }
    this.table = table;
  }

  TableOrView getTable() {
    return table;
  }

}
