package org.hotrod.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hotrod.config.ConverterTag;
import org.hotrod.exceptions.IdentitiesPostFetchNotSupportedException;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.livesql.queries.typesolver.TypeSource;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.JDBCTypes;
import org.hotrod.utils.JDBCTypes.JDBCType;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.exception.CatalogNotSupportedException;
import org.nocrala.tools.database.tartarus.exception.InvalidCatalogException;
import org.nocrala.tools.database.tartarus.exception.InvalidSchemaException;
import org.nocrala.tools.database.tartarus.exception.SchemaNotSupportedException;
import org.nocrala.tools.database.tartarus.utils.JdbcUtil;

public abstract class DatabaseAdapter {

  private static final Logger log = Logger.getLogger(DatabaseAdapter.class.getName());

  protected DatabaseMetaData databaseMedaData;
  protected String identifierQuoteString;

  private Map<String, DataType> dataTypes;

  public static class InsertIntegration {

    private boolean identities;
    private boolean identitiesMustDeclarePKColumns;
    private boolean sequencesKeysResultSet;
    private boolean sequencesStandardResultSet;
    private String outputClause;
    private boolean defaults;

    private InsertIntegration(boolean identities, boolean identitiesMustDeclarePKColumns,
        boolean sequencesKeysResultSet, boolean sequencesStandardResultSet, String outputClause, boolean defaults) {
      this.identities = identities;
      this.identitiesMustDeclarePKColumns = identitiesMustDeclarePKColumns;
      this.sequencesKeysResultSet = sequencesKeysResultSet;
      this.sequencesStandardResultSet = sequencesStandardResultSet;
      this.outputClause = outputClause;
      this.defaults = defaults;
    }

    public static InsertIntegration of(boolean identities, boolean identitiesMustDeclarePKColumns,
        boolean sequencesKeysResultSet, boolean sequencesStandardResultSet, String outputClause, boolean defaults) {
      return new InsertIntegration(identities, identitiesMustDeclarePKColumns, sequencesKeysResultSet,
          sequencesStandardResultSet, outputClause, defaults);
    }

    public boolean integratesIdentities() {
      return identities;
    }

    public boolean identitiesMustDeclarePKColumns() {
      return this.identitiesMustDeclarePKColumns;
    }

    public boolean integratesSequencesStandardResultSet() {
      return sequencesStandardResultSet;
    }

    public boolean integratesSequencesKeysResultSet() {
      return sequencesKeysResultSet;
    }

    public String getOutputClause() {
      return outputClause;
    }

    public boolean isDefaults() {
      return defaults;
    }

  }

  public static enum UnescapedSQLCase {
    LOWER_CASE, UPPER_CASE, ANY_CASE;
  }

  public DatabaseAdapter(final DatabaseMetaData dm) throws SQLException {
    this.databaseMedaData = dm;
    this.identifierQuoteString = dm.getIdentifierQuoteString();
    this.dataTypes = DataType.retrieveDataTypes(this.databaseMedaData);
  }

  /*
   * <pre>
   * 
   * TableTag -* JdbcTable | ^ | ^ * | * | ColumnTag -* JdbcColumn
   * 
   * </pre>
   * 
   * @param md Column metadata
   * 
   * @param columnTag Column tag
   * 
   * @return The property type
   * 
   * @throws UnresolvableDataTypeException When the type cannot be resolved
   */

  // Names

  public boolean equalConfigNames(final String a, final String b) {
    if (a == null || b == null) {
      return false;
    }
    return this.canonizeName(a, false).equals(this.canonizeName(b, false));
  }

  public abstract String canonizeName(String configName, boolean quoted);

  public abstract String renderSQLName(String canonicalName, boolean quoted);

  public abstract boolean canonicalNameRequiresQuoting(String canonicalSQLName);

  protected String quote(final String canonicalName) {
    return this.identifierQuoteString + canonicalName + this.identifierQuoteString;
  }

  // End of names

  public abstract boolean supportsCatalog();

  public abstract boolean supportsSchema();

  public abstract String getName();

  public abstract PropertyType getDialectDefaultType(ColumnMetadata cm) throws UnresolvableDataTypeException;

  // Insert Behavior

