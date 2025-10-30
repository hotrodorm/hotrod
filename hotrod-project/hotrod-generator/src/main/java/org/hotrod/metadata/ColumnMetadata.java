package org.hotrod.metadata;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.config.ColumnTag;
import org.hotrod.config.ConverterTag;
import org.hotrod.config.NameSolverNameTag.Scope;
import org.hotrod.config.NameSolverTag;
import org.hotrod.config.StaticTypeSolverTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.database.ValueRange;
import org.hotrod.exceptions.CouldNotResolveNameException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.identifiers.Id;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.livesql.queries.typesolver.TypeSource;
import org.hotrod.typesolver.DriverColumnMetaData;
import org.hotrod.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.JDBCTypes;
import org.hotrod.utils.JDBCTypes.JDBCType;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcColumn.AutogenerationType;

public class ColumnMetadata implements DriverColumnMetaData {

  private static final Logger log = Logger.getLogger(ColumnMetadata.class.getName());

  private DataSetMetadata dataSet;

  private JdbcColumn c;

  private String catalog;
  private String schema;
  private int ordinal;
  private String columnName;
  private String objectType;
  private String tableName;

  private Id id;

  private boolean belongsToPK;

  private AutogenerationType autogenerationType;

  private int dataType;
  private JDBCType resultSetType;
  private String typeName;
  private Integer columnSize;
  private Integer decimalDigits;
  private String columnDefault;

  private EnumDataSetMetadata enumMetadata;

  private DatabaseAdapter adapter;
  private ColumnTag tag;
  private PropertyType type;
  private StaticTypeSolverTag typeSolverTag;

  private boolean isOLVersionNumberColumn;
  private boolean isOLTimestampColumn;

  private boolean reusesMemberFromSuperClass;

  // ToString

  public String toString() {
    return "columnName=" + columnName + " tableName=" + this.tableName + ", dataType=" + this.dataType + ", typeName="
        + this.typeName + " columnSize=" + this.columnSize + " decimalDigits=" + this.decimalDigits + " --- TYPE: "
        + this.type;
  }

  // From a <table>, <view>, or <enum> tag

