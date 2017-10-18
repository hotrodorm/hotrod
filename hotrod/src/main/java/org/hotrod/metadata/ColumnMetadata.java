package org.hotrod.metadata;

import org.apache.log4j.Logger;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.ConverterTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.utils.identifiers.ColumnIdentifier;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcColumn.AutogenerationType;

public class ColumnMetadata {

  private static final Logger log = Logger.getLogger(ColumnMetadata.class);

  private DataSetMetadata dataSet;

  private JdbcColumn c;
  private String columnName;
  private String tableName;

  private boolean belongsToPK;

  private AutogenerationType autogenerationType;

  private int dataType;
  private String typeName;
  private Integer columnSize;
  private Integer decimalDigits;
  private String columnDefault;

  private EnumDataSetMetadata enumMetadata;

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

  // From a <table>, <view>, or <enum> tag

  public ColumnMetadata(final DataSetMetadata dataSet, final JdbcColumn c, final DatabaseAdapter adapter,
      final ColumnTag columnTag, final boolean isVersionControlColumn, final boolean belongsToPK)
      throws UnresolvableDataTypeException {
    log.debug("init");
    this.dataSet = dataSet;
    this.c = c;
    this.columnName = c.getName();
    this.tableName = c.getTable().getName();
    this.belongsToPK = belongsToPK;
    this.autogenerationType = c.getAutogenerationType();
    this.dataType = c.getDataType();
    this.typeName = c.getTypeName();
    this.columnSize = c.getColumnSize();
    this.decimalDigits = c.getDecimalDigits();
    this.columnDefault = c.getColumnDef();
    this.enumMetadata = null;

    this.adapter = adapter;
    this.tag = columnTag;
    this.type = this.adapter.resolveJavaType(this, this.tag);
    this.identifier = new ColumnIdentifier(this.columnName, this.type, columnTag);
    this.isVersionControlColumn = isVersionControlColumn;
  }

  public void setEnumMetadata(final EnumDataSetMetadata enumMetadata) {
    this.enumMetadata = enumMetadata;
  }

  // From another ColumnMetadata object

  protected ColumnMetadata(final ColumnMetadata cm) {
    this.dataSet = cm.dataSet;
    this.c = cm.c;
    this.columnName = cm.columnName;
    this.tableName = cm.tableName;
    this.belongsToPK = cm.belongsToPK;
    this.autogenerationType = cm.autogenerationType;
    this.dataType = cm.dataType;
    this.typeName = cm.typeName;
    this.columnSize = cm.columnSize;
    this.decimalDigits = cm.decimalDigits;
    this.columnDefault = cm.columnDefault;
    this.enumMetadata = cm.enumMetadata;
    this.adapter = cm.adapter;
    this.tag = cm.tag;
    this.type = cm.type;
    this.identifier = cm.identifier;
    this.isVersionControlColumn = cm.isVersionControlColumn;
  }

  // From a <select> tag

  public ColumnMetadata(final DataSetMetadata dataSet, final JdbcColumn c, final String selectName,
      final DatabaseAdapter adapter, final ColumnTag columnTag, final boolean isVersionControlColumn,
      final boolean belongsToPK) throws UnresolvableDataTypeException {
    this.dataSet = dataSet;
    this.c = c;
    this.columnName = c.getName();
    this.tableName = selectName;
    this.belongsToPK = belongsToPK;
    this.autogenerationType = c.getAutogenerationType();
    this.dataType = c.getDataType();
    this.typeName = c.getTypeName();
    this.columnSize = c.getColumnSize();
    this.decimalDigits = c.getDecimalDigits();
    this.columnDefault = c.getColumnDef();
    this.enumMetadata = null;

    this.adapter = adapter;
    this.tag = columnTag;
    this.type = this.adapter.resolveJavaType(this, this.tag);
    this.identifier = new ColumnIdentifier(this.columnName, this.type, columnTag);
    this.isVersionControlColumn = isVersionControlColumn;
  }

  // Applying a column tag to a column meta data

  public static ColumnMetadata applyColumnTag(final ColumnMetadata cm, final ColumnTag tag) throws UnresolvableDataTypeException {
    ColumnMetadata m2 = new ColumnMetadata(cm);
    m2.tag = tag;
    m2.type = m2.adapter.resolveJavaType(m2, tag);
    return m2;
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
    return this.adapter.renderSQLName(this.columnName);
  }

  // Getters

  public DataSetMetadata getDataSet() {
    return dataSet;
  }

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

  public String getColumnDefault() {
    return columnDefault;
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

  public EnumDataSetMetadata getEnumMetadata() {
    return enumMetadata;
  }

  public String getSequence() {
    return this.tag == null ? null : this.tag.getSequence();
  }

  public String getCanonicalSequence() {
    return this.tag == null ? null : this.adapter.canonizeName(this.tag.getSequence(), false);
  }

  public String renderSQLSequence() {
    return this.tag == null || this.tag.getSequence() == null ? null
        : this.adapter.renderSQLName(this.getCanonicalSequence());
  }

  public AutogenerationType getAutogenerationType() {
    return this.autogenerationType;
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
