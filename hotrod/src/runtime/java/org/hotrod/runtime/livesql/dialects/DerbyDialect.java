package org.hotrod.runtime.livesql.dialects;

import java.util.Date;
import java.util.List;

import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.FullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.InnerJoin;
import org.hotrod.runtime.livesql.queries.select.Join;
import org.hotrod.runtime.livesql.queries.select.LeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.RightOuterJoin;
import org.hotrod.runtime.livesql.util.Separator;

public class DerbyDialect extends SQLDialect {

  public DerbyDialect(final String productName, final String productVersion, final int majorVersion,
      final int minorVersion) {
    super(productName, productVersion, majorVersion, minorVersion);
  }

  // Identifier rendering

  @Override
  public IdentifierRenderer getIdentifierRenderer() {
    // Identifier names are by default upper case in Apache Derby
    return new IdentifierRenderer("[A-Z][A-Z0-9_]*", "\"", "\"", false);
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
          throw new UnsupportedLiveSQLFeatureException("Full outer joins are not supported in Derby database");
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
        if (offset != null || limit != null) {
          if (!versionIsAtLeast(10, 5)) {
            throw new UnsupportedLiveSQLFeatureException("This version of Derby (" + renderVersion()
                + ") does not support the OFFSET or LIMIT clauses. Derby versions 10.5 and newer do");
          }
        }
        return PaginationType.BOTTOM;
      }

      @Override
      public void renderTopPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("In Derby pagination cannot be rendered at the top");
      }

      @Override
      public void renderBottomPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (offset != null) {
          w.write("\nOFFSET " + offset + " ROWS");
        }
        if (limit != null) {
          w.write("\nFETCH NEXT " + limit + " ROWS ONLY");
        }
      }

      @Override
      public void renderBeginEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("In Derby pagination cannot be rendered in an enclosing way");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("In Derby pagination cannot be rendered in an enclosing way");
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
        throw new UnsupportedLiveSQLFeatureException("GROUP_CONCAT() is not supported in Derby database");
      }

      // Arithmetic functions

      @Override
      public void power(final QueryWriter w, final Expression<Number> x, final Expression<Number> exponent) {
        w.write("exp(");
        exponent.renderTo(w);
        w.write(" * ln(");
        x.renderTo(w);
        w.write("))");
      }

      @Override
      public void logarithm(final QueryWriter w, final Expression<Number> x, final Expression<Number> base) {
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
      public void round(final QueryWriter w, final Expression<Number> x, final Expression<Number> places) {
        throw new UnsupportedLiveSQLFeatureException("ROUND() is not supported in Derby database");
      }

      @Override
      public void trunc(final QueryWriter w, final Expression<Number> x, final Expression<Number> places) {
        throw new UnsupportedLiveSQLFeatureException("TRUNC() is not supported in Derby database");
      }

      // String functions

      @Override
      public void concat(final QueryWriter w, final List<Expression<String>> strings) {
        w.write("(");
        Separator sep = new Separator(" || ");
        for (Expression<String> s : strings) {
          w.write(sep.render());
          s.renderTo(w);
        }
        w.write(")");
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
      public void dateTime(final QueryWriter w, final Expression<Date> date, final Expression<Date> time) {
        throw new UnsupportedLiveSQLFeatureException("DATETIME() is not supported in Derby database");
      }

      @Override
      public void extract(final QueryWriter w, final Expression<Date> datetime, final DateTimeFieldExpression field) {
        throw new UnsupportedLiveSQLFeatureException("EXTRACT() is not supported in Derby database");
      }

    };
  }

}
