package org.hotrod.database.adapters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
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

public class PostgreSQLAdapter extends DatabaseAdapter {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(PostgreSQLAdapter.class);

  public PostgreSQLAdapter(final DatabaseMetaData dm) throws SQLException {
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
    return "PostgreSQL Adapter";
  }

  @Override
  public PropertyType getAdapterDefaultType(final ColumnMetadata m) throws UnresolvableDataTypeException {

    log.debug("c.getDataType()=" + m.getDataType());

    switch (m.getDataType()) {

    // Numeric Types

    case java.sql.Types.NUMERIC:
      if ("numeric".equalsIgnoreCase(m.getTypeName())) {
        if (m.getScale() == null || m.getScale() != 0) {
          return new PropertyType(BigDecimal.class, m, false);
        } else {
          if (m.getPrecision() <= 2) {
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
        }
      }
      break;

    case java.sql.Types.SMALLINT:
      if ("int2".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(Short.class, m, false, ValueRange.SHORT_RANGE);
      }
      break;

    case java.sql.Types.INTEGER:
      if ("int4".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(Integer.class, m, false, ValueRange.INTEGER_RANGE);
      } else if ("serial".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(Integer.class, m, false);
      }
      break;

    case java.sql.Types.BIGINT:
      if ("int8".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(Long.class, m, false, ValueRange.LONG_RANGE);
      } else if ("bigserial".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(Long.class, m, false);
      }
      break;

    case java.sql.Types.REAL:
      if ("float4".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(Float.class, m, false);
      }
      break;

    case java.sql.Types.DOUBLE:
      if ("float8".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(Double.class, m, false);
      } else if ("money".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(java.math.BigDecimal.class, m, false);
      }
      break;

    // Char Types

    case java.sql.Types.CHAR:
      if ("bpchar".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(String.class, m, false);
      }
      break;

    case java.sql.Types.VARCHAR:
      if ("varchar".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(String.class, m, false);
      } else if ("text".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(String.class, m, true);
      } else { // enum: Type Name is the enum name
        return new PropertyType(String.class, m, false);
      }

      // Date/Time Types

    case java.sql.Types.DATE:
      if ("date".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(java.sql.Date.class, m, false);
      }
      break;

    case java.sql.Types.TIME:
      if ("time".equals(m.getTypeName())) {
        return new PropertyType(java.sql.Timestamp.class, m, false);
      } else if ("timetz".equals(m.getTypeName())) {
        return new PropertyType(java.sql.Timestamp.class, m, false);
      }
      break;

    case java.sql.Types.TIMESTAMP:
      if ("timestamp".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(java.sql.Timestamp.class, m, false);
      } else if ("timestamptz".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(java.sql.Timestamp.class, m, false);
      }
      break;

    // Binary Types

    case java.sql.Types.BINARY:
      if ("bytea".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType("byte[]", m, true);
      }
      break;

    // Boolean Types

    case java.sql.Types.BIT:
      if ("bool".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(Boolean.class, m, false);
      } else if ("bit".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(java.lang.Object.class, m, false);
      }
      break;

    // Other Types

    case java.sql.Types.STRUCT:
      return new PropertyType(Object.class, m, false);

    case java.sql.Types.ARRAY:
      return new PropertyType(Object.class, m, false);

    case java.sql.Types.OTHER:
      if ("varbit".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType("byte[]", m, false);
      } else if ("uuid".equalsIgnoreCase(m.getTypeName())) {
        return new PropertyType(Object.class, m, false);
      } else {
        return new PropertyType(Object.class, m, false);
      }

    }

    // Unrecognized type

    return new PropertyType(Object.class, m, false);

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
      lw.add("nextval('" + cm.getSequenceId().getRenderedSQLName() + "') as " + cm.getId().getJavaMemberName());
    }
    return "select " + lw.toString();
  }

  @Override
  public String renderSelectSequence(final ObjectId sequenceId) throws SequencesNotSupportedException {
    return "select nextval('" + sequenceId.getRenderedSQLName() + "')";
  }

  @Override
  public String renderInlineSequenceOnInsert(final ColumnMetadata cm) {
    return "nextval('" + cm.getSequenceId().getRenderedSQLName() + "')";
  }

  @Override
  public String renderIdentitiesPostfetch(final List<ColumnMetadata> identityGeneratedColumns)
      throws IdentitiesPostFetchNotSupportedException {
    ListWriter lw = new ListWriter(", ");
    for (ColumnMetadata cm : identityGeneratedColumns) {
      lw.add("select currval(pg_get_serial_sequence('" + cm.getDataSet().getId().getCanonicalSQLName() + "', '"
          + cm.getId().getCanonicalSQLName() + "'))");
    }
    return "select " + lw.toString();
  }

