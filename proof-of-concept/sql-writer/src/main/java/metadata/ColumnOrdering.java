package metadata;

import sql.QueryWriter;
import sql.exceptions.InvalidSQLStatementException;

public class ColumnOrdering {

  private Column column;
  private boolean ascending;

  public ColumnOrdering(final Column column, final boolean ascending) {
    if (column == null) {
      throw new InvalidSQLStatementException(
          "Cannot use null value as column ordering. " + "Please speify a non null column in the ORDER BY clause");
    }
    this.column = column;
    this.ascending = ascending;
  }

  public Column getColumn() {
    return column;
  }

  public boolean isAscending() {
    return ascending;
  };

  public void renderTo(final QueryWriter pq) {
    this.column.renderTo(pq);
    if (!this.ascending) {
      pq.write(" desc");
    }
  }

}
