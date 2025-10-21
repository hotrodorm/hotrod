package org.hotrod.livesql.dialects;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.hotrod.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
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

public class HyperSQLDialect extends LiveSQLDialect {

  public HyperSQLDialect(final boolean discovered, final String productName, final String productVersion,
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

    // Numeric types

    case java.sql.Types.DECIMAL:
    case java.sql.Types.NUMERIC:
      if (m.getScale() != 0) {
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
      return RuntimeType.ofDialect(Byte.class, 7);
    case java.sql.Types.SMALLINT:
      return RuntimeType.ofDialect(Short.class, 8);
    case java.sql.Types.INTEGER:
      return RuntimeType.ofDialect(Integer.class, 9);
    case java.sql.Types.BIGINT:
      return RuntimeType.ofDialect(Long.class, 10);
    case java.sql.Types.FLOAT: // float is never reported
    case java.sql.Types.DOUBLE:
      return RuntimeType.ofDialect(Double.class, 11);

    // Character types

    case java.sql.Types.CHAR: // 1
      // CHAR
      return RuntimeType.ofDialect(String.class, 12);
    case java.sql.Types.VARCHAR: // 12
      // VARCHAR, LONGVARCHAR
      if (m.getColumnTypeName() != null && m.getColumnTypeName().toUpperCase().startsWith("INTERVAL")) {
        return null;
      } else {
        return RuntimeType.ofDialect(String.class, 13);
      }
    case java.sql.Types.CLOB: // 2005
      // CLOB
      return RuntimeType.ofDialect(String.class, 14);

    // Date/Time types

    case java.sql.Types.DATE:
      return RuntimeType.ofDialect(LocalDate.class, 15);
    case java.sql.Types.TIME:
      if ("TIME WITH TIME ZONE".equalsIgnoreCase(m.getColumnTypeName())) {
        return RuntimeType.ofDialect(OffsetTime.class, 17);
      } else {
        return RuntimeType.ofDialect(LocalTime.class, 16);
      }
    case java.sql.Types.TIMESTAMP:
      if ("TIMESTAMP WITH TIME ZONE".equalsIgnoreCase(m.getColumnTypeName())) {
        return RuntimeType.ofDialect(OffsetDateTime.class, 19);
      } else {
        return RuntimeType.ofDialect(LocalDateTime.class, 18);
      }

    case java.sql.Types.BOOLEAN:
      return RuntimeType.ofDialect(Boolean.class, 20);

    // Binary

    case java.sql.Types.BLOB: // 2004
    case java.sql.Types.BINARY: // -2
    case java.sql.Types.VARBINARY: // -3
      // BLOB, BINARY, VARBINARY
      return RuntimeType.ofDialect(byte[].class, 21);

    // Other

    case java.sql.Types.OTHER:
      return null;

    case java.sql.Types.BIT:
    case java.sql.Types.ARRAY:
      return null;

    default: // Unrecognized type
      return null;

    }

  }

  // WITH rendering

  @Override
  public WithRenderer getWithRenderer() {
    return (c) -> "WITH" + (c ? " RECURSIVE" : "");
  }

  // DISTINCT ON rendering

  @Override
  public DistinctOnRenderer getDistinctOnRenderer() {
    throw new UnsupportedLiveSQLFeatureException("The HyperSQL database does not support the DISTINCT ON clause.");
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
          return "FULL OUTER JOIN";
        } else if (join instanceof CrossJoin) {
          return "CROSS JOIN";
        } else if (join instanceof NaturalInnerJoin) {
          return "NATURAL JOIN";
        } else if (join instanceof NaturalLeftOuterJoin) {
          return "NATURAL LEFT OUTER JOIN";
        } else if (join instanceof NaturalRightOuterJoin) {
          return "NATURAL RIGHT OUTER JOIN";
        } else if (join instanceof NaturalFullOuterJoin) {
          return "NATURAL FULL OUTER JOIN";
        } else if (join instanceof JoinLateral) {
          throw new UnsupportedLiveSQLFeatureException("Lateral joins are not supported in the HyperSQL database");
        } else if (join instanceof LeftJoinLateral) {
          throw new UnsupportedLiveSQLFeatureException("Lateral left joins are not supported in the HyperSQL database");
        } else if (join instanceof UnionJoin) {
          return "UNION JOIN";
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in HyperSQL database");
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
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in HyperSQL");
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
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in HyperSQL");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in HyperSQL");
      }

    };
  }

  // For Update rendering

  @Override
  public LockingRenderer getLockingRenderer() {
    throw new UnsupportedLiveSQLFeatureException(
        "HyperSQL does not support locking rows in queries that do not use cursors.");
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
        w.write("EXCEPT ALL");
      }

      @Override
      public void renderIntersect(final QueryWriter w) {
        w.write("INTERSECT");
      }

      @Override
      public void renderIntersectAll(final QueryWriter w) {
        w.write("INTERSECT ALL");
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
      public void logarithm(final QueryWriter w, final NumericExpression x, final NumericExpression base) {
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

      @Override
      public void round(final QueryWriter w, final NumericExpression x, final NumericExpression places) {
        if (places == null) {
          throw new UnsupportedLiveSQLFeatureException(
              "HyperSQL requires the number of decimal places to be specified when using the ROUND() function");
        }
        this.write(w, "round", x, places);
      }

      // String functions

      @Override
      public void substr(final QueryWriter w, final CharExpression string, final NumericExpression from,
          final NumericExpression length) {
        if (length == null) {
          throw new UnsupportedLiveSQLFeatureException(
              "HyperSQL requires the length to be specified when using the SUBSTR() function");
        } else {
          this.write(w, "substr", string, from, length);
        }
      }

      // Date/Time functions

      @Override
      public void currentDate(final QueryWriter w) {
        w.write("curdate()");
      }

      @Override
      public void currentTime(final QueryWriter w) {
        w.write("curtime()");
      }

      @Override
      public void currentDateTime(final QueryWriter w) {
        w.write("current_timestamp");
      }

      @Override
      public void date(final QueryWriter w, final DateTimeExpression datetime) {
        w.write("cast(");
        Shield.renderTo(datetime, w);
        w.write(" as date)");
      }

      @Override
      public void time(final QueryWriter w, final DateTimeExpression datetime) {
        w.write("cast(");
        Shield.renderTo(datetime, w);
        w.write(" as time)");
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
        return "TIME '" + isoTime + isoOffset + "'";
      }

      @Override
      public String renderOffsetTimestamp(final String isoTimestamp, final String isoOffset, final int precision) {
        return "TIMESTAMP '" + isoTimestamp + isoOffset + "'";
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