  @Override
  public String renderAliasedSelectColumn(final StructuredColumnMetadata cm) {
    return cm.getId().getRenderedSQLName() + " as " + this.renderSQLName(cm.getColumnAlias(), false);
  }

  @Override
  public String canonizeName(final String configName, final boolean quoted) {
    return configName == null ? null : (quoted ? configName : configName.toLowerCase());
  }

  private static final String UNQUOTED_IDENTIFIER_PATTERN = "[a-z][a-z0-9_]*";

  @Override
  public String renderSQLName(final String canonicalName, final boolean isQuoted) {
    return canonicalName == null ? null
        : (!isQuoted && canonicalName.matches(UNQUOTED_IDENTIFIER_PATTERN) ? canonicalName
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
    return tableName.toLowerCase();
  }

  @Override
  public boolean isSerial(final JdbcColumn c) {
    if (c.getDataType() == java.sql.Types.BIGINT && "int8".equalsIgnoreCase(c.getTypeName())) {
      return true;
    }
    if (c.getDataType() == java.sql.Types.INTEGER && "int4".equalsIgnoreCase(c.getTypeName())) {
      return true;
    }
    if (c.getDataType() == java.sql.Types.SMALLINT && "int2".equalsIgnoreCase(c.getTypeName())) {
      return true;
    }
    return false;
  }

  // Sorting

  private final static Set<String> CS_SORTABLE_STRING_TYPE_NAMES = new HashSet<String>();
  static {
    CS_SORTABLE_STRING_TYPE_NAMES.add("bpchar"); // internal name of 'char'
    CS_SORTABLE_STRING_TYPE_NAMES.add("varchar");
    CS_SORTABLE_STRING_TYPE_NAMES.add("text");
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
    return UnescapedSQLCase.LOWER_CASE;
  }

  @Override
  public String provideSampleValueFor(final JDBCType jdbcType) {

//select
//1::smallint as smallint, 1::int as int, 1::bigint as bigint,
//1.0::real as float, 1.0::double precision as double, 1.0::decimal as decimal, 1::numeric as numeric,
//'a'::char as char, 'a'::varchar as varchar, 'a'::text as text, 
//date '2001-10-05' as date, '04:05'::time as time, '04:05'::time with time zone as timetz, 
//timestamp '2004-10-19 10:23:54' as timestamp, timestamptz '2004-10-19 10:23:54+02' as timestamptz,
//E'ab'::bytea as bytea, true as boolean, xmlcomment('a') as xml

    switch (jdbcType.getCode()) {

    case java.sql.Types.TINYINT:
      return "1::smallint";
    case java.sql.Types.SMALLINT:
      return "1::smallint";
    case java.sql.Types.INTEGER:
      return "1::int";
    case java.sql.Types.BIGINT:
      return "1::bigint";
    case java.sql.Types.REAL:
      return "1.0::float";
    case java.sql.Types.FLOAT: // JDBC equivalent to DOUBLE PRECISION
      return "1.0::double precision";
    case java.sql.Types.DOUBLE:
      return "1.0::double precision";
    case java.sql.Types.DECIMAL:
      return "1.0::decimal";
    case java.sql.Types.NUMERIC:
      return "1::numeric";

    case java.sql.Types.CHAR:
      return "'a'::char";
    case java.sql.Types.NCHAR:
      return "'a'::char";
    case java.sql.Types.VARCHAR:
      return "'a'::varchar";
    case java.sql.Types.NVARCHAR:
      return "'a'::varchar";
    case java.sql.Types.LONGVARCHAR:
      return "'a'::text";
    case java.sql.Types.LONGNVARCHAR:
      return "'a'::text";
    case java.sql.Types.CLOB:
      return "'a'::text";
    case java.sql.Types.NCLOB:
      return "'a'::text";

    case java.sql.Types.DATE:
      return "date '2001-10-05'";
    case java.sql.Types.TIME:
      return "'04:05'::time";
    case java.sql.Types.TIMESTAMP:
      return "timestamp '2004-10-19 10:23:54'";
    case java.sql.Types.TIMESTAMP_WITH_TIMEZONE:
      return "timestamptz '2004-10-19 10:23:54+02'";

    case java.sql.Types.BLOB:
      return "E'ab'::bytea";
    case java.sql.Types.BINARY:
      return "E'ab'::bytea";
    case java.sql.Types.VARBINARY:
      return "E'ab'::bytea";
    case java.sql.Types.LONGVARBINARY:
      return "E'ab'::bytea";

    case java.sql.Types.BOOLEAN:
      return "true";

    case java.sql.Types.SQLXML:
      return "xmlcomment('a')";

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
      JdbcUtils.runSQLStatement(conn, "set search_path = " + schema);
    }
  }

}
