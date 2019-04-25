package metadata;

public class ColumnOrdering {

  private Column column;
  private boolean ascending;

  public ColumnOrdering(Column column, boolean ascending) {
    this.column = column;
    this.ascending = ascending;
  }

  public Column getColumn() {
    return column;
  }

  public boolean isAscending() {
    return ascending;
  };

}
