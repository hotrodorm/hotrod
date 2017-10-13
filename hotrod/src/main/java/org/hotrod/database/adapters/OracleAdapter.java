package org.hotrod.database.adapters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.database.PropertyType.ValueRange;
import org.hotrod.exceptions.IdentitiesPostFetchNotSupportedException;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.AllottedColumnMetadata;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrod.utils.identifiers.Identifier;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;

public class OracleAdapter extends DatabaseAdapter {

  private static Logger log = Logger.getLogger(OracleAdapter.class);

  private boolean isOracle12cOrNewer;

  public OracleAdapter(final HotRodConfigTag config, final DatabaseMetaData dm) throws SQLException {
    super(config, dm);
    this.isOracle12cOrNewer = this.isOracle12cOrNewer();
  }

  @Override
  public boolean supportsCatalog() {
    return false;
  }

  @Override
  public boolean supportsSchema() {
    return true;
  }

  @Override
  public String getName() {
    return "Oracle Adapter";
  }

  @Override
  public PropertyType getAdapterDefaultType(final ColumnMetadata m) throws UnresolvableDataTypeException {

    log.debug("cm.getDataType()=" + m.getDataType() + " (" + m.getTypeName() + ")");

    switch (m.getDataType()) {

    // Numeric types

    case Types.DECIMAL:
      if (m.getDecimalDigits() == null || m.getDecimalDigits().intValue() != 0) {
        return new PropertyType(BigDecimal.class, m, false);
      } else {
        if (m.getColumnSize() == null) {
          return new PropertyType(BigDecimal.class, m, false);
        } else {
          if (m.getColumnSize().intValue() <= 2) {
            return new PropertyType(Byte.class, m, false, ValueRange.getSignedRange(m.getColumnSize()));
          } else if (m.getColumnSize().intValue() <= 4) {
            return new PropertyType(Short.class, m, false, ValueRange.getSignedRange(m.getColumnSize()));
          } else if (m.getColumnSize().intValue() <= 9) {
            return new PropertyType(Integer.class, m, false, ValueRange.getSignedRange(m.getColumnSize()));
          } else if (m.getColumnSize().intValue() <= 18) {
            return new PropertyType(Long.class, m, false, ValueRange.getSignedRange(m.getColumnSize()));
          } else {
            return new PropertyType(BigInteger.class, m, false);
          }
        }
      }

    case 100: // binary_float
      // Invalid JDBC type (100) reported by the Oracle JDBC Driver.
      return new PropertyType(Float.class, JDBCType.NUMERIC, false);

    case 101: // binary_double
      // Invalid JDBC type (101) reported by the Oracle JDBC Driver.
      return new PropertyType(Double.class, JDBCType.NUMERIC, false);

    case Types.FLOAT: // float, real, double precision
      if ("real".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(BigDecimal.class, m, false);
      } else if ("double precision".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(Double.class, m, false);
      } else if ("float".equalsIgnoreCase(m.getTypeName())) {
        if (m.getColumnSize() <= 23) {
          return new PropertyType(Float.class, m, false);
        } else if (m.getColumnSize() <= 52) {
          return new PropertyType(Double.class, m, false);
        } else {
          return new PropertyType(BigDecimal.class, m, false);
        }
      }

      // Character types

    case Types.CHAR:
      return new PropertyType(String.class, m, false);

    case Types.VARCHAR:
      return new PropertyType(String.class, m, false);

    case Types.CLOB:
      return new PropertyType(String.class, m, true);

    case Types.LONGVARCHAR: // No default java type on HotRod yet.
      // byte[] nor String types do not work on MyBatis out of the box.
      throw new UnresolvableDataTypeException(m);

      // Date/time types

    case Types.TIMESTAMP:
      return "DATE".equalsIgnoreCase(m.getTypeName()) ? new PropertyType(java.util.Date.class, m, false)
          : new PropertyType(java.sql.Timestamp.class, m, false);

    case -101: // timestamp with time zone.
      // Invalid JDBC type (-101) reported by the Oracle JDBC Driver.
      return new PropertyType(java.sql.Timestamp.class, JDBCType.TIMESTAMP, false);

    case -102: // timestamp with local time zone.
      // Invalid JDBC type (-102) reported by the Oracle JDBC Driver.
      return new PropertyType(java.sql.Timestamp.class, JDBCType.TIMESTAMP, false);

    // Binary types

    case Types.VARBINARY:
      return new PropertyType("byte[]", m, true);

    case Types.LONGVARBINARY:
      return new PropertyType("byte[]", m, true);

    case Types.BLOB:
      return new PropertyType("byte[]", m, true);

    case -13: // BFILE
      // Invalid JDBC type (-13) reported by the Oracle JDBC Driver.
      // No default java type on HotRod yet.
      // byte[] type does not work on MyBatis out of the box.
      throw new UnresolvableDataTypeException(m);

      // Other

    case Types.SQLXML:
      // No default java type on HotRod yet.
      throw new UnresolvableDataTypeException(m);

    case -103: // interval year to month
      // Invalid JDBC type (-103) reported by the Oracle JDBC Driver.
      return new PropertyType(Object.class, JDBCType.OTHER, false);

    case -104: // interval day to second
      // Invalid JDBC type (-104) reported by the Oracle JDBC Driver.
      return new PropertyType(Object.class, JDBCType.OTHER, false);

    case Types.OTHER:

      // String types

      if ("NCHAR".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(String.class, m, false);
      } else if ("NVARCHAR2".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(String.class, m, false);
      } else if ("NCLOB".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(String.class, m, true);

      } else if ("URITYPE".equals(m.getTypeName())) {
        return new PropertyType(Object.class, m, false);
      } else if ("ROWID".equals(m.getTypeName())) {
        return new PropertyType(String.class, m, false);
      } else { // varray, struct, ref: fall here
        return new PropertyType(Object.class, m, false);
      }

    default: // Unrecognized type
      return produceType(Object.class, m, false);

    }

  }

