package org.hotrod.database.adapters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.database.PropertyType;
import org.hotrod.database.ValueRange;
import org.hotrod.exceptions.IdentitiesPostFetchNotSupportedException;
import org.hotrod.exceptions.SequencesNotSupportedException;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.livesql.queries.typesolver.TypeSource;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.JDBCTypes.JDBCType;
import org.hotrod.utils.JdbcUtils;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.exception.CatalogNotSupportedException;
import org.nocrala.tools.database.tartarus.exception.InvalidSchemaException;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class HyperSQLAdapter extends DatabaseAdapter {

  private static final Logger log = Logger.getLogger(HyperSQLAdapter.class.getName());

  private static final long MAX_VARCHAR_LENGTH = 64L * 1024;

  public HyperSQLAdapter(final DatabaseMetaData dm) throws SQLException {
    super(dm);
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
    return "HyperSQL Adapter";
  }

  @Override
  public PropertyType getDialectDefaultType(final ColumnMetadata m) throws UnresolvableDataTypeException {

    log.fine("c.getDataType()=" + m.getDataType());

    switch (m.getDataType()) {

    // Numeric types

    case java.sql.Types.DECIMAL:
    case java.sql.Types.NUMERIC:
      if (m.getScale() != null && m.getScale() != 0) {
        return new PropertyType(BigDecimal.class, m, false, TypeSource.STATIC_DIALECT_RULE, 1);
      } else {
        if (m.getPrecision() <= 2) {
          return new PropertyType(Byte.class, m, false, ValueRange.getSignedRange(m.getPrecision()),
              TypeSource.STATIC_DIALECT_RULE, 2);
        } else if (m.getPrecision() <= 4) {
          return new PropertyType(Short.class, m, false, ValueRange.getSignedRange(m.getPrecision()),
              TypeSource.STATIC_DIALECT_RULE, 3);
        } else if (m.getPrecision() <= 9) {
          return new PropertyType(Integer.class, m, false, ValueRange.getSignedRange(m.getPrecision()),
              TypeSource.STATIC_DIALECT_RULE, 4);
        } else if (m.getPrecision() <= 18) {
          return new PropertyType(Long.class, m, false, ValueRange.getSignedRange(m.getPrecision()),
              TypeSource.STATIC_DIALECT_RULE, 5);
        } else {
          return new PropertyType(BigInteger.class, m, false, TypeSource.STATIC_DIALECT_RULE, 6);
        }
      }

    case java.sql.Types.TINYINT:
      return new PropertyType(Byte.class, m, false, ValueRange.BYTE_RANGE, TypeSource.STATIC_DIALECT_RULE, 7);

    case java.sql.Types.SMALLINT:
      return new PropertyType(Short.class, m, false, ValueRange.SHORT_RANGE, TypeSource.STATIC_DIALECT_RULE, 8);

    case java.sql.Types.INTEGER:
      return new PropertyType(Integer.class, m, false, ValueRange.INTEGER_RANGE, TypeSource.STATIC_DIALECT_RULE, 9);

    case java.sql.Types.BIGINT:
      return new PropertyType(Long.class, m, false, ValueRange.LONG_RANGE, TypeSource.STATIC_DIALECT_RULE, 10);

    case java.sql.Types.FLOAT: // float is never reported
    case java.sql.Types.DOUBLE:
      return new PropertyType(Double.class, m, false, TypeSource.STATIC_DIALECT_RULE, 11);

    // Character types

    case java.sql.Types.CHAR:
      return new PropertyType(String.class, m, false, TypeSource.STATIC_DIALECT_RULE, 12);

    case java.sql.Types.VARCHAR:
      if (m.getTypeName() != null && m.getTypeName().toUpperCase().startsWith("INTERVAL")) {
        return new PropertyType(Object.class, m, false, TypeSource.STATIC_DIALECT_RULE, 13);
      } else {
        boolean isLOB = m.getPrecision() >= MAX_VARCHAR_LENGTH;
        return new PropertyType(String.class, m, isLOB, TypeSource.STATIC_DIALECT_RULE, 14);
      }

      // Date/Time types

    case java.sql.Types.DATE:
      return new PropertyType(java.time.LocalDate.class, m, false, TypeSource.STATIC_DIALECT_RULE, 15);
    case java.sql.Types.TIME:
      return new PropertyType(java.time.LocalTime.class, m, false, TypeSource.STATIC_DIALECT_RULE, 16);
    case java.sql.Types.TIMESTAMP:
      return new PropertyType(java.time.LocalDateTime.class, m, false, TypeSource.STATIC_DIALECT_RULE, 17);

    case java.sql.Types.BOOLEAN:
      return new PropertyType(Boolean.class, m, false, TypeSource.STATIC_DIALECT_RULE, 18);

    case java.sql.Types.BLOB:
    case java.sql.Types.BINARY:
    case java.sql.Types.VARBINARY:
      return new PropertyType("byte[]", m, true, TypeSource.STATIC_DIALECT_RULE, 19);

    case java.sql.Types.CLOB:
      return new PropertyType(String.class, m, true, TypeSource.STATIC_DIALECT_RULE, 20);

    case java.sql.Types.OTHER:
      return produceType(Object.class, m, false, m.getResolvedConverter(), 21);

    case java.sql.Types.BIT:
    case java.sql.Types.ARRAY:
      return produceType(Object.class, m, false, m.getResolvedConverter(), 22);

    default: // Unrecognized type
      return produceType(Object.class, m, false, m.getResolvedConverter(), 23);

    }

  }

  @Override
  public InsertIntegration getInsertIntegration() {
    return InsertIntegration.of(true, true, true, false, null, false);
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
      lw.add("next value for " + cm.getSequenceId().getRenderedSQLName() + " as " + cm.getId().getJavaMemberName());
    }
    return "select " + lw.toString() + " from (values (0))";
  }

  @Override
  public String renderSelectSequence(final ObjectId sequenceId) throws SequencesNotSupportedException {
    return "SELECT NEXT VALUE FOR " + sequenceId.getRenderedSQLName() + " FROM (VALUES (0))";
  }

  @Override
  public String renderInlineSequenceOnInsert(final ColumnMetadata cm) {
    return "NEXT VALUE FOR " + cm.getSequenceId().getRenderedSQLName();
  }

  @Override
  public String renderIdentitiesPostfetch(final List<ColumnMetadata> identityGeneratedColumns)
      throws IdentitiesPostFetchNotSupportedException {
    return "call identity()";
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
  public boolean canonicalNameRequiresQuoting(String canonicalSQLName) {
    return !canonicalSQLName.matches(UNQUOTED_IDENTIFIER_PATTERN);
  }

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
  public String currentTimestampSQLExpression() {
    return "CURRENT_TIMESTAMP";
  }

  @Override
  public String provideSampleValueFor(final JDBCType jdbcType) {

//    select
//    cast(1 as tinyint) as tinyint,
//    cast(1 as smallint) as smallint,
//    cast(1 as int) as int,
//    cast(1 as bigint) as bigint,
//    cast(1 as float) as real,
//    cast(1 as double) as double,
//    cast(1 as decimal) as decimal,
//    cast(1 as numeric) as numeric,
//    cast('a' as char) as char,
//    cast('a' as varchar(1)) as varchar,
//    cast('a' as clob) as clob,
//    date '2001-10-05' as date, 
//    time '04:05:06' as time,
//    timestamp '2004-10-19 10:23:54' as timestamp,
//    x'01' as b1,
//    true as xboolean
//    from (values 1) x;

    switch (jdbcType.getCode()) {

    case java.sql.Types.TINYINT:
      return "cast(1 as tinyint)";
    case java.sql.Types.SMALLINT:
      return "cast(1 as smallint)";
    case java.sql.Types.INTEGER:
      return "cast(1 as int)";
    case java.sql.Types.BIGINT:
      return "cast(1 as bigint)";
    case java.sql.Types.REAL:
      return "cast(1 as double)";
    case java.sql.Types.FLOAT: // JDBC equivalent to DOUBLE PRECISION
      return "cast(1 as double)";
    case java.sql.Types.DOUBLE:
      return "cast(1 as double)";
    case java.sql.Types.DECIMAL:
      return "cast(1 as decimal)";
    case java.sql.Types.NUMERIC:
      return "cast(1 as numeric)";

    case java.sql.Types.CHAR:
      return "cast('a' as char)";
    case java.sql.Types.NCHAR:
      return "cast('a' as char)";
    case java.sql.Types.VARCHAR:
      return "cast('a' as varchar(1))";
    case java.sql.Types.NVARCHAR:
      return "cast('a' as varchar(1))";
    case java.sql.Types.LONGVARCHAR:
      return "cast('a' as clob)";
    case java.sql.Types.LONGNVARCHAR:
      return "cast('a' as clob)";
    case java.sql.Types.CLOB:
      return "cast('a' as clob)";
    case java.sql.Types.NCLOB:
      return "cast('a' as clob)";

    case java.sql.Types.DATE:
      return "date '2001-10-05'";
    case java.sql.Types.TIME:
      return "time '04:05:06'";
    case java.sql.Types.TIMESTAMP:
      return "timestamp '2004-10-19 10:23:54'";

    case java.sql.Types.BLOB:
      return "x'01'";
    case java.sql.Types.BINARY:
      return "x'01'";
    case java.sql.Types.VARBINARY:
      return "x'01'";
    case java.sql.Types.LONGVARBINARY:
      return "x'01'";

    case java.sql.Types.BOOLEAN:
      return "true";

//    case java.sql.Types.TIMESTAMP_WITH_TIMEZONE
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
      JdbcUtils.runSQLStatement(conn, "set schema " + schema);
    }
  }

}
