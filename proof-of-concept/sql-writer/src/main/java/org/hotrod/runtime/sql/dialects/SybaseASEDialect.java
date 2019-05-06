package org.hotrod.runtime.sql.dialects;

import java.util.Date;
import java.util.List;

import org.hotrod.runtime.sql.FullOuterJoin;
import org.hotrod.runtime.sql.InnerJoin;
import org.hotrod.runtime.sql.Join;
import org.hotrod.runtime.sql.LeftOuterJoin;
import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.RightOuterJoin;
import org.hotrod.runtime.sql.exceptions.UnsupportedFeatureException;
import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.expressions.datetime.DateTimeFieldExpression;
import org.hotrod.runtime.sql.ordering.OrderingTerm;

import sql.util.Separator;

public class SybaseASEDialect extends SQLDialect {

  public SybaseASEDialect(final String productName, final String productVersion, final int majorVersion,
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
          throw new UnsupportedFeatureException("Full outer joins are not supported in Sybase ASE database");
        } else {
          throw new UnsupportedFeatureException("Cross joins are not supported in Sybase ASE database");
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
          throw new UnsupportedFeatureException("OFFSET is not supported Sybase ASE");
        }
        return PaginationType.TOP;
      }

      @Override
      public void renderTopPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (offset == null) {
          w.write(" top " + limit);
        } else {
          throw new UnsupportedFeatureException("OFFSET is not supported Sybase ASE");
        }
      }

      @Override
      public void renderBottomPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedFeatureException("Pagination cannot be rendered at the bottom in Sybase ASE");
      }

      @Override
      public void renderBeginEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedFeatureException("Pagination cannot be rendered in enclosing fashion in Sybase ASE");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedFeatureException("Pagination cannot be rendered in enclosing fashion in Sybase ASE");
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
        throw new UnsupportedFeatureException("GROUP_CONCAT() is not supported in Sybase ASE");
      }

      // Arithmetic functions

      @Override
      public void logarithm(final QueryWriter w, final Expression<Number> x, final Expression<Number> base) {
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
      public void round(final QueryWriter w, final Expression<Number> x, final Expression<Number> places) {
        if (places == null) {
          throw new UnsupportedFeatureException(
              "Sybase ASE requires the number of decimal places to be specified on the ROUND() function");
        }
        this.write(w, "round", x, places);
      }

      @Override
      public void trunc(final QueryWriter w, final Expression<Number> x, final Expression<Number> places) {
        throw new UnsupportedFeatureException("Sybase ASE does not support the TRUNC()function");
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

      @Override
      public void length(final QueryWriter w, final Expression<String> string) {
        this.write(w, "char_length", string);
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
      public void date(final QueryWriter w, final Expression<Date> datetime) {
        throw new UnsupportedFeatureException("Sybase ASE does not suppor the DATE() function");
      }

      @Override
      public void time(final QueryWriter w, final Expression<Date> datetime) {
        throw new UnsupportedFeatureException("Sybase ASE does not suppor the TIME() function");
      }

      @Override
      public void dateTime(final QueryWriter w, final Expression<Date> date, final Expression<Date> time) {
        throw new UnsupportedFeatureException("Sybase ASE does not suppor the DATETIME() function");
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
