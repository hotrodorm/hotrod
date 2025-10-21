package org.hotrod.livesql.dialects;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.livesql.exceptions.InvalidLiteralException;
import org.hotrod.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeFieldExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.ordering.OrderingTerm;
import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.select.CrossJoin;
import org.hotrod.livesql.queries.select.FullOuterJoin;
import org.hotrod.livesql.queries.select.InnerJoin;
import org.hotrod.livesql.queries.select.Join;
import org.hotrod.livesql.queries.select.JoinLateral;
import org.hotrod.livesql.queries.select.LeftJoinLateral;
import org.hotrod.livesql.queries.select.LeftOuterJoin;
import org.hotrod.livesql.queries.select.NaturalFullOuterJoin;
import org.hotrod.livesql.queries.select.NaturalInnerJoin;
import org.hotrod.livesql.queries.select.NaturalLeftOuterJoin;
import org.hotrod.livesql.queries.select.NaturalRightOuterJoin;
import org.hotrod.livesql.queries.select.RightOuterJoin;
import org.hotrod.livesql.queries.select.UnionJoin;
import org.hotrod.livesql.queries.typesolver.ResultSetColumnMetadata;
import org.hotrod.utils.Separator;

public class SybaseASEDialect extends LiveSQLDialect {

  public SybaseASEDialect(final boolean discovered, final String productName, final String productVersion,
      final int majorVersion, final int minorVersion) {
    super(discovered, productName, productVersion, majorVersion, minorVersion);
  }

  @Override
  public void enableSelectStreaming(PreparedStatement ps, Integer fetchSize) throws SQLException {
    // Streaming not available
    if (fetchSize != null) {
      ps.setFetchSize(fetchSize);
    }
  }

  @Override
  public RuntimeType resolveRuntimeType(final ResultSetColumnMetadata m) {

    switch (m.getColumnType()) {

    // Decimal, numeric and money types

    case Types.DECIMAL:

      if (m.getColumnTypeName().equalsIgnoreCase("money") || m.getColumnTypeName().equalsIgnoreCase("smallmoney")) {
        return RuntimeType.ofDialect(BigDecimal.class, 8);
      } else if ((m.getScale() != 0)) {
        return RuntimeType.ofDialect(BigDecimal.class, 1);
      } else {
        if (m.getPrecision() <= 2) {
          return RuntimeType.ofDialect(Byte.class, 3);
        } else if (m.getPrecision() <= 4) {
          return RuntimeType.ofDialect(Short.class, 4);
        } else if (m.getPrecision() <= 9) {
          return RuntimeType.ofDialect(Integer.class, 5);
        } else if (m.getPrecision() <= 18) {
          return RuntimeType.ofDialect(Long.class, 6);
        } else {
          return RuntimeType.ofDialect(BigInteger.class, 7);
        }

      }

      // Integer types

    case Types.BIT: // -7
      // BIT
      return RuntimeType.ofDialect(Byte.class, 9);

    case Types.TINYINT: // -6
      // TINYINY, UNSIGNED TINYINT
      return m.getColumnTypeName().startsWith("unsigned") ? //
          RuntimeType.ofDialect(Short.class, 11) : //
          RuntimeType.ofDialect(Byte.class, 10);

    case Types.SMALLINT: // 5
      return m.getColumnTypeName().startsWith("unsigned") ? //
          RuntimeType.ofDialect(Integer.class, 13) : //
          RuntimeType.ofDialect(Short.class, 12);

    case Types.INTEGER: // 4
      return m.getColumnTypeName().startsWith("unsigned") ? //
          RuntimeType.ofDialect(Long.class, 15) : //
          RuntimeType.ofDialect(Integer.class, 14);

    case Types.BIGINT: // -5
      return m.getColumnTypeName().startsWith("unsigned") ? //
          RuntimeType.ofDialect(BigInteger.class, 17) : //
          RuntimeType.ofDialect(Long.class, 16);

    // Floating point types

    case Types.REAL: // 7
      // REAL
      return RuntimeType.ofDialect(Float.class, 18);
    case Types.DOUBLE: // 8
      // FLOAT, DOUBLE PRECISION
      return RuntimeType.ofDialect(Double.class, 19);

    // Character types

    case Types.CHAR: // 1
      return RuntimeType.ofDialect(String.class, 20);
    case Types.VARCHAR: // 12
      // VARCHAR(n), UNIVARCHAR(n), NVARCHAR(n), SYSNAME, LONGSYSNAME
      return RuntimeType.ofDialect(String.class, 21);
    case Types.LONGVARCHAR: // -1
      // TEXT, UNITEXT
      return RuntimeType.ofDialect(String.class, 22);

    // Date/Time types

    case Types.DATE:
      return RuntimeType.ofDialect(java.time.LocalDate.class, 23);
    case Types.TIME:
      return RuntimeType.ofDialect(java.time.LocalTime.class, 24);
    case 10: // BIGTIME
      // Invalid JDBC type (10) reported by the SAP ASE JDBC Driver.
      return RuntimeType.ofDialect(java.time.LocalTime.class, 25);
    case Types.TIMESTAMP: // 93
      // DATETIME, SMALLDATETIME
      return RuntimeType.ofDialect(java.time.LocalDateTime.class, 26);
    case 11:
      // BIGDATETIME
      // Invalid JDBC type (11) reported by the SAP ASE JDBC Driver.
      return RuntimeType.ofDialect(java.time.LocalDateTime.class, 27);

    // LOB types

    case Types.BINARY: // BINARY
      return RuntimeType.ofDialect(byte[].class, 28);

    case Types.VARBINARY: // VARBINARY
      return RuntimeType.ofDialect(byte[].class, 29);

    case Types.LONGVARBINARY: // IMAGE
      return RuntimeType.ofDialect(byte[].class, 30);

    // If not found.

    default:
      return null;

    }

  }

