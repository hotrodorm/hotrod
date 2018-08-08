package org.hotrod.database;

import java.io.Serializable;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hotrod.config.ColumnTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.database.PropertyType.ValueRange;
import org.hotrod.exceptions.IdentitiesPostFetchNotSupportedException;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.utils.JdbcTypes;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrod.utils.identifiers.Identifier;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.utils.JdbcUtil;

public abstract class DatabaseAdapter implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger log = Logger.getLogger(DatabaseAdapter.class);

  protected transient DatabaseMetaData databaseMedaData;
  protected String identifierQuoteString;

  private Map<String, DataType> dataTypes;

  public static enum InsertIntegration {
    INTEGRATES_IDENTITIES_SEQUENCES_AND_DEFAULTS(true, true, true), //
    INTEGRATES_IDENTITIES_AND_SEQUENCES(true, true, false), //
    INTEGRATES_IDENTITIES(true, false, false), //
    INTEGRATES_SEQUENCES_AND_DEFAULTS(false, true, true), //
    RUDIMENTARY(false, false, false) //
    ;

    private boolean identities;
    private boolean sequences;
    private boolean defaults;

    private InsertIntegration(final boolean identities, final boolean sequences, final boolean defaults) {
      this.identities = identities;
      this.sequences = sequences;
      this.defaults = defaults;
    }

    public boolean integratesIdentities() {
      return this.identities;
    }

    public boolean integratesSequences() {
      return this.sequences;
    }

    public boolean integratesDefaults() {
      return this.defaults;
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

  /**
   * <pre>
   * 
   *   TableTag  -> JdbcTable
   *     |  ^         |  ^
   *     *  |         *  |
   *   ColumnTag -> JdbcColumn
   * 
   * </pre>
   */

  public PropertyType resolveJavaType(final ColumnMetadata md, final ColumnTag columnTag)
      throws UnresolvableDataTypeException {
    log.debug("columnTag=" + columnTag);
    if (columnTag == null || (columnTag.getJavaType() == null && columnTag.getConverterTag() == null)) {
      log.debug("No user-specified column type. Return adapter type.");
      return getAdapterDefaultType(md);
    } else {
      log.debug("User-specified column type. Use it.");
      JDBCType jdbcType;
      if (columnTag.getJdbcType() != null) {
        // User specified the JDBC type. Use the user's.
        jdbcType = JdbcTypes.nameToType(columnTag.getJdbcType());
        if (jdbcType == null) {
          throw new UnresolvableDataTypeException(md);
        }
      } else {
        // User did not specify the JDBC type. Get it from the live database.
        jdbcType = JdbcTypes.codeToType(md.getDataType());
        if (jdbcType == null) {
          throw new UnresolvableDataTypeException(md);
        }
      }
      ValueRange range = columnTag.getValueRange();
      if (range == null) {
        range = PropertyType.getDefaultValueRange(columnTag.getJavaType());
      }

      String javaType = columnTag.getJavaType() != null ? columnTag.getJavaType()
          : columnTag.getConverterTag().getJavaType();

      return new PropertyType(javaType, jdbcType, columnTag.isLOB(), range);
    }
  }

  // Names

  public boolean equalConfigNames(final String a, final String b) {
    if (a == null || b == null) {
      return false;
    }
    return this.canonizeName(a, false).equals(this.canonizeName(b, false));
  }

  public abstract String canonizeName(String configName, boolean quoted);

  public abstract String renderSQLName(String canonicalName);

  protected String quote(final String canonicalName) {
    return this.identifierQuoteString + canonicalName + this.identifierQuoteString;
  }

  // End of names

  public abstract boolean supportsCatalog();

  public abstract boolean supportsSchema();

  public abstract String getName();

  public abstract PropertyType getAdapterDefaultType(ColumnMetadata cm) throws UnresolvableDataTypeException;

  // Insert Behavior

  public abstract InsertIntegration getInsertIntegration();

  public abstract boolean integratesUsingQuery();

  public abstract String renderInsertQueryColumn(ColumnMetadata cm);

  public abstract String renderSequencesPrefetch(final List<ColumnMetadata> sequenceGeneratedColumns)
      throws SequencesNotSupportedException;

  public abstract String renderSelectSequence(final Identifier sequence) throws SequencesNotSupportedException;

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

  // Sorting

  /**
   * Default implementation
   */
  public boolean isCaseSensitiveSortableString(final ColumnMetadata cm) {
    DataType dataType = this.dataTypes.get(cm.getTypeName());
    log.debug("dataType=" + dataType);
    return dataType != null && dataType.caseSensitive && dataType.searchable;
  }

  public abstract String renderForCaseInsensitiveOrderBy(final ColumnMetadata cm);

  // Utilities

  protected PropertyType produceType(final Class<?> c, final ColumnMetadata m, final boolean isLOB)
      throws UnresolvableDataTypeException {
    JDBCType jdbcType = JdbcTypes.codeToType(m.getDataType());
    if (jdbcType == null) {
      throw new UnresolvableDataTypeException(m);
    }
    return new PropertyType(c.getName(), jdbcType, isLOB);
  }

  // Classes

  private static class DataType implements Serializable {

    private static final long serialVersionUID = 1L;

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
          log.debug("DB TYPE: " + t);
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
