package org.hotrod.metadata;

import org.hotrod.config.VersionControlColumnTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.utils.identifiers.ColumnIdentifier;

public class VersionControlMetadata {

  private DataSetMetadata dataSet;
  private ColumnMetadata columnMetadata;

  @SuppressWarnings("unused")
  private VersionControlColumnTag tag;
  @SuppressWarnings("unused")
  private DatabaseAdapter adapter;

  public VersionControlMetadata(final DataSetMetadata dataSet, final VersionControlColumnTag tag,
      final ColumnMetadata cm, final DatabaseAdapter adapter) throws UnresolvableDataTypeException {
    this.dataSet = dataSet;
    this.tag = tag;
    this.adapter = adapter;
    this.columnMetadata = cm;
  }

  public DataSetMetadata getDataSet() {
    return dataSet;
  }

  public ColumnMetadata getColumnMetadata() {
    return columnMetadata;
  }

  public ColumnIdentifier getIdentifier() {
    return this.columnMetadata.getIdentifier();
  }

}
