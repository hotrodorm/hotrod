package org.hotrod.metadata;

import java.io.Serializable;

import org.hotrod.config.AutoGeneratedColumnTag;
import org.hotrod.config.TypeSolverTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.utils.identifiers.Id;
import org.nocrala.tools.database.tartarus.core.JdbcColumn.AutogenerationType;

public class AutoGeneratedColumnMetadata implements Serializable {

  private static final long serialVersionUID = 1L;

  private DataSetMetadata dataSet;
  private ColumnMetadata columnMetadata;
  private AutoGeneratedColumnTag tag;
  private DatabaseAdapter adapter;

  public AutoGeneratedColumnMetadata(final DataSetMetadata dataSet, final AutoGeneratedColumnTag tag,
      final DatabaseAdapter adapter, final TypeSolverTag typeSolverTag)
      throws UnresolvableDataTypeException, InvalidIdentifierException {
    this.dataSet = dataSet;
    this.tag = tag;
    this.adapter = adapter;
    this.columnMetadata = new ColumnMetadata(dataSet, tag.getJdbcColumn(), this.adapter, null, false, true,
        typeSolverTag);
  }

  public DataSetMetadata getDataSet() {
    return dataSet;
  }

  public ColumnMetadata getColumnMetadata() {
    return columnMetadata;
  }

  public boolean isIdentity() {
    return this.tag.getAutogenerationType() == AutogenerationType.IDENTITY_ALWAYS
        || this.tag.getAutogenerationType() == AutogenerationType.IDENTITY_BY_DEFAULT;
  }

  public AutogenerationType getAutogenerationType() {
    return this.tag.getAutogenerationType();
  }

  public boolean supportsJDBCGeneratedKeys() {
    // return this.adapter.supportsJDBCGeneratedKeys();
    return true;
  }

  public Id getId() {
    return this.columnMetadata.getId();
  }

  public String getSequence() {
    return this.tag.getSequence();
  }

}