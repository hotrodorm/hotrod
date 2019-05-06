package org.hotrod.runtime.sql.dialects;

import java.util.Date;
import java.util.List;

import org.hotrod.runtime.sql.FullOuterJoin;
import org.hotrod.runtime.sql.InnerJoin;
import org.hotrod.runtime.sql.Join;
import org.hotrod.runtime.sql.LeftOuterJoin;
import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.RightOuterJoin;
import org.hotrod.runtime.sql.SQL;
import org.hotrod.runtime.sql.exceptions.UnsupportedFeatureException;
import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.expressions.datetime.DateTimeFieldExpression;
import org.hotrod.runtime.sql.ordering.OrderingTerm;

import sql.util.Separator;

public class SQLServerDialect extends SQLDialect {

  public SQLServerDialect(final String productName, final String productVersion, final int majorVersion,
      final int minorVersion) {
    super(productName, productVersion, majorVersion, minorVersion);
  }

  // Identifier rendering

  @Override
  public IdentifierRenderer getIdentifierRenderer() {
    // Identifier names are case insensitive in SQL Server
    return new IdentifierRenderer("[a-zA-Z][a-zA-Z0-9_]*", "\"", "\"", false);
  }

  // Join rendering

  @Override
  public JoinRenderer getJoinRenderer() {
    return new JoinRenderer() {

      @Override
      public String renderJoinKeywords(final Join join) throws UnsupportedFeatureException {
        if (join instanceof InnerJoin) {
          return "JOIN";
        } else if (join instanceof LeftOuterJoin) {
          return "LEFT OUTER JOIN";
        } else if (join instanceof RightOuterJoin) {
          return "RIGHT OUTER JOIN";
        } else if (join instanceof FullOuterJoin) {
          return "FULL OUTER JOIN";
        } else {
          return "CROSS JOIN";
        }
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
            throw new UnsupportedFeatureException(
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
          throw new UnsupportedFeatureException(
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
          w.write("FETCH NEXT " + limit + " ROWS ONLY");
        }
      }

      @Override
      public void renderBeginEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedFeatureException("Pagination cannot be rendered in enclosing fashion in SQL Server");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedFeatureException("Pagination cannot be rendered in enclosing fashion in SQL Server");
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
      public void groupConcat(final QueryWriter w, final boolean distinct, final Expression<String> value,
          final List<OrderingTerm> ordering, final Expression<String> separator) {
        if (versionIsAtLeast(14)) { // (SQL Server 2017)
          throw new UnsupportedFeatureException("This SQL Server version (" + renderVersion()
              + ") does not support the GROUP_CONCAT() function (string_agg()). It's available since version 14.0 (SQL Server 2017)");
        }
        if (distinct) {
          throw new UnsupportedFeatureException(
              "SQL Server does not support DISTINCT on the GROUP_CONCAT() function (string_agg())");
        }
        if (separator == null) {
          throw new UnsupportedFeatureException(
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
      public void logarithm(final QueryWriter w, final Expression<Number> x, final Expression<Number> base) {
        if (base == null) {
          this.write(w, "log", x);
        } else {
          this.write(w, "log", x, base);
        }
      }

      @Override
      public void round(final QueryWriter w, final Expression<Number> x, final Expression<Number> places) {
        if (places == null) {
          throw new UnsupportedFeatureException(
              "SQL Server requires the number of decimal places to be specified on the ROUND() function");
        }
        this.write(w, "round", x, places);
      }

      @Override
      public void trunc(final QueryWriter w, final Expression<Number> x, final Expression<Number> places) {
        if (places == null) {
          throw new UnsupportedFeatureException(
              "SQL Server requires the number of decimal places to be specified on the TRUNC() function (round())");
        }

        this.write(w, "round", x, places, SQL.box(1));
      }

      // String functions

      @Override
      public void length(final QueryWriter w, final Expression<String> string) {
        this.write(w, "len", string);
      }

      @Override
      public void locate(final QueryWriter w, final Expression<String> substring, final Expression<String> string,
          final Expression<Number> from) {
        if (from == null) {
          this.write(w, "charindex", substring, string);
        } else {
          this.write(w, "charindex", substring, string, from);
        }
      }

      @Override
      public void substr(final QueryWriter w, final Expression<String> string, final Expression<Number> from,
          final Expression<Number> length) {
        if (length == null) {
          throw new UnsupportedFeatureException(
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
      public void date(final QueryWriter w, final Expression<Date> datetime) {
        w.write("convert(date, ");
        datetime.renderTo(w);
        w.write(")");
      }

      @Override
      public void time(final QueryWriter w, final Expression<Date> datetime) {
        w.write("convert(time, ");
        datetime.renderTo(w);
        w.write(")");
      }

      @Override
      public void dateTime(final QueryWriter w, final Expression<Date> date, final Expression<Date> time) {
        w.write("(");
        date.renderTo(w);
        w.write(" + ");
        time.renderTo(w);
        w.write(")");
      }

      @Override
      public void extract(final QueryWriter w, final Expression<Date> datetime, final DateTimeFieldExpression field) {
        w.write("datepart(");
        field.renderTo(w);
        w.write(", ");
        datetime.renderTo(w);
        w.write(")");
      }

    };
  }

}
