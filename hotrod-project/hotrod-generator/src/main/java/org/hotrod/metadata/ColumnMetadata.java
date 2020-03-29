package org.hotrod.metadata;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.ConverterTag;
import org.hotrod.config.TypeSolverTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.database.PropertyType.ValueRange;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.utils.JdbcTypes;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrod.utils.identifiers.Id;
import org.hotrod.utils.identifiers.ObjectId;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcColumn.AutogenerationType;

public class ColumnMetadata implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(ColumnMetadata.class);

  private DataSetMetadata dataSet;

  private JdbcColumn c;
  private String columnName;
  private String tableName;

  private Id id;

  private boolean belongsToPK;

  private AutogenerationType autogenerationType;

  private int dataType;
  private String typeName;
  private Integer columnSize;
  private Integer decimalDigits;
  private String columnDefault;

  private EnumDataSetMetadata enumMetadata;

  private transient DatabaseAdapter adapter;
  private ColumnTag tag;
  private PropertyType type;

  private boolean isVersionControlColumn;

  private boolean reusesMemberFromSuperClass;

  // ToString

  public String toString() {
    return "columnName=" + columnName + " tableName=" + this.tableName + ", dataType=" + this.dataType + ", typeName="
        + this.typeName + " columnSize=" + this.columnSize + " decimalDigits=" + this.decimalDigits + " --- TYPE: "
        + this.type;
  }

  // From a <table>, <view>, or <enum> tag

  public ColumnMetadata(final DataSetMetadata dataSet, final JdbcColumn c, final DatabaseAdapter adapter,
      final ColumnTag columnTag, final boolean isVersionControlColumn, final boolean belongsToPK,
      final TypeSolverTag typeSolverTag) throws UnresolvableDataTypeException, InvalidIdentifierException {
    log.debug("init c=" + c);
    this.dataSet = dataSet;
    this.c = c;
    this.columnName = c.getName();
    log.debug("this.columnName=" + this.columnName);
    this.tableName = c.getTable().getName();

    this.tag = columnTag;
    if (this.tag == null || this.tag.getJavaName() == null) {
      this.id = Id.fromCanonicalSQL(c.getName(), adapter);
    } else {
      this.id = Id.fromCanonicalSQLAndJavaMember(c.getName(), adapter, this.tag.getJavaName());
    }
    log.debug(
        "  > CanonicalSQLName=" + this.id.getCanonicalSQLName() + " RenderedSQLName=" + this.id.getRenderedSQLName());

    this.belongsToPK = belongsToPK;
    this.autogenerationType = c.getAutogenerationType();
    this.dataType = c.getDataType();
    this.typeName = c.getTypeName();
    this.columnSize = c.getColumnSize();
    this.decimalDigits = c.getDecimalDigits();
    this.columnDefault = c.getColumnDef();
    this.enumMetadata = null;

    this.adapter = adapter;
    this.type = ColumnMetadata.resolveJavaType(this, this.tag, this.c, typeSolverTag, this.adapter);
    this.isVersionControlColumn = isVersionControlColumn;
    this.reusesMemberFromSuperClass = false;
  }

  public static PropertyType resolveJavaType(final ColumnMetadata cm, final ColumnTag columnTag, final JdbcColumn c,
      final TypeSolverTag typeSolverTag, final DatabaseAdapter adapter) throws UnresolvableDataTypeException {
    log.debug("columnTag=" + columnTag);
    if (columnTag != null) {
      log.debug("columnTag.getJdbcColumn()=" + columnTag.getJdbcColumn());
    }

    PropertyType typeSolverType = typeSolverTag.resolveType(cm, c);

    if (columnTag != null && (columnTag.getJavaType() != null || columnTag.getConverterTag() != null)) {

      // Use the type specified in the <column> tag

      log.debug("User-specified column type. Use it.");
      JDBCType jdbcType;
      if (columnTag.getJdbcType() != null) {
        // User specified the JDBC type. Use the user's.
        jdbcType = JdbcTypes.nameToType(columnTag.getJdbcType());
        if (jdbcType == null) {
          throw new UnresolvableDataTypeException(cm);
        }
      } else {
        // User did not specify the JDBC type. Get it from the live database.
        jdbcType = JdbcTypes.codeToType(cm.getDataType());
        if (jdbcType == null) {
          throw new UnresolvableDataTypeException(cm);
        }
      }
      ValueRange range = columnTag.getValueRange();
      if (range == null) {
        range = PropertyType.getDefaultValueRange(columnTag.getJavaType());
      }

      String javaType = columnTag.getJavaType() != null ? columnTag.getJavaType()
          : columnTag.getConverterTag().getJavaType();

      return new PropertyType(javaType, jdbcType, columnTag.isLOB(), range);

    } else {

      // Try the <type-solver> rules

      if (typeSolverType != null) {
        return typeSolverType;
      }

      // Otherwise, use the default type from the database adapter

      return adapter.getAdapterDefaultType(cm);

    }

  }

  public void setEnumMetadata(final EnumDataSetMetadata enumMetadata) {
    log.debug("[mark enum column] name=" + this.columnName + " enum=" + enumMetadata.getJdbcName());
    this.enumMetadata = enumMetadata;
  }

  // From another ColumnMetadata object

  protected ColumnMetadata(final ColumnMetadata cm) {
    this.dataSet = cm.dataSet;
    this.c = cm.c;
    this.columnName = cm.columnName;
    this.tableName = cm.tableName;

    this.id = cm.id;

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
    this.isVersionControlColumn = cm.isVersionControlColumn;
    this.reusesMemberFromSuperClass = false;
  }

  // From a <select> tag

  public ColumnMetadata(final DataSetMetadata dataSet, final JdbcColumn c, final String selectName,
      final DatabaseAdapter adapter, final ColumnTag columnTag, final boolean isVersionControlColumn,
      final boolean belongsToPK, final TypeSolverTag typeSolverTag)
      throws UnresolvableDataTypeException, InvalidIdentifierException {
    this.dataSet = dataSet;
    this.c = c;
    this.columnName = c.getName();
    this.tableName = selectName;

    this.tag = columnTag;
    if (this.tag == null || this.tag.getJavaName() == null) {
      this.id = Id.fromCanonicalSQL(c.getName(), adapter);
    } else {
      this.id = Id.fromCanonicalSQLAndJavaMember(c.getName(), adapter, this.tag.getJavaName());
    }

    this.belongsToPK = belongsToPK;
    this.autogenerationType = c.getAutogenerationType();
    this.dataType = c.getDataType();
    this.typeName = c.getTypeName();
    this.columnSize = c.getColumnSize();
    this.decimalDigits = c.getDecimalDigits();
    this.columnDefault = c.getColumnDef();
    this.enumMetadata = null;

    this.adapter = adapter;
    this.type = ColumnMetadata.resolveJavaType(this, this.tag, this.c, typeSolverTag, this.adapter);

    this.isVersionControlColumn = isVersionControlColumn;
    this.reusesMemberFromSuperClass = false;
  }

  // Applying a column tag to a column meta data

  public static ColumnMetadata applyColumnTag(final ColumnMetadata cm, final ColumnTag tag,
      final DatabaseAdapter adapter, final TypeSolverTag typeSolverTag)
      throws UnresolvableDataTypeException, InvalidIdentifierException {
    ColumnMetadata m2 = new ColumnMetadata(cm);
    m2.tag = tag;
//    m2.type = m2.adapter.resolveJavaType(m2, tag);
    m2.type = resolveJavaType(m2, tag, tag.getJdbcColumn(), typeSolverTag, m2.adapter);
    if (tag.getJavaName() != null) {
      m2.id = Id.fromCanonicalSQLAndJavaMember(cm.getColumnName(), adapter, tag.getJavaName());
    }
    return m2;
  }

  // Indexable

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

  public PropertyType getType() {
    return type;
  }

  public Id getId() {
    return this.id;
  }

  public boolean isConfigurationName(final String configurationName) {
    if (configurationName == null) {
      return false;
    }
    return this.adapter.isColumnIdentifier(this.columnName, configurationName);
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

  public ObjectId getSequenceId() {
    return this.tag != null ? this.tag.getSequenceId() : null;
  }

  public AutogenerationType getAutogenerationType() {
    return this.autogenerationType;
  }

  public boolean reusesMemberFromSuperClass() {
    return reusesMemberFromSuperClass;
  }

  // Setters

  void setVersionControlColumn(boolean isVersionControlColumn) {
    this.isVersionControlColumn = isVersionControlColumn;
  }

  public void setReusesMemberFromSuperClass(boolean reusesMemberFromSuperClass) {
    this.reusesMemberFromSuperClass = reusesMemberFromSuperClass;
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
