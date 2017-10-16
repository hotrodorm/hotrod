package org.hotrod.metadata;

import java.util.ArrayList;
import java.util.List;

public class StructuredColumnMetadata extends ColumnMetadata {

  private String columnAlias;

  public StructuredColumnMetadata(final ColumnMetadata cm, final String columnAlias) {
    super(cm);
    this.columnAlias = columnAlias;
  }

  public String getColumnAlias() {
    return columnAlias;
  }

  // Utilities

  public static List<StructuredColumnMetadata> promote(final List<ColumnMetadata> cols, final String aliasPrefix) {
    if (aliasPrefix == null) {
      throw new IllegalArgumentException("aliasPrefix cannot be null!");
    }
    List<StructuredColumnMetadata> columns = new ArrayList<StructuredColumnMetadata>();
    for (ColumnMetadata cm : cols) {
      StructuredColumnMetadata m = new StructuredColumnMetadata(cm, aliasPrefix + cm.getColumnName());
      columns.add(m);
    }
    return columns;
  }

}
