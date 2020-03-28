package org.hotrod.runtime.livesql.dialects;

import java.util.Date;
import java.util.List;

import org.hotrod.runtime.livesql.FullOuterJoin;
import org.hotrod.runtime.livesql.InnerJoin;
import org.hotrod.runtime.livesql.Join;
import org.hotrod.runtime.livesql.LeftOuterJoin;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.RightOuterJoin;
import org.hotrod.runtime.livesql.exceptions.UnsupportedFeatureException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.util.Separator;

public class PostgreSQLDialect extends SQLDialect {

  public PostgreSQLDialect(final String productName, final String productVersion, final int majorVersion,
      final int minorVersion) {
    super(productName, productVersion, majorVersion, minorVersion);
  }

  // Identifier rendering

  @Override
  public IdentifierRenderer getIdentifierRenderer() {
    // Identifier names are by default lower case in PostgreSQL
    return new IdentifierRenderer("[a-z][a-z0-9_]*", "\"", "\"", false);
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
        return offset != null || limit != null ? PaginationType.BOTTOM : null;
      }

      @Override
      public void renderTopPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedFeatureException("Pagination can only be rendered at the bottom in PostgreSQL");
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
        throw new UnsupportedFeatureException("Pagination can only be rendered at the bottom in PostgreSQL");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedFeatureException("Pagination can only be rendered at the bottom in PostgreSQL");
      }

    };
  }

  // Function rendering

  @Override
  public FunctionRenderer getFunctionRenderer() {
    return new FunctionRenderer() {

      // General purpose functions

      @Override
      public void groupConcat(final QueryWriter w, final boolean distinct, final Expression<String> value,
          final List<OrderingTerm> ordering, final Expression<String> separator) {
        if (!versionIsAtLeast(9)) {
          throw new UnsupportedFeatureException("This PostgreSQL version (" + renderVersion()
              + ") does not support the GROUP_CONCAT() function (string_agg()). Only available on PostgreSQL 9.0 or newer");
        }
        if (distinct) {
          throw new UnsupportedFeatureException(
              "PostgreSQL does not support DISTINCT on the GROUP_CONCAT() function (string_agg())");
        }
        if (separator == null) {
          throw new UnsupportedFeatureException(
              "PostgreSQL requires the separator to be specified on the GROUP_CONCAT() function (string_agg())");
        }
        w.write("string_agg(");
        value.renderTo(w);
        w.write(", ");
        separator.renderTo(w);
        if (ordering != null && !ordering.isEmpty()) {
          w.write(" ORDER BY ");
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
          this.write(w, "ln", x);
        } else {
          this.write(w, "log", base, x);
        }
      }

      // String functions

      @Override
      public void locate(final QueryWriter w, final Expression<String> substring, final Expression<String> string,
          final Expression<Number> from) {
        if (from == null) {
          this.write(w, "strpos", string, substring);
        } else {
          throw new UnsupportedFeatureException(
              "PostgreSQL does not support the parameter 'from' in the LOCATE function ('strpos' in PostgreSQL lingo)");
        }
      }

      // Date/Time functions

      @Override
      public void currentDate(final QueryWriter w) {
        w.write("current_date");
      }

      @Override
      public void currentTime(final QueryWriter w) {
        w.write("current_time");
      }

      @Override
      public void currentDateTime(final QueryWriter w) {
        w.write("current_timestamp");
      }

      @Override
      public void date(final QueryWriter w, final Expression<Date> datetime) {
        datetime.renderTo(w);
        w.write("::date");
      }

      @Override
      public void time(final QueryWriter w, final Expression<Date> datetime) {
        datetime.renderTo(w);
        w.write("::time");
      }

      @Override
      public void dateTime(final QueryWriter w, final Expression<Date> date, final Expression<Date> time) {
        w.write("(");
        date.renderTo(w);
        w.write(" + ");
        time.renderTo(w);
        w.write(")");
      }

    };
  }

}
