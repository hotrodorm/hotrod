package org.hotrod.metadata;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.config.ColumnTag;
import org.hotrod.exceptions.UnresolvableDataTypeException;

public class StructuredColumnMetadata extends ColumnMetadata {

  // Properties

  private String columnAlias;
  private boolean id;

  // Constructor

  public StructuredColumnMetadata(final ColumnMetadata cm, final String columnAlias, final boolean id) {
    super(cm);
    this.columnAlias = columnAlias;
    this.id = id;
  }

  // Behavior

  public static StructuredColumnMetadata applyColumnTag(final StructuredColumnMetadata orig, final ColumnTag t)
      throws UnresolvableDataTypeException {
    ColumnMetadata cm = ColumnMetadata.applyColumnTag(orig, t);
    return new StructuredColumnMetadata(cm, orig.columnAlias, orig.id);
  }

  // Getters

  public String getColumnAlias() {
    return columnAlias;
  }

  public boolean isId() {
    return id;
  }

  // Utilities

//  public static List<StructuredColumnMetadata> promote(final List<ColumnMetadata> cols, final String aliasPrefix) {
//    if (aliasPrefix == null) {
//      throw new IllegalArgumentException("aliasPrefix cannot be null!");
//    }
//    List<StructuredColumnMetadata> columns = new ArrayList<StructuredColumnMetadata>();
//    for (ColumnMetadata cm : cols) {
//      StructuredColumnMetadata m = new StructuredColumnMetadata(cm, aliasPrefix + cm.getColumnName());
//      columns.add(m);
//    }
//    return columns;
//  }

}