  public ColumnMetadata(final DataSetMetadata dataSet, final JdbcColumn c, final DatabaseAdapter adapter,
      final ColumnTag columnTag, final boolean isOLVersionNumberColumn, final boolean isOLTimestampColumn,
      final boolean belongsToPK, final StaticTypeSolverTag typeSolverTag, final NameSolverTag nameSolverTag)
      throws UnresolvableDataTypeException, InvalidIdentifierException {
    log.fine("init c=" + c);
    this.dataSet = dataSet;
    this.c = c;
    this.catalog = c.getTable().getCatalog();
    this.schema = c.getTable().getSchema();
    this.ordinal = c.getOrdinalPosition();
    this.columnName = c.getName();
    log.fine("this.columnName=" + this.columnName);
    this.objectType = c.getTable().getType();
    this.tableName = c.getTable().getName();

    this.tag = columnTag;

    if (this.tag != null && this.tag.getProperty() != null) {
      this.id = Id.fromCanonicalSQLAndJavaMember(c.getName(), adapter, this.tag.getProperty());
    } else {
      String replacedName = null;
      try {
        replacedName = nameSolverTag.resolveName(c.getName(), Scope.COLUMN);
        log.fine("%%% " + this.tableName + "." + c.getName() + " -- replacedName=" + replacedName);
      } catch (CouldNotResolveNameException e) {
        throw new InvalidIdentifierException(
            "Could not resolve property name for column " + this.tableName + "." + c.getName() + ": " + e.getMessage());
      }
      if (replacedName != null) {
        String javaClassName = Id.fromCanonicalSQL(replacedName, adapter).getJavaClassName();
        this.id = Id.fromCanonicalSQLAndJavaClass(c.getName(), adapter, javaClassName);
      } else {
        this.id = Id.fromCanonicalSQL(c.getName(), adapter);
      }
    }
    log.fine(
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
    this.type = this.resolveJavaType(this, this.tag, this.c, null, typeSolverTag, this.adapter);
    this.typeSolverTag = typeSolverTag;
    this.isOLVersionNumberColumn = isOLVersionNumberColumn;
    this.isOLTimestampColumn = isOLTimestampColumn;
    this.reusesMemberFromSuperClass = false;
  }

  public void setEnumMetadata(final EnumDataSetMetadata enumMetadata) {
    log.fine("[mark enum column] name=" + this.columnName + " enum=" + enumMetadata.getJdbcName());
    this.enumMetadata = enumMetadata;
  }

  // From another ColumnMetadata object

  protected ColumnMetadata(final ColumnMetadata cm) {
    this.dataSet = cm.dataSet;
    this.c = cm.c;
    this.ordinal = cm.ordinal;
    this.columnName = cm.columnName;
    this.objectType = cm.objectType;
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
    this.typeSolverTag = cm.typeSolverTag;
    this.isOLVersionNumberColumn = cm.isOLVersionNumberColumn;
    this.isOLTimestampColumn = cm.isOLTimestampColumn;
    this.reusesMemberFromSuperClass = false;
  }

//  // From a <select> tag -- create view strategy
//
//  public ColumnMetadata(final ExecutorDAOMetadata dataSet, final JdbcColumn c, final String selectName,
//      final DatabaseAdapter adapter, final ColumnTag columnTag, final boolean isOLVersionNumberColumn,
//      final boolean isOLTimestampColumn, final boolean belongsToPK, final StaticTypeSolverTag typeSolverTag)
//      throws UnresolvableDataTypeException, InvalidIdentifierException {
//    this.dataSet = dataSet;
//    this.c = c;
//    this.catalog = null;
//    this.schema = null;
//    this.ordinal = c.getOrdinalPosition();
//    this.columnName = c.getName();
//    this.tableName = selectName;
//
//    this.tag = columnTag;
//    if (this.tag == null || this.tag.getJavaName() == null) {
//      this.id = Id.fromCanonicalSQL(c.getName(), adapter);
//    } else {
//      this.id = Id.fromCanonicalSQLAndJavaMember(c.getName(), adapter, this.tag.getJavaName());
//    }
//
//    this.belongsToPK = belongsToPK;
//    this.autogenerationType = c.getAutogenerationType();
//    this.dataType = c.getDataType();
//    this.typeName = c.getTypeName();
//    this.columnSize = c.getColumnSize();
//    this.decimalDigits = c.getDecimalDigits();
//    this.columnDefault = c.getColumnDef();
//    this.enumMetadata = null;
//
//    this.adapter = adapter;
//    this.type = this.resolveJavaType(this, this.tag, this.c, null, typeSolverTag, this.adapter);
//    this.typeSolverTag = typeSolverTag;
//
//    this.isOLVersionNumberColumn = isOLVersionNumberColumn;
//    this.isOLTimestampColumn = isOLTimestampColumn;
//    this.reusesMemberFromSuperClass = false;
//  }

  // From a <select> tag -- result set strategy

  public ColumnMetadata(final SelectMethodMetadata dataSet, ResultSetMetaData rm, final int colIndex,
      final String selectName, final DatabaseAdapter adapter, final ColumnTag columnTag,
      final boolean isOLVersionNumberColumn, final boolean isOLTimestampColumn, final boolean belongsToPK,
      final StaticTypeSolverTag typeSolverTag)
      throws UnresolvableDataTypeException, InvalidIdentifierException, SQLException {
    this.dataSet = dataSet;
    this.c = null;
    this.catalog = null;
    this.schema = null;
    this.ordinal = colIndex;
    this.columnName = rm.getColumnLabel(colIndex);

    this.tableName = dataSet.getSelectMethodNamespace() + "." + selectName;

    this.tag = columnTag;
    if (this.tag == null || this.tag.getProperty() == null) {
      this.id = Id.fromCanonicalSQL(this.columnName, adapter);
    } else {
      this.id = Id.fromCanonicalSQLAndJavaMember(this.columnName, adapter, this.tag.getProperty());
    }

    this.belongsToPK = belongsToPK;
    this.autogenerationType = null;
    this.dataType = rm.getColumnType(colIndex);
    this.typeName = rm.getColumnTypeName(colIndex);
    this.columnSize = rm.getPrecision(colIndex);
    this.decimalDigits = rm.getScale(colIndex);
    this.columnDefault = null;
    this.enumMetadata = null;

    this.resultSetType = JDBCTypes.codeToType(this.dataType);

    this.adapter = adapter;
    this.type = this.resolveJavaType(this, this.tag, null, this.resultSetType, typeSolverTag, this.adapter);
    this.typeSolverTag = typeSolverTag;

    this.isOLVersionNumberColumn = isOLVersionNumberColumn;
    this.isOLTimestampColumn = isOLTimestampColumn;
    this.reusesMemberFromSuperClass = false;
  }

  private PropertyType resolveJavaType(final ColumnMetadata cm, final ColumnTag columnTag, final JdbcColumn c,
      final JDBCType resultSetType, final StaticTypeSolverTag typeSolverTag, final DatabaseAdapter adapter)
      throws UnresolvableDataTypeException {

    PropertyType typeSolverType = typeSolverTag.resolveStaticType(cm, c, resultSetType);

    if (columnTag != null && (columnTag.getType() != null || columnTag.getConverterTag() != null)) {

      // Use the type specified in the <column> tag

      log.fine("User-specified column type. Use it.");
      JDBCType jdbcType;
      if (columnTag.getJdbcType() != null) {
        // User specified the JDBC type. Use the user's.
        jdbcType = JDBCTypes.nameToType(columnTag.getJdbcType());
        if (jdbcType == null) {
          throw new UnresolvableDataTypeException(cm);
        }
      } else {
        // User did not specify the JDBC type. Get it from the live database.
        jdbcType = JDBCTypes.codeToType(cm.getDataType());
        if (jdbcType == null) {
          throw new UnresolvableDataTypeException(cm);
        }
      }
      ValueRange range = columnTag.getValueRange();
      if (range == null) {
        range = PropertyType.getDefaultValueRange(columnTag.getType());
      }

      String javaType = columnTag.getType() != null ? columnTag.getType()
          : columnTag.getConverterTag().getDomainClass();

      return new PropertyType(javaType, jdbcType, columnTag.isLOB(), range, TypeSource.STATIC_DESIGNATED,
          cm.getColumnTagConverter(), null);

    } else {

      // Try the <type-solver> rules

      if (typeSolverType != null) {
        return typeSolverType;
      }

      // Otherwise, use the default type from the HotRod database dialect

      PropertyType t = adapter.getDialectDefaultType(cm);
      if (t != null) {
        return t;
      }
      throw new UnresolvableDataTypeException(cm);

    }

  }

  // Applying a column tag to a column meta data

  public static ColumnMetadata applyColumnTag(final ColumnMetadata cm, final ColumnTag tag,
      final DatabaseAdapter adapter) throws UnresolvableDataTypeException, InvalidIdentifierException {
    ColumnMetadata m2 = new ColumnMetadata(cm);
    m2.tag = tag;
    m2.type = cm.resolveJavaType(m2, tag, tag.getJdbcColumn(), cm.resultSetType, cm.typeSolverTag, m2.adapter);
    if (tag.getProperty() != null) {
      m2.id = Id.fromCanonicalSQLAndJavaMember(cm.getName(), adapter, tag.getProperty());
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

  public String getColumnDefault() {
    return columnDefault;
  }

  public boolean isOLVersionNumberColumn() {
    return isOLVersionNumberColumn;
  }

  public boolean isOLTimestampColumn() {
    return this.isOLTimestampColumn;
  }

  public ConverterTag getResolvedConverter() {
    return this.type.getConverterTag();
  }

  public ConverterTag getColumnTagConverter() {
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

  void setOLVersionNumberColumn(boolean isOLVersionNumberColumn) {
    this.isOLVersionNumberColumn = isOLVersionNumberColumn;
  }

  void setOLTimestampColumn(boolean isOLTimestampColumn) {
    log.info("isOLTimestampColumn=" + isOLTimestampColumn);
    this.isOLTimestampColumn = isOLTimestampColumn;
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

  @Override
  public String getCatalog() {
    return this.catalog;
  }

  @Override
  public String getSchema() {
    return this.schema;
  }

  public String getObjectType() {
    return objectType;
  }

  @Override
  public String getTable() {
    return this.tableName;
  }

  @Override
  public final int getOrdinal() {
    return ordinal;
  }

  @Override
  public String getName() {
    return this.columnName;
  }

  @Override
  public String getLabel() {
    return this.columnName;
  }

  public String getTypeName() {
    return typeName;
  }

  public Integer getDataType() {
    return dataType;
  }

  @Override
  public String getDriverDefaultClassName() {
    return null;
  }

  @Override
  public Integer getDisplaySize() {
    return this.columnSize;
  }

  @Override
  public Integer getPrecision() {
    return this.columnSize;
  }

  @Override
  public Integer getScale() {
    return this.decimalDigits;
  }

}