  public abstract InsertIntegration getInsertIntegration();

  public abstract boolean integratesUsingQuery();

  public abstract String renderInsertQueryColumn(ColumnMetadata cm);

  public abstract String renderSequencesPrefetch(final List<ColumnMetadata> sequenceGeneratedColumns)
      throws SequencesNotSupportedException;

  public String renderSelectSequence(final ColumnMetadata cm) throws SequencesNotSupportedException {
    return this.renderSelectSequence(cm.getSequenceId());
  }

  public abstract String renderSelectSequence(final ObjectId sequenceId) throws SequencesNotSupportedException;

  public abstract String renderInlineSequenceOnInsert(final ColumnMetadata cm) throws SequencesNotSupportedException;

  public abstract String renderIdentitiesPostfetch(final List<ColumnMetadata> identityGeneratedColumns)
      throws IdentitiesPostFetchNotSupportedException;

  // End of Insert Behavior

  public abstract boolean isTableIdentifier(String jdbcName, String name);

  public abstract boolean isColumnIdentifier(String jdbcName, String name);

  public abstract String formatSchemaName(String name);

  public abstract String createOrReplaceView(String viewName, String select);

  public abstract String dropView(String viewName);

  public abstract String formatJdbcTableName(String tableName);

  public abstract boolean isSerial(JdbcColumn c);

  public abstract String renderAliasedSelectColumn(StructuredColumnMetadata cm);

  public abstract UnescapedSQLCase getUnescapedSQLCase();

  public abstract String provideSampleValueFor(JDBCType jdbcType);

  public abstract String currentTimestampSQLExpression();

  public abstract void setCurrentCatalogSchema(final Connection conn, final String catalog, final String schema)
      throws CatalogNotSupportedException, InvalidSchemaException, SQLException, InvalidCatalogException,
      SchemaNotSupportedException;

  // Sorting

  /* Default implementation */
  public boolean isCaseSensitiveSortableString(final ColumnMetadata cm) {
    DataType dataType = this.dataTypes.get(cm.getTypeName());
    log.fine("dataType=" + dataType);
    return dataType != null && dataType.caseSensitive && dataType.searchable;
  }

  public abstract String renderForCaseInsensitiveOrderBy(final ColumnMetadata cm);

  // Utilities

  protected PropertyType produceType(final Class<?> c, final ColumnMetadata m, final boolean isLOB,
      final ConverterTag converterTag, final Integer ruleNumber) throws UnresolvableDataTypeException {
    JDBCType jdbcType = JDBCTypes.codeToType(m.getDataType());
    if (jdbcType == null) {
      throw new UnresolvableDataTypeException(m);
    }
    return new PropertyType(c.getName(), jdbcType, isLOB, TypeSource.STATIC_DIALECT_RULE, converterTag, ruleNumber);
  }

  // Classes

  private static class DataType {

    private String typeName;
    private int dataType;
    private boolean caseSensitive;
    private boolean searchable;

    public static Map<String, DataType> retrieveDataTypes(final DatabaseMetaData databaseMedaData) throws SQLException {
      Map<String, DataType> dataTypes = new HashMap<String, DataType>();
      ResultSet rs = null;
      try {
        rs = databaseMedaData.getTypeInfo();
        while (rs.next()) {
          DataType t = new DataType();
          t.typeName = JdbcUtil.getString(rs, 1);
          t.dataType = JdbcUtil.getInt(rs, 2);
          t.caseSensitive = rs.getBoolean(8);
          if (rs.wasNull()) {
            t.caseSensitive = false;
          }
          Integer searchableValue = JdbcUtil.getIntObj(rs, 9);
          t.searchable = searchableValue != null && searchableValue.equals(DatabaseMetaData.typeSearchable);
          log.fine("DB TYPE: " + t);
          dataTypes.put(t.typeName, t);
        }
        return dataTypes;
      } finally {
        JdbcUtil.closeDbResources(rs);
      }
    }

    public String toString() {
      // ReflectionToStringBuilder a;
      return "typeName:" + this.typeName + " dataType=" + this.dataType + " caseSensitive=" + this.caseSensitive
          + " searchable=" + this.searchable;
    }

  }

}
