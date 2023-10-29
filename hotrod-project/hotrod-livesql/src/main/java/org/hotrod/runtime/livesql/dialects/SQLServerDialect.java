package org.hotrod.runtime.livesql.dialects;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.exceptions.InvalidLiteralException;
import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberConstant;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
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
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.RightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.UnionJoin;
import org.hotrodorm.hotrod.utils.Separator;

public class SQLServerDialect extends LiveSQLDialect {

  public SQLServerDialect(final boolean discovered, final String productName, final String productVersion,
      final int majorVersion, final int minorVersion) {
    super(discovered, productName, productVersion, majorVersion, minorVersion);
  }

  // WITH rendering

  @Override
  public WithRenderer getWithRenderer() {
    return (c) -> "WITH";
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
          if (versionIsAtLeast(12, 0)) {
            return "CROSS APPLY";
          }
          throw new UnsupportedLiveSQLFeatureException(
              "LiveSQL supports lateral joins in the SQL Server database starting in version 2014, "
                  + "but the current version is reported as " + renderVersion());
        } else if (join instanceof LeftJoinLateral) {
          if (versionIsAtLeast(12, 0)) {
            return "OUTER APPLY";
          }
          throw new UnsupportedLiveSQLFeatureException(
              "LiveSQL supports lateral joins in the SQL Server database starting in version 2014, "
                  + "but the current version is reported as " + renderVersion());
        } else if (join instanceof UnionJoin) {
          throw new UnsupportedLiveSQLFeatureException("Union joins are not supported in SQL Server database");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in SQL Server database");
        }
      }

      @Override
      public String renderOptionalOnPredicate(final Join join) throws UnsupportedLiveSQLFeatureException {
        return "";
      }

    };
  }

  // Pagination rendering

  public PaginationRenderer getPaginationRenderer() {
    return new PaginationRenderer() {

      @Override
      public PaginationType getPaginationType(final Integer offset, final Integer limit) {
        if (offset != null) {
          if (!versionIsAtLeast(11)) { // SQL Server 2012
            throw new UnsupportedLiveSQLFeatureException(
                "OFFSET not supported on this version of SQL Server. OFFSET is supported starting on SQL Server 2012 (internal version 11.0)");
          }
        }
        return versionIsAtLeast(11) ? PaginationType.BOTTOM : PaginationType.TOP;
      }

      @Override
      public void renderTopPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (offset == null && !versionIsAtLeast(11)) {
          w.write(" top " + limit);
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "In SQL Server before version 2012, LIMIT can only be used without OFFSET as the TOP clause");
        }
      }

      @Override
      public void renderBottomPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (offset != null) {
          w.write("\nOFFSET " + offset + " ROWS");
        } else if (limit != null) {
          w.write("\nOFFSET 0 ROWS");
        }
        if (limit != null) {
          w.write("\nFETCH NEXT " + limit + " ROWS ONLY");
        }
      }

      @Override
      public void renderBeginEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException(
            "Pagination cannot be rendered in enclosing fashion in SQL Server");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException(
            "Pagination cannot be rendered in enclosing fashion in SQL Server");
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
        throw new UnsupportedLiveSQLFeatureException("SQL Server does not support the EXCEPT ALL set operator. "
            + "Nevertheless, this operator can be simulated using an anti join");
      }

      @Override
      public void renderIntersect(final QueryWriter w) {
        w.write("INTERSECT");
      }

      @Override
      public void renderIntersectAll(final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("SQL Server does not support the INTERSECT ALL set operator. "
            + "Nevertheless, this operator can be simulated using a semi join");
      }

    };
  }

  // Function rendering

  @Override
  public FunctionRenderer getFunctionRenderer() {
    return new FunctionRenderer() {

      // General purpose functions

      // 8.0 for SQL Server 2000.
      // 9.0 for SQL Server 2005.
      // 10.0 for SQL Server 2008.
      // 10.5 for SQL Server 2008 R2.
      // 11.0 for SQL Server 2012.
      // 12.0 for SQL Server 2014.
      // 13.0 for SQL Server 2016.
      // 14.0 for SQL Server 2017.

      @Override
      public void groupConcat(final QueryWriter w, final boolean distinct, final StringExpression value,
          final List<OrderingTerm> ordering, final StringExpression separator) {
        if (versionIsAtLeast(14)) { // (SQL Server 2017)
          throw new UnsupportedLiveSQLFeatureException("This SQL Server version (" + renderVersion()
              + ") does not support the GROUP_CONCAT() function (string_agg()). It's available since version 14.0 (SQL Server 2017)");
        }
        if (distinct) {
          throw new UnsupportedLiveSQLFeatureException(
              "SQL Server does not support DISTINCT on the GROUP_CONCAT() function (string_agg())");
        }
        if (separator == null) {
          throw new UnsupportedLiveSQLFeatureException(
              "SQL Server requires the separator to be specified on the GROUP_CONCAT() function (string_agg())");
        }
        w.write("string_agg(");
        value.renderTo(w);
        w.write(", ");
        separator.renderTo(w);
        w.write(")");
        if (ordering != null && !ordering.isEmpty()) {
          w.write(" within group (ORDER BY ");
          Separator sep = new Separator();
          for (OrderingTerm t : ordering) {
            w.write(sep.render());
            t.renderTo(w);
          }
        }
        w.write(")");
      }

      // Arithmetic functions

      @Override
      public void logarithm(final QueryWriter w, final NumberExpression x, final NumberExpression base) {
        if (base == null) {
          this.write(w, "log", x);
        } else {
          this.write(w, "log", x, base);
        }
      }

      @Override
      public void round(final QueryWriter w, final NumberExpression x, final NumberExpression places) {
        if (places == null) {
          throw new UnsupportedLiveSQLFeatureException(
              "SQL Server requires the number of decimal places to be specified on the ROUND() function");
        }
        this.write(w, "round", x, places);
      }

      @Override
      public void trunc(final QueryWriter w, final NumberExpression x, final NumberExpression places) {
        if (places == null) {
          throw new UnsupportedLiveSQLFeatureException(
              "SQL Server requires the number of decimal places to be specified on the TRUNC() function (round())");
        }

        this.write(w, "round", x, places, new NumberConstant(1));
      }

      // String functions

      @Override
      public void length(final QueryWriter w, final StringExpression string) {
        this.write(w, "len", string);
      }

      @Override
      public void locate(final QueryWriter w, final StringExpression substring, final StringExpression string,
          final NumberExpression from) {
        if (from == null) {
          this.write(w, "charindex", substring, string);
        } else {
          this.write(w, "charindex", substring, string, from);
        }
      }

      @Override
      public void substr(final QueryWriter w, final StringExpression string, final NumberExpression from,
          final NumberExpression length) {
        if (length == null) {
          throw new UnsupportedLiveSQLFeatureException(
              "SQL Server requires the length parameter to be be specified on the SUBSTR() function");
        }
        this.write(w, "substring", string, from, length);
      }

      // Date/Time functions

      @Override
      public void currentDate(final QueryWriter w) {
        w.write("getdate()");
      }

      @Override
      public void currentTime(final QueryWriter w) {
        w.write("convert(time, current_timestamp)");
      }

      @Override
      public void currentDateTime(final QueryWriter w) {
        w.write("current timestamp");
      }

      @Override
      public void date(final QueryWriter w, final DateTimeExpression datetime) {
        w.write("convert(date, ");
        datetime.renderTo(w);
        w.write(")");
      }

      @Override
      public void time(final QueryWriter w, final DateTimeExpression datetime) {
        w.write("convert(time, ");
        datetime.renderTo(w);
        w.write(")");
      }

      @Override
      public void dateTime(final QueryWriter w, final DateTimeExpression date, final DateTimeExpression time) {
        w.write("(");
        date.renderTo(w);
        w.write(" + ");
        time.renderTo(w);
        w.write(")");
      }

      @Override
      public void extract(final QueryWriter w, final DateTimeExpression datetime, final DateTimeFieldExpression field) {
        w.write("datepart(");
        field.renderTo(w);
        w.write(", ");
        datetime.renderTo(w);
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
      return "\"" + canonical.replace("\"", "\"\"").replace("]", "]]") + "\"";
    }
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
        if (precision > 7) {
          throw new InvalidLiteralException(
              "SQL Server's TIME literals accept a maximum precision of 7, but " + precision + " was specified.");
        }
        return "cast('" + isoTime + "' as TIME)";
      }

      @Override
      public String renderTimestamp(final String isoTimestamp, final int precision) {
        if (precision > 7) {
          throw new InvalidLiteralException("SQL Server's DATETIME2 literals accept a maximum precision of 7, " + "but "
              + precision + " was specified.");
        }
        return "cast('" + isoTimestamp + "' as DATETIME2)";
      }

      @Override
      public String renderOffsetTime(final String isoTime, final String isoOffset, final int precision) {
        throw new InvalidLiteralException("SQL Server does not implement the TIME WITH TIME ZONE data type.");
      }

      @Override
      public String renderOffsetTimestamp(final String isoTimestamp, final String isoOffset, final int precision) {
        if (precision > 7) {
          throw new InvalidLiteralException("SQL Server's DATETIMEOFFSET literals accept a maximum precision of 7, but "
              + precision + " was specified.");
        }
        return "cast('" + isoTimestamp + " " + isoOffset + "' as DATETIMEOFFSET)";
      }

    };
  }

  @Override
  public BooleanLiteralRenderer getBooleanLiteralRenderer() {
    return new BooleanLiteralRenderer() {

      @Override
      public void renderTrue(final LiveSQLContext context, final QueryWriter w) {
        w.write("1 = 1");
      }

      @Override
      public void renderFalse(final LiveSQLContext context, final QueryWriter w) {
        w.write("1 = 0");
      }

    };
  }

}
