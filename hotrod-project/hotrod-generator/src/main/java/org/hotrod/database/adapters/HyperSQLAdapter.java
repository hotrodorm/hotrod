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
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class HyperSQLAdapter extends DatabaseAdapter {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(HyperSQLAdapter.class);

  private static final long MAX_VARCHAR_LENGTH = 64 * 1024;

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
  public PropertyType getAdapterDefaultType(final ColumnMetadata m) throws UnresolvableDataTypeException {

    log.debug("c.getDataType()=" + m.getDataType());

    switch (m.getDataType()) {

    // Numeric types

    case java.sql.Types.DECIMAL:
    case java.sql.Types.NUMERIC:
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

    case java.sql.Types.FLOAT: // float is never reported
    case java.sql.Types.DOUBLE:
      return new PropertyType(Double.class, m, false);

    // Character types

    case java.sql.Types.CHAR:
      return new PropertyType(String.class, m, false);

    case java.sql.Types.VARCHAR:
      if (m.getTypeName() != null && m.getTypeName().toUpperCase().startsWith("INTERVAL")) {
        return new PropertyType(Object.class, m, false);
      } else {
        boolean isLOB = m.getColumnSize() >= MAX_VARCHAR_LENGTH;
        return new PropertyType(String.class, m, isLOB);
      }

      // Date/Time types

    case java.sql.Types.DATE:
      return new PropertyType(java.sql.Date.class, m, false);
    case java.sql.Types.TIME:
      return new PropertyType(java.sql.Time.class, m, false);
    case java.sql.Types.TIMESTAMP:
      return new PropertyType(java.sql.Timestamp.class, m, false);

    case java.sql.Types.BOOLEAN:
      return new PropertyType(Boolean.class, m, false);

    case java.sql.Types.BLOB:
    case java.sql.Types.BINARY:
    case java.sql.Types.VARBINARY:
      return new PropertyType("byte[]", m, true);

    case java.sql.Types.CLOB:
      return new PropertyType(String.class, m, true);

    case java.sql.Types.OTHER:
      return produceType(Object.class, m, false);

    case java.sql.Types.BIT:
    case java.sql.Types.ARRAY:
      return produceType(Object.class, m, false);

    default: // Unrecognized type
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
    ListWriter lw = new ListWriter(", ");
    for (ColumnMetadata cm : sequenceGeneratedColumns) {
      lw.add("next value for " + cm.getSequenceId().getRenderedSQLName() + " as " + cm.getId().getJavaMemberName());
    }
    return "select " + lw.toString() + " from (values (0))";
  }

  @Override
  public String renderSelectSequence(final ObjectId sequenceId) throws SequencesNotSupportedException {
    return "select next value for " + sequenceId.getRenderedSQLName() + " from (values (0))";
  }

  @Override
  public String renderInlineSequenceOnInsert(final ColumnMetadata cm) {
    return "next value for " + cm.getSequenceId().getRenderedSQLName();
  }

  @Override
  public String renderIdentitiesPostfetch(final List<ColumnMetadata> identityGeneratedColumns)
      throws IdentitiesPostFetchNotSupportedException {
    return "call identity()";
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
      return "cast(1 as tinyint";
    case java.sql.Types.SMALLINT:
      return "cast(1 as smallint)";
    case java.sql.Types.INTEGER:
      return "cast(1 as int)";
    case java.sql.Types.BIGINT:
      return "cast(1 as bigint)";
    case java.sql.Types.FLOAT:
      return "cast(1 as float)";
    case java.sql.Types.REAL:
      return "cast(1 as float)";
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
      return "cast('a' as clob) as clob";
    case java.sql.Types.LONGNVARCHAR:
      return "cast('a' as clob) as clob";
    case java.sql.Types.CLOB:
      return "cast('a' as clob) as clob";
    case java.sql.Types.NCLOB:
      return "cast('a' as clob) as clob";

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

}
