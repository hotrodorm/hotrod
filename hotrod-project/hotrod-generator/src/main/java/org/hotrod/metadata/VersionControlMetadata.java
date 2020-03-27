package org.hotrod.metadata;

import java.io.Serializable;

import org.hotrod.config.VersionControlColumnTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.UnresolvableDataTypeException;

public class VersionControlMetadata implements Serializable {

  private static final long serialVersionUID = 1L;

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

}
