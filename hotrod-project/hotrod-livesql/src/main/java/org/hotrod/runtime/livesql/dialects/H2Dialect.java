package org.hotrod.runtime.livesql.dialects;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.LockingConcurrency;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject.LockingMode;
import org.hotrod.runtime.livesql.queries.select.CrossJoin;
import org.hotrod.runtime.livesql.queries.select.FullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.InnerJoin;
import org.hotrod.runtime.livesql.queries.select.Join;
import org.hotrod.runtime.livesql.queries.select.JoinLateral;
import org.hotrod.runtime.livesql.queries.select.LeftJoinLateral;
import org.hotrod.runtime.livesql.queries.select.LeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalFullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalInnerJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalLeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalRightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.RightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.UnionJoin;
import org.hotrod.runtime.livesql.queries.typesolver.ResultSetColumnMetadata;

public class H2Dialect extends LiveSQLDialect {

  public H2Dialect(final boolean discovered, final String productName, final String productVersion,
      final int majorVersion, final int minorVersion) {
    super(discovered, productName, productVersion, majorVersion, minorVersion);
  }

  @Override
  public Class<?> resolveColumnType(final ResultSetColumnMetadata m) {

    switch (m.getColumnType()) {

    // Numeric types

    case java.sql.Types.DECIMAL:
    case java.sql.Types.NUMERIC:
      if (m.getScale() != 0) {
        return BigDecimal.class;
      } else {
        if (m.getPrecision() <= 2) {
          return Byte.class;
        } else if (m.getPrecision() <= 4) {
          return Short.class;
        } else if (m.getPrecision() <= 9) {
          return Integer.class;
        } else if (m.getPrecision() <= 18) {
          return Long.class;
        } else {
          return BigInteger.class;
        }
      }

    case java.sql.Types.TINYINT:
      return Byte.class;

    case java.sql.Types.SMALLINT:
      return Short.class;

    case java.sql.Types.INTEGER:
      return Integer.class;

    case java.sql.Types.BIGINT:
      return Long.class;

    case java.sql.Types.DOUBLE:
      return Double.class;

    case java.sql.Types.REAL:
      return Float.class;

    // Character types

    case java.sql.Types.CHAR:
      return String.class;

    case java.sql.Types.VARCHAR:
      return String.class;

    case java.sql.Types.CLOB:
      return String.class;

    // Date/Time types

    case java.sql.Types.DATE:
      return java.sql.Date.class;
    case java.sql.Types.TIME: // differs from original: java.sql.Time
      return LocalTime.class;
    case java.sql.Types.TIME_WITH_TIMEZONE: // new
      return OffsetTime.class;
    case java.sql.Types.TIMESTAMP: // differs from original: java.sql.Timestamp
      return LocalDateTime.class;
    case java.sql.Types.TIMESTAMP_WITH_TIMEZONE: // new
      return ZonedDateTime.class;

    // Binary

    case java.sql.Types.VARBINARY:
      return byte[].class;
    case java.sql.Types.BLOB:
      return byte[].class;

    // Boolean

    case java.sql.Types.BOOLEAN:
      return Boolean.class;

    // Other

    case java.sql.Types.BINARY: // UUID
      return byte[].class;

    case java.sql.Types.ARRAY: // ARRAY
      return Object[].class;

    case java.sql.Types.OTHER:
      if ("timestamp with timezone".equalsIgnoreCase(m.getColumnTypeName())) {
        return java.sql.Timestamp.class;

        // If the JDBC driver was 1.4.x (unstable as of Dec 2016) we could use:
        // return new PropertyType("org.h2.api.TimestampWithTimeZone", m,
        // false);

      } else {
        return byte[].class;
      }

    default: // Unrecognized type
      return Object.class;

    }

  }

  // WITH rendering

  @Override
  public WithRenderer getWithRenderer() {
    return (c) -> "WITH";
  }

  // DISTINCT ON rendering

