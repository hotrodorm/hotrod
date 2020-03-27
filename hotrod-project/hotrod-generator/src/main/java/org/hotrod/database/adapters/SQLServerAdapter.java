package org.hotrod.database.adapters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.database.PropertyType.ValueRange;
import org.hotrod.exceptions.IdentitiesPostFetchNotSupportedException;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrod.utils.identifiers.ObjectId;
import org.hotrodorm.hotrod.utils.ListWriter;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;

public class SQLServerAdapter extends DatabaseAdapter {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(SQLServerAdapter.class);

  private static final long MAX_VARCHAR_LENGTH = 64 * 1024;

  public SQLServerAdapter(final DatabaseMetaData dm) throws SQLException {
    super(dm);
  }

  @Override
  public boolean supportsCatalog() {
    return true;
  }

  @Override
  public boolean supportsSchema() {
    return true;
  }

  @Override
  public String getName() {
    return "SQL Server Adapter";
  }

  @Override
  public PropertyType getAdapterDefaultType(final ColumnMetadata m) throws UnresolvableDataTypeException {

    log.debug("c.getDataType()=" + m.getDataType());

    boolean isLOB;

    switch (m.getDataType()) {

    // Numeric types

    case Types.DECIMAL: // DECIMAL, MONEY, SMALLMONEY
      if ((m.getDecimalDigits() != null) && (m.getDecimalDigits().intValue() != 0)) {
        return new PropertyType(BigDecimal.class, m, false);
      } else if (m.getColumnSize() <= 2) {
        return new PropertyType(Byte.class, m, false, ValueRange.getSignedRange(m.getColumnSize()));
      } else if (m.getColumnSize() <= 4) {
        return new PropertyType(Short.class, m, false, ValueRange.getSignedRange(m.getColumnSize()));
      } else if (m.getColumnSize() <= 9) {
        return new PropertyType(Integer.class, m, false, ValueRange.getSignedRange(m.getColumnSize()));
      } else if (m.getColumnSize() <= 18) {
        return new PropertyType(Long.class, m, false, ValueRange.getSignedRange(m.getColumnSize()));
      } else {
        return new PropertyType(BigInteger.class, m, false);
      }

    case Types.BIT:
      return new PropertyType(Byte.class, m, false);
    case Types.TINYINT:
      return new PropertyType(Byte.class, m, false, ValueRange.BYTE_RANGE);
    case Types.SMALLINT:
      return new PropertyType(Short.class, m, false, ValueRange.SHORT_RANGE);
    case Types.INTEGER:
      return new PropertyType(Integer.class, m, false, ValueRange.INTEGER_RANGE);
    case Types.BIGINT:
      return new PropertyType(Long.class, m, false, ValueRange.LONG_RANGE);

    case Types.REAL:
      return new PropertyType(Float.class, m, false);
    case Types.DOUBLE:
      return new PropertyType(Double.class, m, false);

    // Char types

    case Types.CHAR: // CHAR, UNIQUEIDENTIFIER
      return new PropertyType(String.class, m, false);
    case Types.VARCHAR:
      return new PropertyType(String.class, m, false);
    case Types.NCHAR:
      return new PropertyType(String.class, m, false);
    case Types.NVARCHAR:
      isLOB = m.getColumnSize() >= MAX_VARCHAR_LENGTH;
      return new PropertyType(String.class, m, isLOB);
    case Types.LONGVARCHAR: // TEXT
      isLOB = m.getColumnSize() >= MAX_VARCHAR_LENGTH;
      return new PropertyType(String.class, m, isLOB);
    case Types.LONGNVARCHAR: // NTEXT, XML
      // return new PropertyType(String.class, m); // LONGNVARCHAR is not
      // supported by MyBatis.
      isLOB = m.getColumnSize() >= MAX_VARCHAR_LENGTH;
      return new PropertyType(String.class, JDBCType.VARCHAR, isLOB);

    // Date/Time types

    case Types.DATE:
      return new PropertyType(java.sql.Date.class, m, false);
    case Types.TIME:
      if (m.getDecimalDigits() <= 3) {
        return new PropertyType(java.sql.Time.class, m, false);
      } else {
        return new PropertyType(java.sql.Timestamp.class, m, false);
      }
    case Types.TIMESTAMP: // DATETIME, DATETIME2, SMALLDATETIME
      return new PropertyType(java.sql.Timestamp.class, m, false);
    case -155: // DATETIMEOFFSET
      // Invalid JDBC type (-155) reported by the SQL Server JDBC Driver.
      return new PropertyType(java.sql.Timestamp.class, JDBCType.TIMESTAMP, false);

    // Binary types

    case Types.BINARY:
      if ("timestamp".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(Object.class, m, false);
      } else {
        return new PropertyType("byte[]", m, false);
      }
    case Types.VARBINARY: // VARBINARY, HIERARCHYID, GEOGRAPHY, GEOMETRY
      if ("hierarchyid".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType("byte[]", m, false);
      } else if ("geometry".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType("byte[]", m, false);
      } else if ("geography".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType("byte[]", m, false);
      } else {
        isLOB = m.getColumnSize() >= MAX_VARCHAR_LENGTH;
        return new PropertyType("byte[]", m, isLOB);
      }
    case Types.LONGVARBINARY:
      return new PropertyType("byte[]", m, true);

    // Other types

    case Types.OTHER:
      if ("DECFLOAT".equals(m.getTypeName())) {
        return new PropertyType(BigDecimal.class, m, false);
      } else if ("XML".equals(m.getTypeName())) {
        return new PropertyType(Object.class, m, false);
      } else {
        return new PropertyType(Object.class, m, false);
      }

    case -150: // SQL_VARIANT
      // Invalid JDBC type (-150) reported by the SQL Server JDBC Driver.
      return new PropertyType(Object.class, JDBCType.OTHER, false);

    default: // Unrecognized type
      return produceType(Object.class, m, false);

    }

  }

