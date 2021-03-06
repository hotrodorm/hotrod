package org.hotrod.database.adapters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.database.PropertyType.ValueRange;
import org.hotrod.exceptions.IdentitiesPostFetchNotSupportedException;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.exception.SchemaNotSupportedException;

public class SQLiteAdapter extends DatabaseAdapter {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(SQLiteAdapter.class);

  public SQLiteAdapter(final DatabaseMetaData dm) throws SQLException {
    super(dm);
  }

  @Override
  public boolean supportsCatalog() {
    return false;
  }

  @Override
  public boolean supportsSchema() {
    return false;
  }

  @Override
  public String getName() {
    return "SQLite Adapter";
  }

  @Override
  public PropertyType getAdapterDefaultType(final ColumnMetadata m) throws UnresolvableDataTypeException {

    log.debug("c.getDataType()=" + m.getDataType());

    switch (m.getDataType()) {

    // Numeric types

    case java.sql.Types.DECIMAL:
      if (m.getDecimalDigits() != null && m.getDecimalDigits() != 0) {
        return new PropertyType(BigDecimal.class, m, false);
      } else {
        if (m.getColumnSize() <= 2) {
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
      }

    case java.sql.Types.TINYINT:
      return new PropertyType(Byte.class, m, false, ValueRange.BYTE_RANGE);

    case java.sql.Types.SMALLINT:
      return new PropertyType(Short.class, m, false, ValueRange.SHORT_RANGE);

    case java.sql.Types.INTEGER:
      return new PropertyType(Integer.class, m, false, ValueRange.INTEGER_RANGE);

    case java.sql.Types.BIGINT:
      return new PropertyType(Long.class, m, false, ValueRange.LONG_RANGE);

    case java.sql.Types.DOUBLE:
      return new PropertyType(Double.class, m, false);

    case java.sql.Types.REAL:
      return new PropertyType(Float.class, m, false);

    // Character types

    case java.sql.Types.CHAR:
      return new PropertyType(String.class, m, false);

    case java.sql.Types.VARCHAR:
      return new PropertyType(String.class, m, false);

    case java.sql.Types.CLOB:
      return new PropertyType(String.class, m, true);

    // Date/Time types

    case java.sql.Types.DATE:
      return new PropertyType(java.sql.Date.class, m, false);
    case java.sql.Types.TIME:
      return new PropertyType(java.sql.Time.class, m, false);
    case java.sql.Types.TIMESTAMP:
      return new PropertyType(java.sql.Timestamp.class, m, false);

    // Binary

    case java.sql.Types.VARBINARY:
      return new PropertyType("byte[]", m, false);
    case java.sql.Types.BLOB:
      return new PropertyType("byte[]", m, true);

    // Boolean

    case java.sql.Types.BOOLEAN:
      return new PropertyType(Boolean.class, m, false);

    // Other

    case java.sql.Types.BINARY: // UUID
      // return new PropertyType(java.util.UUID.class, m, false);
      return new PropertyType("byte[]", m, false);

    case java.sql.Types.ARRAY: // ARRAY
      return new PropertyType("java.lang.Object[]", m, false);

    case java.sql.Types.OTHER:
      if ("timestamp with timezone".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(java.sql.Timestamp.class, m, false);

        // If the JDBC driver was 1.4.x (unstable as of Dec 2016) we could use:
        // return new PropertyType("org.h2.api.TimestampWithTimeZone", m,
        // false);

      } else {
        // return produceType(Object.class, m, false);
        return new PropertyType("byte[]", m, false);
      }

    default: // Unrecognized type
      return produceType(Object.class, m, false);

    }

  }

  @Override
  public InsertIntegration getInsertIntegration() {
    return InsertIntegration.RUDIMENTARY;
  }

  @Override
  public boolean integratesUsingQuery() {
    return false;
  }

  @Override
  public String renderInsertQueryColumn(final ColumnMetadata cm) {
    throw new UnsupportedOperationException("This database does not return insert values using a query.");
  }

  @Override
  public String renderSequencesPrefetch(final List<ColumnMetadata> sequenceGeneratedColumns)
      throws SequencesNotSupportedException {
    throw new UnsupportedOperationException("This database does not return support sequences.");
  }

  @Override
  public String renderSelectSequence(final ObjectId id) throws SequencesNotSupportedException {
    throw new UnsupportedOperationException("This database does not return support sequences.");
  }

  @Override
  public String renderInlineSequenceOnInsert(final ColumnMetadata cm) {
    throw new UnsupportedOperationException("This database does not return support sequences.");
  }

  @Override
  public String renderIdentitiesPostfetch(final List<ColumnMetadata> identityGeneratedColumns)
      throws IdentitiesPostFetchNotSupportedException {
    throw new UnsupportedOperationException("This database does not return support identities.");
  }

  @Override
  public String renderAliasedSelectColumn(final StructuredColumnMetadata cm) {
    return cm.getId().getRenderedSQLName() + " as " + this.renderSQLName(cm.getColumnAlias());
  }

  @Override
  public String canonizeName(final String configName, final boolean quoted) {
    return configName == null ? null : (quoted ? configName : configName.toUpperCase());
  }

  private static final String UNQUOTED_IDENTIFIER_PATTERN = "[A-Z][A-Z0-9_]*";

  @Override
  public String renderSQLName(final String canonicalName) {
    return canonicalName == null ? null
        : (canonicalName.matches(UNQUOTED_IDENTIFIER_PATTERN) ? canonicalName : super.quote(canonicalName));
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
    if (c.getDataType() == Types.DECIMAL && (c.getDecimalDigits() == null || c.getDecimalDigits().intValue() == 0)
        && c.getColumnSize() != null && c.getColumnSize() <= 18) {
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

  @Override
  public String renderForCaseInsensitiveOrderBy(final ColumnMetadata cm) {
    return "lower(" + cm.getId().getRenderedSQLName() + ")";
  }

  @Override
  public UnescapedSQLCase getUnescapedSQLCase() {
    return UnescapedSQLCase.UPPER_CASE;
  }

  @Override
  public String provideSampleValueFor(final JDBCType jdbcType) {
    return "?";
  }

  @Override
  public void setCurrentCatalogSchema(final Connection conn, final String catalog, final String schema)
      throws SchemaNotSupportedException {
    throw new SchemaNotSupportedException();
  }

}
