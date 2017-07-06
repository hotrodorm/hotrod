package org.hotrod.metadata;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hotrod.config.tags.ColumnTag;
import org.hotrod.config.tags.ConverterTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.utils.identifiers.ColumnIdentifier;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;

public class ColumnMetadata {

  private static final Logger log = Logger.getLogger(ColumnMetadata.class);

  private String columnName;
  private String tableName;

  private boolean belongsToPK;

  private int dataType;
  private String typeName;

  private Integer columnSize;
  private Integer decimalDigits;

  private DatabaseAdapter adapter;
  private ColumnTag tag;
  private PropertyType type;
  private ColumnIdentifier identifier;

  private boolean isVersionControlColumn;

  // ToString

  public String toString() {
    return "columnName=" + columnName + " tableName=" + this.tableName + ", dataType=" + this.dataType + ", typeName="
        + this.typeName + " columnSize=" + this.columnSize + " decimalDigits=" + this.decimalDigits + " --- TYPE: "
        + this.type;
  }

  public ColumnMetadata(final JdbcColumn c, final DatabaseAdapter adapter, final ColumnTag columnTag,
      final boolean isVersionControlColumn, final boolean belongsToPK) throws UnresolvableDataTypeException {
    log.debug("init");
    this.columnName = c.getName();
    this.tableName = c.getTable().getName();
    this.belongsToPK = belongsToPK;
    this.dataType = c.getDataType();
    this.typeName = c.getTypeName();
    this.columnSize = c.getColumnSize();
    this.decimalDigits = c.getDecimalDigits();

    this.adapter = adapter;
    this.tag = columnTag;
    this.type = this.adapter.resolveJavaType(this);
    this.identifier = new ColumnIdentifier(this.columnName, this.type, columnTag);
    this.isVersionControlColumn = isVersionControlColumn;
  }

  public ColumnMetadata(final JdbcColumn c, final String selectName, final DatabaseAdapter adapter,
      final ColumnTag columnTag, final boolean isVersionControlColumn, final boolean belongsToPK)
      throws UnresolvableDataTypeException {
    this.columnName = c.getName();
    this.tableName = selectName;
    this.belongsToPK = belongsToPK;
    this.dataType = c.getDataType();
    this.typeName = c.getTypeName();
    this.columnSize = c.getColumnSize();
    this.decimalDigits = c.getDecimalDigits();

    this.adapter = adapter;
    this.tag = columnTag;
    this.type = this.adapter.resolveJavaType(this);
    this.identifier = new ColumnIdentifier(this.columnName, this.type, columnTag);
    this.isVersionControlColumn = isVersionControlColumn;
  }

  public ColumnMetadata(final String queryName, final ResultSetMetaData md, final int col,
      final DatabaseAdapter adapter, final boolean isVersionControlColumn, final boolean belongsToPK)
      throws SQLException, UnresolvableDataTypeException {
    this.columnName = md.getColumnLabel(col) != null ? md.getColumnLabel(col) : md.getColumnName(col);
    this.tableName = queryName;
    this.belongsToPK = belongsToPK;
    this.dataType = md.getColumnType(col);
    this.typeName = md.getColumnTypeName(col);
    this.columnSize = md.getPrecision(col);
    this.decimalDigits = md.getScale(col);

    this.adapter = adapter;
    this.tag = findTag(adapter);
    this.type = this.adapter.resolveJavaType(this);
    this.identifier = new ColumnIdentifier(this.columnName, this.type, null);
    this.isVersionControlColumn = isVersionControlColumn;
  }

  // Indexing methods

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
    result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
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
    ColumnMetadata other = (ColumnMetadata) obj;
    if (columnName == null) {
      if (other.columnName != null)
        return false;
    } else if (!columnName.equals(other.columnName))
      return false;
    if (tableName == null) {
      if (other.tableName != null)
        return false;
    } else if (!tableName.equals(other.tableName))
      return false;
    return true;
  }

  // Utilities

  public ColumnTag findTag(final DatabaseAdapter adapter) {
    try {
      return adapter.findTableColumnTag(this);
    } catch (UnresolvableDataTypeException e) {
      return null;
    }
  }

  public PropertyType getType() {
    return type;
  }

  public ColumnIdentifier getIdentifier() {
    return identifier;
  }

  public String renderSQLIdentifier() {
    return this.adapter.quoteSQLName(this.columnName);
  }

  // Getters

  public String getColumnName() {
    return columnName;
  }

  public String getTableName() {
    return tableName;
  }

  public int getDataType() {
    return dataType;
  }

  public String getTypeName() {
    return typeName;
  }

  public Integer getColumnSize() {
    return columnSize;
  }

  public Integer getDecimalDigits() {
    return decimalDigits;
  }

  public ColumnTag getTag() {
    return tag;
  }

  public boolean isVersionControlColumn() {
    return isVersionControlColumn;
  }

  public ConverterTag getConverter() {
    return this.tag != null ? this.tag.getConverterTag() : null;
  }

  public boolean belongsToPK() {
    return belongsToPK;
  }

  // Setters

  void setVersionControlColumn(boolean isVersionControlColumn) {
    this.isVersionControlColumn = isVersionControlColumn;
  }

  // Sorting

  public boolean isString() {
    return "java.lang.String".equals(this.typeName);
  }

  public boolean isCaseSensitiveStringSortable() {
    return this.adapter.isCaseSensitiveSortableString(this);
  }

  public String renderForCaseInsensitiveOrderBy() {
    return this.adapter.renderForCaseInsensitiveOrderBy(this);
  }

}