  @Override
  public InsertIntegration getInsertIntegration() {
    return InsertIntegration.INTEGRATES_IDENTITIES_SEQUENCES_AND_DEFAULTS;
    // return InsertIntegration.INTEGRATES_IDENTITIES;
  }

  @Override
  public boolean integratesUsingQuery() {
    return true;
  }

  @Override
  public String renderInsertQueryColumn(final ColumnMetadata cm) {
    return "inserted." + cm.getId().getRenderedSQLName() + " as " + cm.getId().getJavaMemberName();
  }

  @Override
  public String renderSequencesPrefetch(final List<ColumnMetadata> sequenceGeneratedColumns)
      throws SequencesNotSupportedException {
    ListWriter lw = new ListWriter(", ");
    for (ColumnMetadata cm : sequenceGeneratedColumns) {
      lw.add("next value for " + cm.getSequenceId().getRenderedSQLName() + " as " + cm.getId().getJavaMemberName());
    }
    return "select " + lw.toString();
  }

  @Override
  public String renderSelectSequence(final ObjectId sequenceId) throws SequencesNotSupportedException {
    return "select next value for " + sequenceId.getRenderedSQLName();
  }

  @Override
  public String renderInlineSequenceOnInsert(final ColumnMetadata cm) {
    return "next value for " + cm.getSequenceId().getRenderedSQLName();
  }

  @Override
  public String renderIdentitiesPostfetch(final List<ColumnMetadata> identityGeneratedColumns)
      throws IdentitiesPostFetchNotSupportedException {
    return "select scope_identity()";
  }

  @Override
  public String renderAliasedSelectColumn(final StructuredColumnMetadata cm) {
    return cm.getId().getRenderedSQLName() + " as " + this.renderSQLName(cm.getColumnAlias());
  }

  @Override
  public String canonizeName(final String configName, final boolean quoted) {
    return configName;
  }

  private static final String UNQUOTED_IDENTIFIER_PATTERN = "[A-Za-z][A-Za-z0-9_]*";

  @Override
  public String renderSQLName(final String canonicalName) {
    return canonicalName == null ? null
        : (canonicalName.matches(UNQUOTED_IDENTIFIER_PATTERN) ? canonicalName.toLowerCase()
            : super.quote(canonicalName));
  }

  @Override
  public boolean isTableIdentifier(final String jdbcName, final String name) {
    return name == null ? false : name.equalsIgnoreCase(jdbcName);
  }

  @Override
  public boolean isColumnIdentifier(final String jdbcName, final String name) {
    return name == null ? false : name.equalsIgnoreCase(jdbcName);
  }

  @Override
  public String formatSchemaName(final String name) {
    return name == null ? null : name.toUpperCase();
  }

  @Override
  public String createOrReplaceView(final String viewName, final String select) {
    return "create view " + viewName + " as\n" + select;
  }

  @Override
  public String dropView(final String viewName) {
    return "drop view " + viewName;
  }

  @Override
  public String formatJdbcTableName(final String tableName) {
    return tableName.toUpperCase();
  }

  @Override
  public boolean isSerial(final JdbcColumn c) {
    if (c.getDataType() == Types.DECIMAL && (c.getDecimalDigits() == null || c.getDecimalDigits().intValue() == 0)) {
      return true;
    }
    if (c.getDataType() == Types.TINYINT) {
      return true;
    }
    if (c.getDataType() == Types.SMALLINT) {
      return true;
    }
    if (c.getDataType() == Types.INTEGER) {
      return true;
    }
    if (c.getDataType() == Types.BIGINT) {
      return true;
    }
    return false;
  }

  // Sorting

  private final static Set<String> CS_SORTABLE_STRING_TYPE_NAMES = new HashSet<String>();
  static {
    CS_SORTABLE_STRING_TYPE_NAMES.add("char");
    CS_SORTABLE_STRING_TYPE_NAMES.add("varchar");
    CS_SORTABLE_STRING_TYPE_NAMES.add("nchar");
    CS_SORTABLE_STRING_TYPE_NAMES.add("nvarchar");
  }

  @Override
  public boolean isCaseSensitiveSortableString(final ColumnMetadata cm) {
    return CS_SORTABLE_STRING_TYPE_NAMES.contains(cm.getTypeName());
  }

  @Override
  public String renderForCaseInsensitiveOrderBy(final ColumnMetadata cm) {
    return "lower(" + cm.getId().getRenderedSQLName() + ")";
  }

  @Override
  public UnescapedSQLCase getUnescapedSQLCase() {
    return UnescapedSQLCase.ANY_CASE;
  }

}
