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
import org.hotrod.identifiers.ObjectId;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.runtime.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrod.utils.JdbcUtils;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.exception.CatalogNotSupportedException;
import org.nocrala.tools.database.tartarus.exception.InvalidSchemaException;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class OracleAdapter extends DatabaseAdapter {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(OracleAdapter.class);

  private boolean isOracle12cOrNewer;

  public OracleAdapter(final DatabaseMetaData dm) throws SQLException {
    super(dm);
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
    case Types.NUMERIC:
      if (m.getScale() == null || m.getScale().intValue() != 0) {
        return new PropertyType(BigDecimal.class, m, false);
      } else {
        if (m.getPrecision() == null) {
          return new PropertyType(BigDecimal.class, m, false);
        } else {
          if (m.getPrecision().intValue() <= 2) {
            return new PropertyType(Byte.class, m, false, ValueRange.getSignedRange(m.getPrecision()));
          } else if (m.getPrecision().intValue() <= 4) {
            return new PropertyType(Short.class, m, false, ValueRange.getSignedRange(m.getPrecision()));
          } else if (m.getPrecision().intValue() <= 9) {
            return new PropertyType(Integer.class, m, false, ValueRange.getSignedRange(m.getPrecision()));
          } else if (m.getPrecision().intValue() <= 18) {
            return new PropertyType(Long.class, m, false, ValueRange.getSignedRange(m.getPrecision()));
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
        if (m.getPrecision() <= 23) {
          return new PropertyType(Float.class, m, false);
        } else if (m.getPrecision() <= 52) {
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
      lw.add(cm.getSequenceId().getRenderedSQLName() + ".nextval as " + cm.getId().getJavaMemberName());
    }
    return "select " + lw.toString() + " from dual";
  }

  @Override
  public String renderSelectSequence(final ObjectId sequenceId) throws SequencesNotSupportedException {
    return "select " + sequenceId.getRenderedSQLName() + ".nextval from dual";
  }

  @Override
  public String renderInlineSequenceOnInsert(final ColumnMetadata cm) {
    return cm.getSequenceId().getRenderedSQLName() + ".nextval";
  }

  @Override
  public String renderIdentitiesPostfetch(final List<ColumnMetadata> identityGeneratedColumns)
      throws IdentitiesPostFetchNotSupportedException {
    throw new IdentitiesPostFetchNotSupportedException("Oracle does not support post-fetch of identity value(s).");
  }

  @Override
  public String renderAliasedSelectColumn(final StructuredColumnMetadata cm) {
    return cm.getId().getRenderedSQLName() + " as " + this.renderSQLName(cm.getColumnAlias(), false);
  }

  @Override
  public String canonizeName(final String configName, final boolean quoted) {
    return configName == null ? null : (quoted ? configName : configName.toUpperCase());
  }

  private static final String UNQUOTED_IDENTIFIER_PATTERN = "[A-Z][A-Z0-9_]*";

  @Override
  public String renderSQLName(final String canonicalName, final boolean isQuoted) {
    return canonicalName == null ? null
        : (!isQuoted && canonicalName.matches(UNQUOTED_IDENTIFIER_PATTERN) ? canonicalName.toLowerCase()
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
    return "create or replace view " + viewName + " as\n" + select;
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
    return "lower(" + cm.getId().getRenderedSQLName() + ")";
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

  @Override
  public UnescapedSQLCase getUnescapedSQLCase() {
    return UnescapedSQLCase.UPPER_CASE;
  }

  @Override
  public String provideSampleValueFor(final JDBCType jdbcType) {

//    select
//    cast(1 as number(2)) as xtinyint,
//    cast(1 as number(4)) as ssmallint,
//    cast(1 as number(9)) as xint,
//    cast(1 as number(18)) as xbigint,
//    cast(1 as float) as xfloat,
//    cast(1 as real) as xreal,
//    cast(1 as double precision) as xdouble,
//    cast(1 as decimal(10)) as xdecimal,
//    cast(1 as numeric(10)) as xnumeric,
//    --
//    cast('a' as char(1)) as xchar,
//    cast(N'a' as nchar(1)) as xnchar,
//    cast('a' as varchar2(1)) as xvarchar,
//    cast(N'a' as nvarchar2(1)) as xnvarchar,
//    empty_clob() as xclob,
//    --
//    date '2001-01-01' as xtimestamp,
//    timestamp '2001-01-01 12:34:56' as xtimestamp,
//    timestamp '2001-01-01 12:34:56 -08:00' xtimestamptz,
//    --
//    empty_blob() as xblob
//  from dual;

    switch (jdbcType.getCode()) {

    case java.sql.Types.TINYINT:
      return "cast(1 as number(2))";
    case java.sql.Types.SMALLINT:
      return "cast(1 as number(4))";
    case java.sql.Types.INTEGER:
      return "cast(1 as number(9))";
    case java.sql.Types.BIGINT:
      return "cast(1 as number(18))";
    case java.sql.Types.REAL:
      return "cast(1 as real)";
    case java.sql.Types.FLOAT: // JDBC equivalent to DOUBLE PRECISION
      return "cast(1 as double precision)";
    case java.sql.Types.DOUBLE:
      return "cast(1 as double precision)";
    case java.sql.Types.DECIMAL:
      return "cast(1 as decimal(10))";
    case java.sql.Types.NUMERIC:
      return "cast(1 as numeric(10))";

    case java.sql.Types.CHAR:
      return "cast('a' as char(1))";
    case java.sql.Types.NCHAR:
      return "cast(N'a' as nchar(1))";
    case java.sql.Types.VARCHAR:
      return "cast('a' as varchar2(1))";
    case java.sql.Types.NVARCHAR:
      return "cast(N'a' as nvarchar2(1))";
    case java.sql.Types.CLOB:
      return "empty_clob()";

    case java.sql.Types.DATE:
      return "date '2001-01-01'";
    case java.sql.Types.TIMESTAMP:
      return "timestamp '2001-01-01 12:34:56'";
    case java.sql.Types.TIMESTAMP_WITH_TIMEZONE:
      return "timestamp '2001-01-01 12:34:56 -08:00'";

    case java.sql.Types.BLOB:
      return "empty_blob()";

//    case java.sql.Types.BINARY:
//    case java.sql.Types.VARBINARY:
//    case java.sql.Types.LONGVARBINARY:
//    case java.sql.Types.TIME
//    case java.sql.Types.LONGVARCHAR
//    case java.sql.Types.LONGNVARCHAR
//    case java.sql.Types.NCLOB
//    case java.sql.Types.BOOLEAN
//    case java.sql.Types.SQLXML
//    case java.sql.Types.TIME_WITH_TIMEZONE -- should be removed from the SQL Standard
//    case java.sql.Types.ARRAY
//    case java.sql.Types.BIT
//    case java.sql.Types.DATALINK
//    case java.sql.Types.DISTINCT
//    case java.sql.Types.JAVA_OBJECT
//    case java.sql.Types.NULL
//    case java.sql.Types.OTHER
//    case java.sql.Types.REF
//    case java.sql.Types.REF_CURSOR
//    case java.sql.Types.ROWID
//    case java.sql.Types.STRUCT

    }

    return null;
  }

  @Override
  public void setCurrentCatalogSchema(final Connection conn, final String catalog, final String schema)
      throws CatalogNotSupportedException, InvalidSchemaException, SQLException {
    if (catalog != null) {
      throw new CatalogNotSupportedException();
    }
    if (schema == null) {
      throw new InvalidSchemaException(JdbcUtils.getSchemas(conn.getMetaData(), catalog));
    } else {
      JdbcUtils.runSQLStatement(conn, "alter session set current_schema = " + schema);
    }
  }

}
