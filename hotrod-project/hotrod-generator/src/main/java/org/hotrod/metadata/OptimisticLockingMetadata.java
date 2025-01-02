package org.hotrod.metadata;

import java.io.Serializable;

import org.hotrod.config.OptimisticLockingTag;
import org.hotrod.config.OptimisticLockingTag.OptimisticLockingStrategy;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.typesolver.UnresolvableDataTypeException;

public class OptimisticLockingMetadata implements Serializable {

  private static final long serialVersionUID = 1L;

  private DataSetMetadata dataSet;
  private ColumnMetadata columnMetadata;

  private OptimisticLockingTag tag;
  @SuppressWarnings("unused")
  private DatabaseAdapter adapter;

  public OptimisticLockingMetadata(final DataSetMetadata dataSet, final OptimisticLockingTag tag,
      final ColumnMetadata cm, final DatabaseAdapter adapter) throws UnresolvableDataTypeException {
    this.dataSet = dataSet;
    this.tag = tag;
    this.adapter = adapter;
    this.columnMetadata = cm;
  }

  public DataSetMetadata getDataSet() {
    return dataSet;
  }

  public OptimisticLockingStrategy getStrategy() {
    return this.tag.getStrategy();
  }

  public ColumnMetadata getColumnMetadata() {
    return columnMetadata;
  }

}