  @Override
  public InsertIntegration getInsertIntegration() {
    return this.isOracle12cOrNewer ? InsertIntegration.INTEGRATES_IDENTITIES_SEQUENCES_AND_DEFAULTS
        : InsertIntegration.INTEGRATES_SEQUENCES_AND_DEFAULTS;
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
    ListWriter lw = new ListWriter(", ");
    for (ColumnMetadata cm : sequenceGeneratedColumns) {
      lw.add(cm.renderSQLSequence() + ".nextval as " + cm.getIdentifier().getJavaMemberIdentifier());
    }
    return "select " + lw.toString() + " from dual";
  }

  @Override
  public String renderSelectSequence(final Identifier sequence) throws SequencesNotSupportedException {
    return "select " + sequence.getSQLIdentifier() + ".nextval from dual";
  }

  @Override
  public String renderInlineSequenceOnInsert(final ColumnMetadata cm) {
    return cm.renderSQLSequence() + ".nextval";
  }

  @Override
  public String renderIdentitiesPostfetch(final List<ColumnMetadata> identityGeneratedColumns)
      throws IdentitiesPostFetchNotSupportedException {
    throw new IdentitiesPostFetchNotSupportedException("Oracle does not support post-fetch of identity value(s).");
  }

  @Override
  public String renderAliasedSelectColumn(final AllottedColumnMetadata cm) {
    return cm.renderSQLIdentifier() + " as " + this.renderSQLName(cm.getAlias());
  }

  // @Override
  // public boolean supportsJDBCGeneratedKeys() {
  // return false;
  // }

  // @Override
  // public String getAutoGeneratedKeySentence(final AutoGeneratedColumnMetadata
  // agcm)
  // throws AutogeneratedKeysNotSupportedException {
  //
  // if (agcm.isIdentity()) {
  // if (this.isOracle12cOrNewer) {
  // // As of version 12c, Oracle does not yet provide a way of retrieving
  // // the identity PK value.
  // return "select null from dual";
  // } else {
  // throw new AutogeneratedKeysNotSupportedException(
  // "Identity auto-generated key is not supported in Oracle prior to version
  // 12c. "
  // + "If you want to use a sequence, " + "please specify the sequence for the
  // table '"
  // + agcm.getDataSet().getIdentifier().getSQLIdentifier() + "'.");
  // }
  // }
  // return "select " + agcm.getSequence().trim() + ".nextval from dual";
  // }

  // @Override
  // public String renderSelectSequence(final Identifier identifier) throws
  // SequencesNotSupportedException {
  // return "select " + identifier.getSQLIdentifier().trim() + ".nextval from
  // dual";
  // }

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
    return "create or replace view " + viewName + " as " + select;
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
    if (c.getDataType() == Types.DECIMAL && c.getDecimalDigits() != null && c.getDecimalDigits().intValue() == 0) {
      return true;
    }
    return false;
  }

  // Sorting

  @Override
  public String renderForCaseInsensitiveOrderBy(final ColumnMetadata cm) {
    return "lower(" + cm.renderSQLIdentifier() + ")";
  }

  // Helpers

  private boolean isOracle12cOrNewer() throws SQLException {
    int majorVersion = this.databaseMedaData.getDatabaseMajorVersion();
    int minorVersion = this.databaseMedaData.getDatabaseMinorVersion();
    if (majorVersion < 12) {
      return false;
    }
    if (majorVersion > 12) {
      return true;
    }
    return minorVersion >= 1;
  }

}
