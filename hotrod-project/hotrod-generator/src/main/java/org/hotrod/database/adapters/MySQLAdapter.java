package org.hotrod.database.adapters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
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
import org.hotrod.identifiers.ObjectId;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrod.utils.JdbcUtils;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.exception.CatalogNotSupportedException;
import org.nocrala.tools.database.tartarus.exception.InvalidCatalogException;
import org.nocrala.tools.database.tartarus.exception.InvalidSchemaException;
import org.nocrala.tools.database.tartarus.exception.SchemaNotSupportedException;

public class MySQLAdapter extends DatabaseAdapter {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(MySQLAdapter.class);

  public MySQLAdapter(final DatabaseMetaData dm) throws SQLException {
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
    return "MySQL Adapter";
  }

  @Override
  public PropertyType getAdapterDefaultType(final ColumnMetadata m) throws UnresolvableDataTypeException {

    log.debug("c.getDataType()=" + m.getDataType() + " (" + m.getTypeName() + ")");

    switch (m.getDataType()) {

    // Numeric types

    case java.sql.Types.DECIMAL:

      if ((m.getDecimalDigits() != null) && (m.getDecimalDigits() != 0)) {
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
      if (m.getTypeName().toUpperCase().contains("UNSIGNED")) {
        return new PropertyType(Short.class, m, false, ValueRange.UNSIGNED_BYTE_RANGE);
      } else {
        return new PropertyType(Byte.class, m, false, ValueRange.BYTE_RANGE);
      }

    case java.sql.Types.SMALLINT:
      if (m.getTypeName().toUpperCase().contains("UNSIGNED")) {
        return new PropertyType(Integer.class, m, false, ValueRange.UNSIGNED_SHORT_RANGE);
      } else {
        return new PropertyType(Short.class, m, false, ValueRange.SHORT_RANGE);
      }

    case java.sql.Types.INTEGER:
      if (m.getTypeName().toUpperCase().equals("MEDIUMINT")) {
        return new PropertyType(Integer.class, m, false, new ValueRange(0L, -128L * 256 * 256, 128L * 256 * 256 - 1));
      } else if (m.getTypeName().toUpperCase().equals("MEDIUMINT UNSIGNED")) {
        return new PropertyType(Integer.class, m, false, new ValueRange(0L, 0L, 256L * 256 * 256 - 1));
      } else if (m.getTypeName().toUpperCase().equals("INT")) {
        return new PropertyType(Integer.class, m, false, ValueRange.INTEGER_RANGE);
      } else if (m.getTypeName().toUpperCase().equals("INT UNSIGNED")) {
        return new PropertyType(Long.class, m, false, new ValueRange(0L, 0L, 256L * 256 * 256 * 256 - 1));
      }

    case java.sql.Types.BIGINT:
      if (m.getTypeName().toUpperCase().equals("BIGINT")) {
        return new PropertyType(Long.class, m, false, ValueRange.LONG_RANGE);
      } else if (m.getTypeName().toUpperCase().equals("BIGINT UNSIGNED")) {
        return new PropertyType(BigInteger.class, m, false);
      }

    case java.sql.Types.REAL:
    case java.sql.Types.FLOAT:
      return new PropertyType(Float.class, m, false);

    case java.sql.Types.DOUBLE:
      return new PropertyType(Double.class, m, false);

    case java.sql.Types.CHAR: // CHAR, ENUM, SET
      return new PropertyType(String.class, m, false);

    case java.sql.Types.VARCHAR: // VARCHAR, TINYTEXT
      boolean isLOB = "TINYTEXT".equalsIgnoreCase(m.getTypeName());
      return new PropertyType(String.class, m, isLOB);
    case java.sql.Types.LONGVARCHAR: // TEXT, MEDIUMTEXT, LONGTEXT
      return new PropertyType(String.class, m, true);

    case java.sql.Types.DATE:
      return new PropertyType(java.sql.Date.class, m, false);
    case java.sql.Types.TIME:
      return new PropertyType(java.sql.Time.class, m, false);
    case java.sql.Types.TIMESTAMP:
      return new PropertyType(java.sql.Timestamp.class, m, false);

    case java.sql.Types.BINARY: // TINYBLOB
      return new PropertyType("byte[]", m, true);

    case java.sql.Types.LONGVARBINARY: // BLOB, MEDIUMBLOB, LONGBLOB
      return new PropertyType("byte[]", m, true);

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
    return name == null ? false : name.equalsIgnoreCase(jdbcName);
  }

  @Override
  public boolean isColumnIdentifier(final String jdbcName, final String name) {
    return name == null ? false : name.equalsIgnoreCase(jdbcName);
  }

  @Override
  public String formatSchemaName(final String name) {
    return name;
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
    return tableName;
  }

  @Override
  public boolean isSerial(final JdbcColumn c) {
    if (c.getDataType() == Types.DECIMAL //
        && (c.getDecimalDigits() == null || c.getDecimalDigits().intValue() == 0) //
        && (c.getColumnSize() == null || c.getColumnSize().intValue() <= 18)) {
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
    if (c.getDataType() == Types.BIGINT && !"BIGINT UNSIGNED".equals(c.getTypeName().trim().toUpperCase())) {
      return true;
    }

    return false;
  }

  // Sorting

  private final static Set<String> CS_SORTABLE_STRING_TYPE_NAMES = new HashSet<String>();
  static {
    CS_SORTABLE_STRING_TYPE_NAMES.add("CHAR");
    CS_SORTABLE_STRING_TYPE_NAMES.add("VARCHAR");
    CS_SORTABLE_STRING_TYPE_NAMES.add("TINYTEXT");
    CS_SORTABLE_STRING_TYPE_NAMES.add("TEXT");
    CS_SORTABLE_STRING_TYPE_NAMES.add("MEDIUMTEXT");
    CS_SORTABLE_STRING_TYPE_NAMES.add("LONGTEXT");
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

  @Override
  public String provideSampleValueFor(final JDBCType jdbcType) {

//    select
//    1 as xbigint,
//    1.2 as xdecimal,
//    --
//    'a' as xvarchar,
//    date '2001-01-01' as xdate,
//    time '12:34:56' as xtime,  
//    timestamp '2001-01-01 12:34:56' as xtimestamp,
//    --
//    b'01' as xblob

    switch (jdbcType.getCode()) {

    case java.sql.Types.TINYINT:
      return "1";
    case java.sql.Types.SMALLINT:
      return "1";
    case java.sql.Types.INTEGER:
      return "1";
    case java.sql.Types.BIGINT:
      return "1";
    case java.sql.Types.REAL:
      return "1.2";
    case java.sql.Types.FLOAT: // JDBC equivalent to DOUBLE PRECISION
      return "1.2";
    case java.sql.Types.DOUBLE:
      return "1.2";
    case java.sql.Types.DECIMAL:
      return "1.2";
    case java.sql.Types.NUMERIC:
      return "1.2";

    case java.sql.Types.CHAR:
      return "'a'";
    case java.sql.Types.NCHAR:
      return "'a'";
    case java.sql.Types.VARCHAR:
      return "'a'";
    case java.sql.Types.NVARCHAR:
      return "'a'";
    case java.sql.Types.LONGVARCHAR:
      return "'a'";
    case java.sql.Types.LONGNVARCHAR:
      return "'a'";
    case java.sql.Types.CLOB:
      return "'a'";
    case java.sql.Types.NCLOB:
      return "'a'";

    case java.sql.Types.DATE:
      return "date '2001-01-01'";
    case java.sql.Types.TIME:
      return "time '12:34:56'";
    case java.sql.Types.TIMESTAMP:
      return "timestamp '2001-01-01 12:34:56'";

    case java.sql.Types.BLOB:
      return "b'01'";
    case java.sql.Types.BINARY:
      return "b'01'";
    case java.sql.Types.VARBINARY:
      return "b'01'";
    case java.sql.Types.LONGVARBINARY:
      return "b'01'";

//    case java.sql.Types.BOOLEAN:
//    case java.sql.Types.SQLXML:
//    case java.sql.Types.TIMESTAMP_WITH_TIMEZONE
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
      throws CatalogNotSupportedException, InvalidSchemaException, SQLException, InvalidCatalogException,
      SchemaNotSupportedException {
    if (catalog == null) {
      throw new InvalidCatalogException(JdbcUtils.getCatalogs(conn.getMetaData()));
    }
    if (schema != null) {
      throw new SchemaNotSupportedException();
    }
    JdbcUtils.runSQLStatement(conn, "use " + catalog);
  }

}
