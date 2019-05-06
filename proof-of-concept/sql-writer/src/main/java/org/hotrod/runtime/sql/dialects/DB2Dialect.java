package org.hotrod.runtime.sql.dialects;

import java.util.List;

import org.hotrod.runtime.sql.FullOuterJoin;
import org.hotrod.runtime.sql.InnerJoin;
import org.hotrod.runtime.sql.Join;
import org.hotrod.runtime.sql.LeftOuterJoin;
import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.RightOuterJoin;
import org.hotrod.runtime.sql.exceptions.UnsupportedFeatureException;
import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.ordering.OrderingTerm;

import sql.util.Separator;

public class DB2Dialect extends SQLDialect {

  public DB2Dialect(final String productName, final String productVersion, final int majorVersion,
      final int minorVersion) {
    super(productName, productVersion, majorVersion, minorVersion);
  }

  // Identifier rendering

  @Override
  public IdentifierRenderer getIdentifierRenderer() {
    // Identifier names are by default upper case in DB2
    return new IdentifierRenderer("[A-Z][A-Z0-9_]*", "\"", "\"", false);
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
          if (!versionIsAtLeast(11, 1)) {
            throw new UnsupportedFeatureException("This version of DB2 (" + renderVersion()
                + ") does not support the OFFSET clause. DB2 11.1 and newer do.");
          }
        }
        return PaginationType.BOTTOM;
      }

      @Override
      public void renderTopPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedFeatureException("In DB2 pagination cannot be rendered at the top.");
      }

      @Override
      public void renderBottomPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (offset != null) {
          if (!versionIsAtLeast(11, 1)) {
            throw new UnsupportedFeatureException("This version of DB2 (" + renderVersion()
                + ") does not support the OFFSET clause. Support starts in DB2 11.1.");
          }
        }
        if (offset != null) {
          w.write("\nOFFSET " + offset + " ROWS");
        }
        if (limit != null) {
          w.write("\nFETCH NEXT " + limit + " ROWS ONLY");
        }
      }

      @Override
      public void renderBeginEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedFeatureException("In DB2 pagination cannot be rendered in an enclosing way.");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedFeatureException("In DB2 pagination cannot be rendered in an enclosing way.");
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
        if (distinct) {
          throw new UnsupportedFeatureException(
              "DB2 does not support DISTINCT on the GROUP_CONCAT() function (listagg()).");
        }
        w.write("listagg(");
        value.renderTo(w);
        if (separator != null) {
          w.write(", ");
          separator.renderTo(w);
        }
        w.write(")");
        if (ordering != null && !ordering.isEmpty()) {
          w.write(" withing group (ORDER BY ");
          Separator sep = new Separator();
          for (OrderingTerm t : ordering) {
            w.write(sep.render());
            t.renderTo(w);
          }
          w.write(")");
        }
      }

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

      // String functions

      // Date/Time functions

      @Override
      public void currentDate(final QueryWriter w) {
        w.write("current date");
      }

      @Override
      public void currentTime(final QueryWriter w) {
        w.write("current time");
      }

      @Override
      public void currentDateTime(final QueryWriter w) {
        w.write("current timestamp");
      }

    };
  }

}
