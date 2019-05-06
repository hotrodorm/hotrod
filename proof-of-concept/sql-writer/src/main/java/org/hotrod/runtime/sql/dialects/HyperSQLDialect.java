package org.hotrod.runtime.sql.dialects;

import java.util.Date;

import org.hotrod.runtime.sql.FullOuterJoin;
import org.hotrod.runtime.sql.InnerJoin;
import org.hotrod.runtime.sql.Join;
import org.hotrod.runtime.sql.LeftOuterJoin;
import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.RightOuterJoin;
import org.hotrod.runtime.sql.exceptions.UnsupportedFeatureException;
import org.hotrod.runtime.sql.expressions.Expression;

public class HyperSQLDialect extends SQLDialect {

  public HyperSQLDialect(final String productName, final String productVersion, final int majorVersion,
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
        throw new UnsupportedFeatureException("Pagination can only be rendered at the bottom in HyperSQL");
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
        throw new UnsupportedFeatureException("Pagination can only be rendered at the bottom in HyperSQL");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedFeatureException("Pagination can only be rendered at the bottom in HyperSQL");
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
        if (places == null) {
          throw new UnsupportedFeatureException(
              "HyperSQL requires the number of decimal places to be specified when using the ROUND() function");
        }
        this.write(w, "round", x, places);
      }

      // String functions

      @Override
      public void substr(final QueryWriter w, final Expression<String> string, final Expression<Number> from,
          final Expression<Number> length) {
        if (length == null) {
          throw new UnsupportedFeatureException(
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
      public void date(final QueryWriter w, final Expression<Date> datetime) {
        w.write("cast(");
        datetime.renderTo(w);
        w.write(" as date)");
      }

      @Override
      public void time(final QueryWriter w, final Expression<Date> datetime) {
        w.write("cast(");
        datetime.renderTo(w);
        w.write(" as time)");
      }

    };
  }

}