  @Override
  public DistinctOnRenderer getDistinctOnRenderer() {
    throw new UnsupportedLiveSQLFeatureException("The H2 database does not support the DISTINCT ON clause.");
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
          return "LEFT JOIN";
        } else if (join instanceof RightOuterJoin) {
          return "RIGHT JOIN";
        } else if (join instanceof FullOuterJoin) {
          return "FULL JOIN";
        } else if (join instanceof CrossJoin) {
          return "CROSS JOIN";
        } else if (join instanceof NaturalInnerJoin) {
          return "NATURAL JOIN";
        } else if (join instanceof NaturalLeftOuterJoin) {
          return "NATURAL LEFT JOIN";
        } else if (join instanceof NaturalRightOuterJoin) {
          return "NATURAL RIGHT JOIN";
        } else if (join instanceof NaturalFullOuterJoin) {
          return "NATURAL FULL JOIN";
        } else if (join instanceof JoinLateral) {
          throw new UnsupportedLiveSQLFeatureException("Lateral joins are not supported in the H2 database");
        } else if (join instanceof LeftJoinLateral) {
          throw new UnsupportedLiveSQLFeatureException("Lateral left joins are not supported in the H2 database");
        } else if (join instanceof UnionJoin) {
          throw new UnsupportedLiveSQLFeatureException("Union joins are not supported in the H2 database");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in the H2 database");
        }
      }

    };
  }

  // Pagination rendering

  public PaginationRenderer getPaginationRenderer() {
    return new PaginationRenderer() {

      @Override
      public PaginationType getPaginationType(final boolean orderedSelect, final Integer offset, final Integer limit) {
        return offset != null || limit != null ? PaginationType.BOTTOM : null;
      }

      @Override
      public void renderTopPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in H2");
      }

      @Override
      public void renderBottomPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (limit != null) {
          if (offset != null) {
            w.write("\nLIMIT " + limit + " OFFSET " + offset);
          } else {
            w.write("\nLIMIT " + limit);
          }
        } else {
          w.write("\nOFFSET " + offset);
        }
      }

      @Override
      public void renderBeginEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in H2");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in H2");
      }

    };
  }

  // For Update rendering

  @Override
  public LockingRenderer getLockingRenderer() {
    return new LockingRenderer() {

      @Override
      public String renderLockingAfterFromClause(LockingMode lockingMode, LockingConcurrency lockingConcurrency,
          Number waitTime) {
        return null;
      }

      @Override
      public String renderLockingAfterLimitClause(LockingMode lockingMode, LockingConcurrency lockingConcurrency,
          Number waitTime) {
        if (lockingMode == LockingMode.FOR_SHARE) {
          throw new UnsupportedLiveSQLFeatureException(
              "The H2 database does not support locking FOR SHARE in SELECT statements");
        }
        switch (lockingConcurrency) {
        case NO_WAIT:
          if (!versionIsAtLeast(2, 2)) {
            throw new UnsupportedLiveSQLFeatureException(
                "The H2 database does not support locking with NOWAIT in the current version. This feature is supported in H2 starting in version 2.2.x");
          }
          return "FOR UPDATE NOWAIT";
        case WAIT:
          if (!versionIsAtLeast(2, 2)) {
            throw new UnsupportedLiveSQLFeatureException(
                "The H2 database does not support locking with WAIT <n> in the current version. This feature is supported in H2 starting in version 2.2.x");
          }
          return "FOR UPDATE WAIT " + waitTime;
        case SKIP_LOCKED:
          if (!versionIsAtLeast(2, 2)) {
            throw new UnsupportedLiveSQLFeatureException(
                "The H2 database does not support locking with SKIP LOCKED in the current version. This feature is supported in H2 starting in version 2.2.x");
          }
          return "FOR UPDATE SKIP LOCKED";
        default:
          return "FOR UPDATE";
        }
      }

    };
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
        w.write("EXCEPT");
      }

      @Override
      public void renderExceptAll(final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("The H2 database does not support the EXCEPT ALL set operator. "
            + "Nevertheless, this operator can be simulated using an anti join");
      }

      @Override
      public void renderIntersect(final QueryWriter w) {
        w.write("INTERSECT");
      }

      @Override
      public void renderIntersectAll(final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("The H2 database does not support the INTERSECT ALL set operator. "
            + "Nevertheless, this operator can be simulated using a semi join");
      }

    };
  }

  // Function rendering

  @Override
  public FunctionRenderer getFunctionRenderer() {
    return new FunctionRenderer() {

      // General purpose functions

      // Arithmetic functions

      @Override
      public void logarithm(final QueryWriter w, final NumberExpression x, final NumberExpression base) {
        if (base == null) {
          this.write(w, "ln", x);
        } else {
          w.write("(");
          this.write(w, "ln", x);
          w.write(" / ");
          this.write(w, "ln", base);
          w.write(")");
        }
      }

      public void trunc(final QueryWriter w, final NumberExpression x, final NumberExpression places) {
        if (places == null) {
          throw new UnsupportedLiveSQLFeatureException(
              "H2 requires the number of decimal places to be specified when using the TRUNC() function");
        }
        this.write(w, "trunc", x, places);
      }

      // String functions

      // Date/Time functions

      @Override
      public void date(final QueryWriter w, final DateTimeExpression datetime) {
        w.write("cast(");
        Helper.renderTo(datetime, w);
        w.write(" as date)");
      }

      @Override
      public void time(final QueryWriter w, final DateTimeExpression datetime) {
        w.write("cast(");
        Helper.renderTo(datetime, w);
        w.write(" as time)");
      }

      @Override
      public void dateTime(final QueryWriter w, final DateTimeExpression date, final DateTimeExpression time) {
        w.write("(");
        Helper.renderTo(date, w);
        w.write(" + ");
        Helper.renderTo(time, w);
        w.write(")");
      }

    };
  }

  // New SQL Identifier rendering

  private final String UNQUOTED_NATURAL = "[A-Za-z][A-Za-z0-9_]*";
  private final String UNQUOTED_CANONICAL = "[A-Z][A-Z0-9_]*";

  @Override
  public String naturalToCanonical(final String natural) {
    if (natural == null) {
      return null;
    }
    if (natural.matches(UNQUOTED_NATURAL)) {
      return natural.toUpperCase();
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
    return "\"" + verbatim.replace("\"", "\"\"") + "\"";
  }

  @Override
  public DateTimeLiteralRenderer getDateTimeLiteralRenderer() {
    return new DateTimeLiteralRenderer() {

      @Override
      public String renderDate(final String isoDate) {
        return "DATE '" + isoDate + "'";
      }

      @Override
      public String renderTime(final String isoTime, final int precision) {
        return "TIME '" + isoTime + "'";
      }

      @Override
      public String renderTimestamp(final String isoTimestamp, final int precision) {
        return "TIMESTAMP '" + isoTimestamp + "'";
      }

      @Override
      public String renderOffsetTime(final String isoTime, final String isoOffset, final int precision) {
        return "TIME WITH TIME ZONE '" + isoTime + isoOffset + "'";
      }

      @Override
      public String renderOffsetTimestamp(final String isoTimestamp, final String isoOffset, final int precision) {
        return "TIMESTAMP WITH TIME ZONE '" + isoTimestamp + isoOffset + "'";
      }

    };

  }

  @Override
  public BooleanLiteralRenderer getBooleanLiteralRenderer() {
    return new BooleanLiteralRenderer() {

      @Override
      public void renderTrue(final QueryWriter w) {
        w.write("true");
      }

      @Override
      public void renderFalse(final QueryWriter w) {
        w.write("false");
      }

    };
  }

  @Override
  public boolean mandatoryColumnNamesInRecursiveCTEs() {
    return true;
  }

  // Update rendering

  @Override
  public UpdateRenderer getUpdateRenderer() {
    return new UpdateRenderer() {

      @Override
      public boolean removeMainTableAlias() {
        return false;
      }

    };
  }

}
