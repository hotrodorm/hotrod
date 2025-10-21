package org.hotrod.livesql.dialects;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

import org.hotrod.livesql.exceptions.InvalidLiteralException;
import org.hotrod.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.livesql.expressions.Expression;
import org.hotrod.livesql.expressions.Shield;
import org.hotrod.livesql.expressions.character.CharExpression;
import org.hotrod.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.livesql.expressions.numeric.NumericExpression;
import org.hotrod.livesql.ordering.OHelper;
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
import org.hotrod.livesql.queries.select.UnarySelectObject.LockingConcurrency;
import org.hotrod.livesql.queries.select.UnarySelectObject.LockingMode;
import org.hotrod.livesql.queries.select.UnionJoin;
import org.hotrod.livesql.queries.typesolver.ResultSetColumnMetadata;
import org.hotrod.utils.Separator;

public class OracleDialect extends LiveSQLDialect {

  public OracleDialect(final boolean discovered, final String productName, final String productVersion,
      final int majorVersion, final int minorVersion) {
    super(discovered, productName, productVersion, majorVersion, minorVersion);
  }

  @Override
  public void enableSelectStreaming(PreparedStatement ps, Integer fetchSize) throws SQLException {
    // Streaming enabled by default
    if (fetchSize != null) {
      ps.setFetchSize(fetchSize);
    }
  }