  // WITH rendering

  @Override
  public WithRenderer getWithRenderer() {
    throw new UnsupportedLiveSQLFeatureException(
        "LiveSQL does not support Common Table Expressions (CTEs) SAP ASE/Sybase.");
  }

  // DISTINCT ON rendering

  @Override
  public DistinctOnRenderer getDistinctOnRenderer() {
    throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not support the DISTINCT ON clause.");
  }

  // From rendering

  @Override
  public FromRenderer getFromRenderer() {
    return () -> "";
  }

  // Table Expression rendering

  @Override
  public TableExpressionRenderer getTableExpressionRenderer() {
    return (columns) -> " ("
        + Arrays.stream(columns).map(c -> this.canonicalToNatural(c)).collect(Collectors.joining(", ")) + ")";
  }

  // Join rendering

  @Override
  public JoinRenderer getJoinRenderer() {
    return new JoinRenderer() {

      @Override
      public String renderJoinKeywords(final Join join) throws UnsupportedLiveSQLFeatureException {
        if (join instanceof InnerJoin) {
          return "JOIN";
        } else if (join instanceof LeftOuterJoin) {
          return "LEFT OUTER JOIN";
        } else if (join instanceof RightOuterJoin) {
          return "RIGHT OUTER JOIN";
        } else if (join instanceof FullOuterJoin) {
          throw new UnsupportedLiveSQLFeatureException("Full outer joins are not supported in the Sybase ASE database");
        } else if (join instanceof CrossJoin) {
          throw new UnsupportedLiveSQLFeatureException("Cross joins are not supported in the Sybase ASE database");
        } else if (join instanceof NaturalInnerJoin) {
          throw new UnsupportedLiveSQLFeatureException("Natural joins are not supported in the Sybase ASE database");
        } else if (join instanceof NaturalLeftOuterJoin) {
          throw new UnsupportedLiveSQLFeatureException("Natural joins are not supported in the Sybase ASE database");
        } else if (join instanceof NaturalRightOuterJoin) {
          throw new UnsupportedLiveSQLFeatureException("Natural joins are not supported in the Sybase ASE database");
        } else if (join instanceof NaturalFullOuterJoin) {
          throw new UnsupportedLiveSQLFeatureException("Natural joins are not supported in the Sybase ASE database");
        } else if (join instanceof JoinLateral) {
          throw new UnsupportedLiveSQLFeatureException("Lateral joins are not supported in the Sybase ASE database");
        } else if (join instanceof LeftJoinLateral) {
          throw new UnsupportedLiveSQLFeatureException(
              "Lateral left joins are not supported in the Sybase ASE database");
        } else if (join instanceof UnionJoin) {
          throw new UnsupportedLiveSQLFeatureException("Union joins are not supported in Sybase ASE database");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in Sybase ASE database");
        }
      }

    };
  }

  // Pagination rendering

