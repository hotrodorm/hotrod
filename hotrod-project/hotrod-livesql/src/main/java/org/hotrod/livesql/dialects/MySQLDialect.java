package org.hotrod.livesql.dialects;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.hotrod.livesql.exceptions.InvalidLiteralException;
import org.hotrod.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.metadata.TableOrView;
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
import org.hotrod.livesql.queries.select.SShield;
import org.hotrod.livesql.queries.select.UnarySelectObject.LockingConcurrency;
import org.hotrod.livesql.queries.select.UnarySelectObject.LockingMode;
import org.hotrod.livesql.queries.select.UnionJoin;
import org.hotrod.livesql.queries.typesolver.ResultSetColumnMetadata;

public class MySQLDialect extends LiveSQLDialect {

  public MySQLDialect(final boolean discovered, final String productName, final String productVersion,
      final int majorVersion, final int minorVersion) {
    super(discovered, productName, productVersion, majorVersion, minorVersion);
  }

  @Override
  public void enableSelectStreaming(PreparedStatement ps, Integer fetchSize) throws SQLException {
    // Ignore fetch size to enable streaming
    ps.setFetchSize(Integer.MIN_VALUE);
  }

  @Override
  public RuntimeType resolveRuntimeType(final ResultSetColumnMetadata m) {

    switch (m.getColumnType()) {

    // Numeric types

    case java.sql.Types.DECIMAL:
      if ((m.getScale() != 0)) {
        return RuntimeType.ofDialect(BigDecimal.class, 1);
      } else {
        if (m.getPrecision() <= 2) {
          return RuntimeType.ofDialect(Byte.class, 2);
        } else if (m.getPrecision() <= 4) {
          return RuntimeType.ofDialect(Short.class, 3);
        } else if (m.getPrecision() <= 9) {
          return RuntimeType.ofDialect(Integer.class, 4);
        } else if (m.getPrecision() <= 18) {
          return RuntimeType.ofDialect(Long.class, 5);
        } else {
          return RuntimeType.ofDialect(BigInteger.class, 6);
        }
      }

    case java.sql.Types.TINYINT:
      if (m.getColumnTypeName().toUpperCase().contains("UNSIGNED")) {
        return RuntimeType.ofDialect(Short.class, 8);
      } else {
        return RuntimeType.ofDialect(Byte.class, 7);
      }

    case java.sql.Types.SMALLINT:
      if (m.getColumnTypeName().toUpperCase().contains("UNSIGNED")) {
        return RuntimeType.ofDialect(Integer.class, 10);
      } else {
        return RuntimeType.ofDialect(Short.class, 9);
      }

    case java.sql.Types.INTEGER:
      if (m.getColumnTypeName().toUpperCase().equals("MEDIUMINT")) {
        return RuntimeType.ofDialect(Integer.class, 11);
      } else if (m.getColumnTypeName().toUpperCase().equals("MEDIUMINT UNSIGNED")) {
        return RuntimeType.ofDialect(Integer.class, 12);
      } else if (m.getColumnTypeName().toUpperCase().equals("INT")) {
        return RuntimeType.ofDialect(Integer.class, 13);
      } else if (m.getColumnTypeName().toUpperCase().equals("INT UNSIGNED")) {
        return RuntimeType.ofDialect(Long.class, 14);
      }

    case java.sql.Types.BIGINT:
      if (m.getColumnTypeName().toUpperCase().equals("BIGINT")) {
        return RuntimeType.ofDialect(Long.class, 15);
      } else if (m.getColumnTypeName().toUpperCase().equals("BIGINT UNSIGNED")) {
        return RuntimeType.ofDialect(BigInteger.class, 16);
      }

    case java.sql.Types.REAL:
    case java.sql.Types.FLOAT:
      return RuntimeType.ofDialect(Float.class, 17);

    case java.sql.Types.DOUBLE:
      return RuntimeType.ofDialect(Double.class, 18);

    case java.sql.Types.CHAR: // 1
      // CHAR, ENUM, SET
      return RuntimeType.ofDialect(String.class, 19);
    case java.sql.Types.VARCHAR: // 12
      // VARCHAR, TINYTEXT
      if ("TINYTEXT".equalsIgnoreCase(m.getColumnTypeName())) {
        return RuntimeType.ofDialect(String.class, 21);
      } else {
        return RuntimeType.ofDialect(String.class, 20);
      }
    case java.sql.Types.LONGVARCHAR: // -1
      // TEXT, MEDIUMTEXT, LONGTEXT
      return RuntimeType.ofDialect(String.class, 22);

    case java.sql.Types.DATE: // 91
      if ("YEAR".equalsIgnoreCase(m.getColumnTypeName())) {
        return RuntimeType.ofDialect(Integer.class, 27);
      } else {
        return RuntimeType.ofDialect(java.time.LocalDate.class, 23);
      }
    case java.sql.Types.TIME: // 92
      return RuntimeType.ofDialect(java.time.LocalTime.class, 24);
    case java.sql.Types.TIMESTAMP: // 93
      if ("TIMESTAMP".equalsIgnoreCase(m.getColumnTypeName())) {
        return RuntimeType.ofDialect(java.time.OffsetDateTime.class, 26);
      } else {
        return RuntimeType.ofDialect(java.time.LocalDateTime.class, 25);
      }

    case java.sql.Types.VARBINARY: // -3
      // TINYBLOB
      return RuntimeType.ofDialect(byte[].class, 28);
    case java.sql.Types.LONGVARBINARY: // -4
      // BLOB, MEDIUMBLOB, LONGBLOB
      return RuntimeType.ofDialect(byte[].class, 29);

    default: // Unrecognized type
      return null;

    }

  }

