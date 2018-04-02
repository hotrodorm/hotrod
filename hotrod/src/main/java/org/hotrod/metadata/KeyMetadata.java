package org.hotrod.metadata;

import java.io.Serializable;
import java.util.List;

import org.hotrod.runtime.util.ListWriter;

public class KeyMetadata implements Serializable {

  private static final long serialVersionUID = 1L;

  private TableDataSetMetadata tableMetadata;
  private List<ColumnMetadata> columns;

  public KeyMetadata(final TableDataSetMetadata dataSet, final List<ColumnMetadata> columns) {
    this.tableMetadata = dataSet;
    this.columns = columns;
  }

  void linkReferencedDataSet(final TableDataSetMetadata tableMetadata) {
    this.tableMetadata = tableMetadata;
  }

  // Getters

  public TableDataSetMetadata getTableMetadata() {
    return tableMetadata;
  }

  public List<ColumnMetadata> getColumns() {
    return columns;
  }

  // Indexing methods

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((tableMetadata == null) ? 0 : tableMetadata.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    KeyMetadata other = (KeyMetadata) obj;
    if (tableMetadata == null) {
      if (other.tableMetadata != null)
        return false;
    } else if (!tableMetadata.equals(other.tableMetadata))
      return false;
    return equals(this.columns, other.columns);
  }

  private boolean equals(final List<ColumnMetadata> a, final List<ColumnMetadata> b) {
    if (a.size() != b.size()) {
      return false;
    }
    for (int i = 0; i < a.size(); i++) {
      if (!a.get(i).equals(b.get(i))) {
        return false;
      }
    }
    return true;
  }

  // Utilities

  public String toCamelCase(final String columnSeam) {
    ListWriter lw = new ListWriter(columnSeam == null ? "" : columnSeam);
    for (ColumnMetadata km : this.columns) {
      lw.add(km.getIdentifier().getJavaClassIdentifier());
    }
    return lw.toString();
  }

}