  @Override
  public RuntimeType resolveRuntimeType(final ResultSetColumnMetadata m) {

    switch (m.getColumnType()) {

    // Numeric types

    case java.sql.Types.DECIMAL: // 3
    case java.sql.Types.NUMERIC: // 2
      if (m.getScale() > 0) { // DECIMAL or NUMBER with decimal places
        return RuntimeType.ofDialect(BigDecimal.class, 1);
      } else if (m.getScale() < 0) { // FLOAT, REAL, DOUBLE PRECISION
        return RuntimeType.ofDialect(Double.class, 7);
      } else { // DECIMAL or NUMBER without decimal places
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

    case 100: // BINARY_FLOAT
      return RuntimeType.ofDialect(Float.class, 8);
    case 101: // BINARY_DOUBLE
      return RuntimeType.ofDialect(Double.class, 9);

    // Character types

    case java.sql.Types.CHAR: // 1
      // CHAR
      return RuntimeType.ofDialect(String.class, 10);
    case java.sql.Types.VARCHAR: // 12
      // VARCHAR, VARCHAR2
      return RuntimeType.ofDialect(String.class, 11);
    case java.sql.Types.NCHAR: // -15
      // NCHAR
      return RuntimeType.ofDialect(String.class, 12);
    case java.sql.Types.NVARCHAR: // -9
      // NVARCHAR2
      return RuntimeType.ofDialect(String.class, 13);
    case java.sql.Types.CLOB: // 2005
      // CLOB
      return RuntimeType.ofDialect(String.class, 14);
    case java.sql.Types.NCLOB: // 2011
      // NCLOB
      return RuntimeType.ofDialect(String.class, 15);
    case java.sql.Types.LONGVARCHAR: // -1
      // LONG
      return RuntimeType.ofDialect(String.class, 16);

    // Date/Time types

    case java.sql.Types.TIMESTAMP: // 93
      // DATE, TIMESTAMP
      return RuntimeType.ofDialect(java.time.LocalDateTime.class, 17);
    case -101:
      // TIMESTAMP WITH TIME ZONE
      return RuntimeType.ofDialect(OffsetDateTime.class, 18);
    case -102:
      // TIMESTAMP WITH LOCAL TIME ZONE
      return RuntimeType.ofDialect(OffsetDateTime.class, 19);
    case -103:
      // INTERVALYM
      return null;
    case -104:
      // INTERVALDS
      return null;

    // Binary

    case java.sql.Types.VARBINARY: // -3
      // RAW
      return RuntimeType.ofDialect(byte[].class, 20);
    case java.sql.Types.LONGVARBINARY: // -4
      // LONG RAW
      return RuntimeType.ofDialect(byte[].class, 21);
    case java.sql.Types.BLOB: // 2004
      // BLOB
      return RuntimeType.ofDialect(byte[].class, 22);
    case -13: // BFILE
      return RuntimeType.ofDialect(byte[].class, 23);

    // Other

    case java.sql.Types.ROWID: // -8
      // ROWID
      return RuntimeType.ofDialect(String.class, 24);

    case java.sql.Types.SQLXML: // 2009
      // XMLTYPE
      return null;

    case java.sql.Types.STRUCT: // 2002
      // URITYPE, OBJECT (struct)
      return null;

    case java.sql.Types.ARRAY: // 2003
      // VARRAY
      return null;
    case java.sql.Types.REF: // 2006
      // REF
      return null;

    default: // other
      return null;

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
    throw new UnsupportedLiveSQLFeatureException("The Oracle database does not support the DISTINCT ON clause.");
  }

  // From rendering

  @Override
  public FromRenderer getFromRenderer() {
    return () -> "FROM dual";
  }

  // Table Expression rendering

  @Override
  public TableExpressionRenderer getTableExpressionRenderer() {
    throw new UnsupportedLiveSQLFeatureException(
        "Oracle does not support naming the columns of a table expression right after the table expression name.");
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
          if (versionIsAtLeast(12, 1)) {
            return "JOIN LATERAL";
          }
          throw new UnsupportedLiveSQLFeatureException(
              "LiveSQL supports lateral joins in the Oracle database starting in version 12c1, "
                  + "but the current version is " + renderVersion());
        } else if (join instanceof LeftJoinLateral) {
          if (versionIsAtLeast(12, 1)) {
            return "LEFT JOIN LATERAL";
          }
          throw new UnsupportedLiveSQLFeatureException(
              "LiveSQL supports lateral joins in the Oracle database starting in version 12c1, "
                  + "but the current version is " + renderVersion());
        } else if (join instanceof UnionJoin) {
          throw new UnsupportedLiveSQLFeatureException("Union joins are not supported in Oracle database");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in Oracle database");
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
          return " ON 1 = 1";
        } else if (join instanceof LeftJoinLateral) {
          return " ON 1 = 1";
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

  // -- offset 5, limit 3
  // select * from (
  // select x.*, rownum as "_HotRodRowNum_" from (
  //
  // select * from account order by id -- untouched original query
  //
  // ) x where rownum <= 5 + 3
  // ) y where "_HotRodRowNum_" > 5

  public PaginationRenderer getPaginationRenderer() {
    return new PaginationRenderer() {

      @Override
      public PaginationType getPaginationType(final boolean orderedSelect, final Integer offset, final Integer limit) {
        return versionIsAtLeast(12, 1) ? PaginationType.BOTTOM : PaginationType.ENCLOSE;
      }

      @Override
      public void renderTopPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination cannot be rendered at the top");
      }

      @Override
      public void renderBottomPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (!versionIsAtLeast(12, 1)) {
          throw new UnsupportedLiveSQLFeatureException(
              "Pagination cannot be rendered at the bottom in this version of Oracle");
        }
        if (offset != null) {
          w.write("\nOFFSET " + offset + " ROWS");
        }
        if (limit != null) {
          w.write("\nFETCH NEXT " + limit + " ROWS ONLY");
        }
      }

      private static final String HOTROD_ROWNUM_COLUMN = "_HotRod_RowNum_";

      @Override
      public void renderBeginEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (versionIsAtLeast(12, 1)) {
          throw new UnsupportedLiveSQLFeatureException(
              "Pagination cannot be rendered in an enclosing way in this version of Oracle.");
        }
        if (offset != null) {
          w.write("SELECT * FROM (");
          w.enterLevel();
          w.write("\n");
        }
        w.write("SELECT x.*, rownum as \"" + HOTROD_ROWNUM_COLUMN + "\" FROM (");
        w.enterLevel();
        w.write("\n");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (versionIsAtLeast(12, 1)) {
          throw new UnsupportedLiveSQLFeatureException(
              "Pagination cannot be rendered in an enclosing way in this version of Oracle");
        }
        if (limit != null) {
          w.exitLevel();
          w.write("\n");
          w.write(") x WHERE rownum <= " + (offset != null ? "" + offset + " + " : "") + limit);
        } else {
          w.exitLevel();
          w.write("\n");
          w.write(") x");
        }
        if (offset != null) {
          w.exitLevel();
          w.write("\n");
          w.write(") y WHERE \"" + HOTROD_ROWNUM_COLUMN + "\" > " + offset);
        }
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
              "The Oracle database does not support locking FOR SHARE in SELECT statements");
        }

        if (lockingConcurrency != null) {
          switch (lockingConcurrency) {
          case NO_WAIT:
            return "FOR UPDATE NOWAIT";
          case WAIT:
            if (waitTime instanceof Integer) {
              return "FOR UPDATE WAIT " + waitTime;
            } else {
              throw new UnsupportedLiveSQLFeatureException(
                  "The Oracle database supports locking with WAIT <n> in SELECT statements only for integer values of <n>, "
                      + "but the supplied value (" + waitTime + ") is not an integer");
            }
          case SKIP_LOCKED:
            return "FOR UPDATE SKIP LOCKED";
          default:
            return "FOR UPDATE";
          }
        } else {
          return "FOR UPDATE";
        }

      }

    };
  }

  // Set Operator Rendering

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
        if (w.getSQLDialect().getMajorVersion() >= 21) {
          w.write("EXCEPT");
        } else {
          w.write("MINUS");
        }
      }

      @Override
      public void renderExceptAll(final QueryWriter w) {
        if (w.getSQLDialect().getMajorVersion() >= 21) {
          w.write("EXCEPT ALL");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "LiveSQL supports the EXCEPT ALL (MINUS ALL) set operator for Oracle database starting in version 21c; "
                  + "this version is " + w.getSQLDialect().renderVersion()
                  + ". Nevertheless, this operator can be simulated using an anti join");
        }
      }

      @Override
      public void renderIntersect(final QueryWriter w) {
        w.write("INTERSECT");
      }

      @Override
      public void renderIntersectAll(final QueryWriter w) {
        if (w.getSQLDialect().getMajorVersion() >= 21) {
          w.write("INTERSECT ALL");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "LiveSQL supports the INTERSECT ALL set operator for Oracle database starting in version 21c; "
                  + "this version is " + w.getSQLDialect().renderVersion()
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

      @Override
      public void groupConcat(final QueryWriter w, final boolean distinct, final CharExpression value,
          final List<OrderingTerm> ordering, final CharExpression separator) {
        if (distinct) {
          throw new UnsupportedLiveSQLFeatureException(
              "Oracle does not support DISTINCT on the GROUP_CONCAT() function (listagg())");
        }
        if (ordering == null || ordering.isEmpty()) {
          throw new UnsupportedLiveSQLFeatureException("In Oracle GROUP_CONCAT() requires ordering columns");
        }
        w.write("listagg(");
        Shield.renderTo(value, w);
        if (separator != null) {
          w.write(", ");
          Shield.renderTo(separator, w);
        }
        w.write(")");
        w.write(" withing group (ORDER BY ");
        Separator sep = new Separator();
        for (OrderingTerm t : ordering) {
          w.write(sep.render());
          OHelper.renderTo(t, w);
        }
        w.write(")");
      }

      // Arithmetic functions

      @Override
      public void remainder(final QueryWriter w, final NumericExpression a, final NumericExpression b) {
        w.write("mod(");
        Shield.renderTo(a, w);
        w.write(", ");
        Shield.renderTo(b, w);
        w.write(")");
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
      public void locate(final QueryWriter w, final CharExpression substring, final CharExpression string,
          final NumericExpression from) {
        if (from == null) {
          this.write(w, "instr", string, substring);
        } else {
          this.write(w, "instr", string, substring, from);
        }
      }

      // Date/Time functions

      @Override
      public void currentDate(final QueryWriter w) {
        w.write("trunc(current_date)");
      }

      @Override
      public void currentTime(final QueryWriter w) {
        w.write("to_char(current_date, 'HH24:MI:SS')");
      }

      @Override
      public void currentDateTime(final QueryWriter w) {
        w.write("current_timestamp");
      }

      @Override
      public void date(final QueryWriter w, final DateTimeExpression datetime) {
        w.write("trunc(");
        Shield.renderTo(datetime, w);
        w.write(")");
      }

      @Override
      public void time(final QueryWriter w, final DateTimeExpression datetime) {
        w.write("to_char(");
        Shield.renderTo(datetime, w);
        w.write(", 'HH24:MI:SS')");
      }

      @Override
      public void dateTime(final QueryWriter w, final DateTimeExpression date, final DateTimeExpression time) {
        w.write("to_date(to_char(");
        Shield.renderTo(date, w);
        w.write(", 'yyyymmdd') || ' ' || ");
        Shield.renderTo(time, w);
        w.write(", 'yyyymmdd hh24:mi:ss')");
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
    return "\"" + verbatim.replace("\"", "\"\"").replace("'", "''") + "\"";
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
        throw new InvalidLiteralException("Oracle database does not implement the TIME data type.");
      }

      @Override
      public String renderTimestamp(final String isoTimestamp, final int precision) {
        return "TIMESTAMP '" + isoTimestamp + "'";
      }

      @Override
      public String renderOffsetTime(final String isoTime, final String isoOffset, final int precision) {
        throw new InvalidLiteralException("Oracle database does not implement the TIME WITH TIME ZONE data type.");
      }

      @Override
      public String renderOffsetTimestamp(final String isoTimestamp, final String isoOffset, final int precision) {
        return "TIMESTAMP '" + isoTimestamp + " " + isoOffset + "'";
      }

    };
  }

  @Override
  public BooleanLiteralRenderer getBooleanLiteralRenderer() {
    return new BooleanLiteralRenderer() {

      @Override
      public void renderTrue(final QueryWriter w) {
        if (versionIsAtLeast(23)) {
          w.write("true");
        } else {
          w.write("1 = 1");
        }
      }

      @Override
      public void renderFalse(final QueryWriter w) {
        if (versionIsAtLeast(23)) {
          w.write("false");
        } else {
          w.write("1 = 0");
        }
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