  // WITH rendering

  @Override
  public WithRenderer getWithRenderer() {
    if (versionIsAtLeast(8, 0, 1)) {
      return (c) -> "WITH" + (c ? " RECURSIVE" : "");
    }
    throw new UnsupportedLiveSQLFeatureException(
        "LiveSQL supports Common Table Expressions (CTEs) starting in MySQL 8.0.1 or newer, "
            + "but the current version is " + renderVersion());
  }

  // DISTINCT ON rendering

  @Override
  public DistinctOnRenderer getDistinctOnRenderer() {
    throw new UnsupportedLiveSQLFeatureException("The MySQL database does not support the DISTINCT ON clause.");
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
          throw new UnsupportedLiveSQLFeatureException("Full outer joins are not supported in MySQL");
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
          if (versionIsAtLeast(8, 0, 14)) {
            return "JOIN LATERAL";
          }
          throw new UnsupportedLiveSQLFeatureException(
              "LiveSQL supports lateral joins in the MySQL database starting in version 8.0.14, "
                  + "but the current version is " + renderVersion());
        } else if (join instanceof LeftJoinLateral) {
          if (versionIsAtLeast(8, 0, 14)) {
            return "LEFT JOIN LATERAL";
          }
          throw new UnsupportedLiveSQLFeatureException(
              "LiveSQL supports lateral joins in the PostgreSQL database starting in version 8.0.14, "
                  + "but the current version is " + renderVersion());
        } else if (join instanceof UnionJoin) {
          throw new UnsupportedLiveSQLFeatureException("Union joins are not supported in MariaDB database");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in MariaDB database");
        }
      }

      @Override
      public String renderOptionalOnPredicate(final Join join) throws UnsupportedLiveSQLFeatureException {
        if (join instanceof InnerJoin) {
          return "";
        } else if (join instanceof LeftOuterJoin) {
          return "";
        } else if (join instanceof RightOuterJoin) {
          return "";
        } else if (join instanceof FullOuterJoin) {
          return "";
        } else if (join instanceof CrossJoin) {
          return "";
        } else if (join instanceof NaturalInnerJoin) {
          return "";
        } else if (join instanceof NaturalLeftOuterJoin) {
          return "";
        } else if (join instanceof NaturalRightOuterJoin) {
          return "";
        } else if (join instanceof NaturalFullOuterJoin) {
          return "";
        } else if (join instanceof JoinLateral) {
          return " ON true";
        } else if (join instanceof LeftJoinLateral) {
          return " ON true";
        } else if (join instanceof UnionJoin) {
          throw new UnsupportedLiveSQLFeatureException("Union joins are not supported in Oracle database");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in Oracle database");
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
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in MySQL");
      }

      @Override
      public void renderBottomPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (limit != null) {
          if (offset != null) {
            w.write("\nLIMIT " + limit + "\nOFFSET " + offset);
          } else {
            w.write("\nLIMIT " + limit);
          }
        } else {
          w.write("\nOFFSET " + offset);
        }
      }

      @Override
      public void renderBeginEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in MySQL");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in MySQL");
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
        StringBuilder sb = new StringBuilder();

        if (lockingMode == LockingMode.FOR_UPDATE) {
          sb.append("FOR UPDATE");
        } else {
          sb.append("FOR SHARE");
        }

        if (lockingConcurrency != null) {
          switch (lockingConcurrency) {
          case NO_WAIT:
            sb.append(" NOWAIT");
            break;
          case WAIT:
            throw new UnsupportedLiveSQLFeatureException(
                "The MySQL database does not support locking with WAIT <n> in SELECT statements");
          case SKIP_LOCKED:
            sb.append(" SKIP LOCKED");
            break;
          }
        }

        return sb.toString();
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
        if (w.getSQLDialect().versionIsAtLeast(8, 0, 31)) {
          w.write("EXCEPT");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "MySQL does not support the EXCEPT set operator before version 8.0.31; " + "this version is "
                  + w.getSQLDialect().renderVersion()
                  + ". Nevertheless, this operator can be simulated using an anti join");
        }
      }

      @Override
      public void renderExceptAll(final QueryWriter w) {
        if (w.getSQLDialect().versionIsAtLeast(8, 0, 31)) {
          w.write("EXCEPT ALL");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "MySQL does not support the EXCEPT ALL set operator before version 8.0.31; " + "this version is "
                  + w.getSQLDialect().renderVersion()
                  + ". Nevertheless, this operator can be simulated using an anti join");
        }
      }

      @Override
      public void renderIntersect(final QueryWriter w) {
        if (w.getSQLDialect().versionIsAtLeast(8, 0, 31)) {
          w.write("INTERSECT");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "MySQL does not support the INTERSECT set operator before version 8.0.31; " + "this version is "
                  + w.getSQLDialect().renderVersion()
                  + ". Nevertheless, this operator can be simulated using a semi join");
        }
      }

      @Override
      public void renderIntersectAll(final QueryWriter w) {
        if (w.getSQLDialect().versionIsAtLeast(8, 0, 31)) {
          w.write("INTERSECT ALL");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "MySQL does not support the INTERSECT ALL set operator before version 8.0.31; " + "this version is "
                  + w.getSQLDialect().renderVersion()
                  + ". Nevertheless, this operator can be simulated using a semi join");
        }
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
      public void trunc(final QueryWriter w, final NumericExpression x, final NumericExpression places) {
        if (places == null) {
          this.write(w, "truncate", x);
        } else {
          this.write(w, "truncate", x, places);
        }
      }

      // String functions

      // Date/Time functions

      @Override
      public void currentDate(final QueryWriter w) {
        w.write("curdate()");
      }

      @Override
      public void currentTime(final QueryWriter w) {
        w.write("curtime()");
      }

    };
  }

  // New SQL Identifier rendering

  // MySQL may be case sensitive or not depending on the OS where it's running.
  // Assume it's case sensitive and quote always.

  @Override
  public String naturalToCanonical(final String natural) {
    return natural;
  }

  @Override
  public String canonicalToNatural(final String canonical) {
    if (canonical == null)
      return null;
    return this.quoteIdentifier(canonical);
  }

  @Override
  public String quoteIdentifier(final String verbatim) {
    return "`" + verbatim.replace("`", "``") + "`";
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
        if (precision > 6) {
          throw new InvalidLiteralException(
              "MySQL's TIME literals accept a maximum precision of 6, but " + precision + " was specified.");
        }
        return "TIME '" + isoTime + "'";
      }

      @Override
      public String renderTimestamp(final String isoTimestamp, final int precision) {
        if (precision > 6) {
          throw new InvalidLiteralException(
              "MySQL's TIMESTAMP literals accept a maximum precision of 6, but " + precision + " was specified.");
        }
        return "TIMESTAMP '" + isoTimestamp + "'";
      }

      @Override
      public String renderOffsetTime(final String isoTime, final String isoOffset, final int precision) {
        throw new InvalidLiteralException("MySQL does not implement the TIME WITH TIME ZONE data type.");
      }

      @Override
      public String renderOffsetTimestamp(final String isoTimestamp, final String isoOffset, final int precision) {
        throw new InvalidLiteralException("MySQL does not implement the TIMESTAMP WITH TIME ZONE data type.");
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
    return false;
  }

  public String canonicalToNatural(final TableOrView tov) {
    if (tov == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    if (tov.getCatalog() != null) {
      sb.append(this.canonicalToNatural(tov.getCatalog()));
      sb.append(".");
    }
    sb.append(this.canonicalToNatural(SShield.getName(tov)));
    return sb.toString();
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