  public PaginationRenderer getPaginationRenderer() {
    return new PaginationRenderer() {

      @Override
      public PaginationType getPaginationType(final boolean orderedSelect, final Integer offset, final Integer limit) {
        if (offset != null) {
          throw new UnsupportedLiveSQLFeatureException("OFFSET is not supported Sybase ASE");
        }
        return PaginationType.TOP;
      }

      @Override
      public void renderTopPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (offset == null) {
          w.write(" top " + limit);
        } else {
          throw new UnsupportedLiveSQLFeatureException("OFFSET is not supported Sybase ASE");
        }
      }

      @Override
      public void renderBottomPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination cannot be rendered at the bottom in Sybase ASE");
      }

      @Override
      public void renderBeginEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException(
            "Pagination cannot be rendered in enclosing fashion in Sybase ASE");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException(
            "Pagination cannot be rendered in enclosing fashion in Sybase ASE");
      }

    };
  }

  // For Update rendering

  @Override
  public LockingRenderer getLockingRenderer() {
    throw new UnsupportedLiveSQLFeatureException(
        "Sybase ASE does not support locking rows for plain SELECTs outside cursors and procedures");
  }

  // Set operation rendering

  @Override
  public SetOperatorRenderer getSetOperationRenderer() {
    return new SetOperatorRenderer() {

      @Override
      public void renderUnion(final QueryWriter w) {
        w.write("UNION");
      }

      @Override
      public void renderUnionAll(final QueryWriter w) {
        w.write("UNION ALL");
      }

      @Override
      public void renderExcept(final QueryWriter w) {
        w.write("MINUS");
      }

      @Override
      public void renderExceptAll(final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Sybase/SAP ASE does not support the EXCEPT ALL set operator. "
            + "Nevertheless, this operator can be simulated using an anti join");
      }

      @Override
      public void renderIntersect(final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Sybase/SAP ASE does not support the INTERSECT set operator. "
            + "Nevertheless, this operator can be simulated using a semi join");
      }

      @Override
      public void renderIntersectAll(final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Sybase/SAP ASE does not support the INTERSECT ALL set operator. "
            + "Nevertheless, this operator can be simulated using a semi join");
      }

    };
  }

  // Function rendering

  @Override
  public FunctionRenderer getFunctionRenderer() {
    return new FunctionRenderer() {

      // General purpose functions

      @Override
      public void groupConcat(final QueryWriter w, final boolean distinct, final CharExpression value,
          final List<OrderingTerm> ordering, final CharExpression separator) {
        throw new UnsupportedLiveSQLFeatureException("GROUP_CONCAT() is not supported in Sybase ASE");
      }

      // Arithmetic functions

      @Override
      public void logarithm(final QueryWriter w, final NumericExpression x, final NumericExpression base) {
        if (base == null) {
          this.write(w, "log", x);
        } else {
          w.write("(");
          this.write(w, "log", x);
          w.write(" / ");
          this.write(w, "log", base);
          w.write(")");
        }
      }

      @Override
      public void round(final QueryWriter w, final NumericExpression x, final NumericExpression places) {
        if (places == null) {
          throw new UnsupportedLiveSQLFeatureException(
              "Sybase ASE requires the number of decimal places to be specified on the ROUND() function");
        }
        this.write(w, "round", x, places);
      }

      @Override
      public void trunc(final QueryWriter w, final NumericExpression x, final NumericExpression places) {
        throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not support the TRUNC()function");
      }

      // String functions

      @Override
      public void concat(final QueryWriter w, final List<CharExpression> strings) {
        w.write("(");
        Separator sep = new Separator(" || ");
        for (CharExpression s : strings) {
          w.write(sep.render());
          Shield.renderTo(s, w);
        }
        w.write(")");
      }

      @Override
      public void length(final QueryWriter w, final CharExpression string) {
        this.write(w, "char_length", string);
      }

      @Override
      public void locate(final QueryWriter w, final CharExpression substring, final CharExpression string,
          final NumericExpression from) {
        if (from == null) {
          this.write(w, "charindex", substring, string);
        } else {
          this.write(w, "charindex", substring, string, from);
        }
      }

      @Override
      public void substr(final QueryWriter w, final CharExpression string, final NumericExpression from,
          final NumericExpression length) {
        if (length == null) {
          throw new UnsupportedLiveSQLFeatureException(
              "Sybase ASE requires the length parameter to be be specified on the SUBSTR() function");
        }
        this.write(w, "substring", string, from, length);
      }

      // Date/Time functions

      @Override
      public void currentDate(final QueryWriter w) {
        w.write("current_date()");
      }

      @Override
      public void currentTime(final QueryWriter w) {
        w.write("current_time");
      }

      @Override
      public void currentDateTime(final QueryWriter w) {
        w.write("getdate()");
      }

      @Override
      public void date(final QueryWriter w, final DateTimeExpression datetime) {
        throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not suppor the DATE() function");
      }

      @Override
      public void time(final QueryWriter w, final DateTimeExpression datetime) {
        throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not suppor the TIME() function");
      }

      @Override
      public void dateTime(final QueryWriter w, final DateTimeExpression date, final DateTimeExpression time) {
        throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not suppor the DATETIME() function");
      }

      @Override
      public void extract(final QueryWriter w, final DateTimeExpression datetime, final DateTimeFieldExpression field) {
        w.write("datepart(");
        Shield.renderTo(field, w);
        w.write(", ");
        Shield.renderTo(datetime, w);
        w.write(")");
      }

    };
  }

  // New SQL Identifier rendering

  private final String UNQUOTED_NATURAL = "[A-Za-z][A-Za-z0-9_]*";
  private final String UNQUOTED_CANONICAL = "[A-Za-z][A-Za-z0-9_]*";

  @Override
  public String naturalToCanonical(final String natural) {
    if (natural == null) {
      return null;
    }
    if (natural.matches(UNQUOTED_NATURAL)) {
      return natural;
    }
    return natural;
  }

  @Override
  public String canonicalToNatural(final String canonical) {
    if (canonical == null)
      return null;
    if (canonical.matches(UNQUOTED_CANONICAL)) {
      return canonical.toLowerCase();
    } else {
      return this.quoteIdentifier(canonical);
    }
  }

  @Override
  public String quoteIdentifier(final String verbatim) {
    return "\"" + verbatim.replace("\"", "\"\"").replace("'", "''").replace("]", "]]") + "\"";
  }

  @Override
  public DateTimeLiteralRenderer getDateTimeLiteralRenderer() {
    return new DateTimeLiteralRenderer() {

      @Override
      public String renderDate(final String isoDate) {
        return "cast('" + isoDate + "' as DATE)";
      }

      @Override
      public String renderTime(final String isoTime, final int precision) {
        if (precision > 3) {
          throw new InvalidLiteralException(
              "Sybase ASE's TIME literals accept a maximum precision of 3, but " + precision + " was specified");
        }
        return "cast('" + isoTime + "' as TIME)";
      }

      @Override
      public String renderTimestamp(final String isoTimestamp, final int precision) {
        if (precision > 6) {
          throw new InvalidLiteralException(
              "Sybase ASE's TIME literals accept a maximum precision of 6, but " + precision + " was specified");
        }
        return "cast('" + isoTimestamp + "' as BIGDATETIME)";
      }

      @Override
      public String renderOffsetTime(final String isoTime, final String isoOffset, final int precision) {
        throw new InvalidLiteralException("Sybase ASE does not implement the TIME WITH TIME ZONE data type.");
      }

      @Override
      public String renderOffsetTimestamp(final String isoTimestamp, final String isoOffset, final int precision) {
        throw new InvalidLiteralException("Sybase ASE does not implement the TIMESTAMP WITH TIME ZONE data type.");
      }

    };
  }

  @Override
  public BooleanLiteralRenderer getBooleanLiteralRenderer() {
    return new BooleanLiteralRenderer() {

      @Override
      public void renderTrue(final QueryWriter w) {
        w.write("1 = 1");
      }

      @Override
      public void renderFalse(final QueryWriter w) {
        w.write("1 = 0");
      }

    };
  }

  @Override
  public boolean mandatoryColumnNamesInRecursiveCTEs() {
    return false;
  }

  // Update rendering

  @Override
  public UpdateRenderer getUpdateRenderer() {
    return new UpdateRenderer() {

      @Override
      public boolean removeMainTableAlias() {
        return true;
      }

    };
  }

  @Override
  public CastRenderer getCastRenderer() {
    return new CastRenderer() {

      @Override
      public void render(QueryWriter w, Expression expr, String type) {
        w.write("CAST(");
        Shield.renderTo(expr, w);
        w.write(" AS " + type + ")");
      }

    };
  }

}
