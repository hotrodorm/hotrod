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

public class DB2Adapter extends DatabaseAdapter {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(DB2Adapter.class);

  public DB2Adapter(final DatabaseMetaData dm) throws SQLException {
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
    return "DB2 Adapter";
  }

  @Override
  public PropertyType getAdapterDefaultType(final ColumnMetadata m) throws UnresolvableDataTypeException {

    log.debug("c.getDataType()=" + m.getDataType());

    switch (m.getDataType()) {

    case Types.DECIMAL:
      if ((m.getScale() != null) && (m.getScale().intValue() != 0)) {
        return new PropertyType(BigDecimal.class, m, false);
      } else if (m.getPrecision() <= 2) {
        return new PropertyType(Byte.class, m, false, ValueRange.getSignedRange(m.getPrecision()));
      } else if (m.getPrecision() <= 4) {
        return new PropertyType(Short.class, m, false, ValueRange.getSignedRange(m.getPrecision()));
      } else if (m.getPrecision() <= 9) {
        return new PropertyType(Integer.class, m, false, ValueRange.getSignedRange(m.getPrecision()));
      } else if (m.getPrecision() <= 18) {
        return new PropertyType(Long.class, m, false, ValueRange.getSignedRange(m.getPrecision()));
      } else {
        return new PropertyType(BigInteger.class, m, false);
      }

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

    case Types.CHAR:
      return new PropertyType(String.class, m, false);
    case Types.VARCHAR:
      return new PropertyType(String.class, m, false);
    case Types.LONGVARCHAR:
      return new PropertyType(String.class, m, false);

    case Types.CLOB:
      return new PropertyType(String.class, m, true);

    case Types.DATE:
      return new PropertyType(java.sql.Date.class, m, false);
    case Types.TIME:
      return new PropertyType(java.sql.Time.class, m, false);
    case Types.TIMESTAMP:
      return new PropertyType(java.sql.Timestamp.class, m, false);

    case Types.LONGVARBINARY:
      return new PropertyType("byte[]", m, false);
    case Types.VARBINARY:
      return new PropertyType("byte[]", m, false);
    case Types.BINARY:
      return new PropertyType("byte[]", m, false);
    case Types.BLOB:
      return new PropertyType("byte[]", m, true);

    case Types.OTHER:
      if ("DECFLOAT".equals(m.getTypeName())) {
        return new PropertyType(BigDecimal.class, m, false);
      } else if ("XML".equals(m.getTypeName())) {
        return new PropertyType(Object.class, m, false);
      } else {
        return new PropertyType(Object.class, m, false);
      }

    default: // Unrecognized type
      return produceType(Object.class, m, false);

    }

  }

  @Override
  public InsertIntegration getInsertIntegration() {
    return InsertIntegration.INTEGRATES_IDENTITIES_SEQUENCES_AND_DEFAULTS;
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
    return "select " + lw.toString() + " from sysibm.sysdummy1";
  }

  @Override
  public String renderSelectSequence(final ObjectId sequenceId) throws SequencesNotSupportedException {
    return "select next value for " + sequenceId.getRenderedSQLName() + " from sysibm.sysdummy1";
  }

  @Override
  public String renderInlineSequenceOnInsert(final ColumnMetadata cm) {
    return "next value for " + cm.getSequenceId().getRenderedSQLName();
  }

  @Override
  public String renderIdentitiesPostfetch(final List<ColumnMetadata> identityGeneratedColumns)
      throws IdentitiesPostFetchNotSupportedException {
    return "values identity_val_local()";
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
    return "lcase(" + cm.getId().getRenderedSQLName() + ")";
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
//    cast(1 as decimal(10)) as decimal,
//    cast(1 as numeric(10)) as numeric,
//    cast(1 as real) as real,
//    cast(1 as float) as xdouble,
//    --
//    cast('a' as char(1)) as char,
//    cast('a' as varchar(1)) as varchar,
//    cast(N'a' as nchar(1)) as nchar,
//    cast(N'a' as nvarchar(1)) as nvarchar,
//    cast('a' as text) as clob,
//    --
//    convert(date, '2001-01-01 12:34:56') as date,
//    convert(time, '2001-01-01 12:34:56') as time,
//    convert(timestamp, '2001-01-01 12:34:56') as timestamp,
//    --
//    cast('a' as binary) as blob

    switch (jdbcType.getCode()) {

    case java.sql.Types.TINYINT:
      return "cast(1 as smallint)";
    case java.sql.Types.SMALLINT:
      return "cast(1 as smallint)";
    case java.sql.Types.INTEGER:
      return "cast(1 as int)";
    case java.sql.Types.BIGINT:
      return "cast(1 as bigint)";
    case java.sql.Types.REAL:
      return "cast(1 as real)";
    case java.sql.Types.FLOAT: // JDBC equivalent to DOUBLE PRECISION
      return "cast(1 as double)";
    case java.sql.Types.DOUBLE:
      return "cast(1 as double)";
    case java.sql.Types.DECIMAL:
      return "cast(1 as decimal(10))";
    case java.sql.Types.NUMERIC:
      return "cast(1 as numeric(10))";

    case java.sql.Types.CHAR:
      return "cast('a' as char(1))";
    case java.sql.Types.NCHAR:
      return "cast(N'a' as nchar(1))";
    case java.sql.Types.VARCHAR:
      return "cast('a' as varchar(1))";
    case java.sql.Types.NVARCHAR:
      return "cast(N'a' as nvarchar(1))";
    case java.sql.Types.CLOB:
      return "cast('a' as clob(1))";

    case java.sql.Types.DATE:
      return "date '2001-01-01'";
    case java.sql.Types.TIME:
      return "time '12:34:56'";
    case java.sql.Types.TIMESTAMP:
      return "timestamp '2001-01-01 12:34:56'";

    case java.sql.Types.BLOB:
      return "blob('a')";

//    case java.sql.Types.BINARY:
//    case java.sql.Types.VARBINARY:
//    case java.sql.Types.LONGVARBINARY:
//    case java.sql.Types.BOOLEAN:
//    case java.sql.Types.LONGVARCHAR
//    case java.sql.Types.LONGNVARCHAR
//    case java.sql.Types.NCLOB
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
      JdbcUtils.runSQLStatement(conn, "set schema = " + schema);
    }
  }

}
