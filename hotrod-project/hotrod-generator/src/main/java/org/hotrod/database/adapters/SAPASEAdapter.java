package org.hotrod.database.adapters;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrod.utils.identifiers.ObjectId;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;

public class SAPASEAdapter extends DatabaseAdapter {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(SAPASEAdapter.class);

  public SAPASEAdapter(final DatabaseMetaData dm) throws SQLException {
    super(dm);
  }

  @Override
  public boolean supportsCatalog() {
    return true;
  }

  @Override
  public boolean supportsSchema() {
    return false;
  }

  @Override
  public String getName() {
    return "SAP ASE (Sybase) Adapter";
  }

  @Override
  public PropertyType getAdapterDefaultType(final ColumnMetadata m) throws UnresolvableDataTypeException {

    log.debug("c.getDataType()=" + m.getDataType() + " (" + m.getColumnSize() + ", " + m.getDecimalDigits() + ")");

    switch (m.getDataType()) {

    // Decimal, numeric and money types

    case Types.DECIMAL:

      if (m.getTypeName().equalsIgnoreCase("money") || m.getTypeName().equalsIgnoreCase("smallmoney")) {
        return new PropertyType(BigDecimal.class, m, false);
      } else if ((m.getDecimalDigits() != null) && (m.getDecimalDigits().intValue() != 0)) {
        return new PropertyType(BigDecimal.class, m, false);
      } else {
        if (m.getColumnSize() == null) {
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

      }

      // Integer types

    case Types.TINYINT:
      return new PropertyType(Byte.class, m, false, ValueRange.BYTE_RANGE);

    case Types.SMALLINT:
      return m.getTypeName().startsWith("unsigned") ? //
          new PropertyType(Integer.class, m, false, ValueRange.INTEGER_RANGE) : //
          new PropertyType(Short.class, m, false, ValueRange.SHORT_RANGE);

    case Types.INTEGER:
      return m.getTypeName().startsWith("unsigned") ? //
          new PropertyType(Long.class, m, false, ValueRange.LONG_RANGE) : //
          new PropertyType(Integer.class, m, false, ValueRange.INTEGER_RANGE);

    case Types.BIGINT:
      return m.getTypeName().startsWith("unsigned") ? //
          new PropertyType(BigInteger.class, m, false) : //
          new PropertyType(Long.class, m, false, ValueRange.LONG_RANGE);

    // Floating point types

    case Types.REAL: // FLOAT, REAL, DOUBLE PRECISION
    case Types.DOUBLE:
      return new PropertyType(Double.class, m, false);

    // Character types

    case Types.CHAR:
      return new PropertyType(String.class, m, false);

    case Types.VARCHAR:
      return new PropertyType(String.class, m, false);

    case Types.LONGVARCHAR:
      return new PropertyType(String.class, m, true);

    // Bit type

    case Types.BIT:
      return new PropertyType(Byte.class, m, false);

    // Date/Time types

    case Types.DATE:
      return new PropertyType(java.sql.Date.class, m, false);

    case Types.TIMESTAMP:
      return new PropertyType(java.sql.Timestamp.class, m, false);

    case 10: // BIGTIME
      // Invalid JDBC type (10) reported by the SAP ASE JDBC Driver.
      return new PropertyType(java.sql.Timestamp.class, JDBCType.TIMESTAMP, false);

    case 11: // BIGDATETIME
      // Invalid JDBC type (11) reported by the SAP ASE JDBC Driver.
      return new PropertyType(java.sql.Timestamp.class, JDBCType.TIMESTAMP, false);

    case Types.TIME:
      return new PropertyType(java.sql.Timestamp.class, m, false);

    // LOB types

    case Types.BINARY: // BINARY
      return new PropertyType("byte[]", m, false);

    case Types.VARBINARY: // VARBINARY
      return new PropertyType("byte[]", m, false);

    case Types.LONGVARBINARY: // IMAGE
      return new PropertyType("byte[]", m, true);

    // If not found.

    default:
      return produceType(Object.class, m, false);

    }
  }

  @Override
  public InsertIntegration getInsertIntegration() {
    return InsertIntegration.INTEGRATES_IDENTITIES;
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
    throw new SequencesNotSupportedException("Sequence-generated columns are not supported by this database.");
  }

  @Override
  public String renderSelectSequence(final ObjectId id) throws SequencesNotSupportedException {
    throw new SequencesNotSupportedException("Sequence-generated columns are not supported by this database.");
  }

  @Override
  public String renderInlineSequenceOnInsert(final ColumnMetadata cm) throws SequencesNotSupportedException {
    throw new SequencesNotSupportedException("Sequence-generated columns are not supported by this database.");
  }

  @Override
  public String renderIdentitiesPostfetch(final List<ColumnMetadata> identityGeneratedColumns)
      throws IdentitiesPostFetchNotSupportedException {
    return "select @@identity";
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
        : (canonicalName.matches(UNQUOTED_IDENTIFIER_PATTERN) ? canonicalName : super.quote(canonicalName));
  }

  @Override
  public boolean isTableIdentifier(final String jdbcName, final String name) {
    return name == null ? false : name.equals(jdbcName);
  }

  @Override
  public boolean isColumnIdentifier(final String jdbcName, final String name) {
    return name == null ? false : name.equals(jdbcName);
  }

  @Override
  public String formatSchemaName(final String name) {
    return name == null ? null : name;
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
    return tableName;
  }

  @Override
  public boolean isSerial(final JdbcColumn c) {
    if (c.getDataType() == Types.BIGINT) {
      // Can be, as long as it corresponds to a java Long (i.e. not unsigned)
      return !c.getTypeName().startsWith("unsigned");
    }
    if (c.getDataType() == Types.INTEGER) {
      return true;
    }
    if (c.getDataType() == Types.SMALLINT) {
      return true;
    }
    if (c.getDataType() == Types.TINYINT) {
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
    return UnescapedSQLCase.ANY_CASE;
  }

}
