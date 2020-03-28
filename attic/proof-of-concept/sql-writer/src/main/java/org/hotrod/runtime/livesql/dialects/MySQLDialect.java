package org.hotrod.runtime.livesql.dialects;

import org.hotrod.runtime.livesql.FullOuterJoin;
import org.hotrod.runtime.livesql.InnerJoin;
import org.hotrod.runtime.livesql.Join;
import org.hotrod.runtime.livesql.LeftOuterJoin;
import org.hotrod.runtime.livesql.QueryWriter;
import org.hotrod.runtime.livesql.RightOuterJoin;
import org.hotrod.runtime.livesql.exceptions.UnsupportedFeatureException;
import org.hotrod.runtime.livesql.expressions.Expression;

public class MySQLDialect extends SQLDialect {

  public MySQLDialect(final String productName, final String productVersion, final int majorVersion,
      final int minorVersion) {
    super(productName, productVersion, majorVersion, minorVersion);
  }

  // Identifier rendering

  @Override
  public IdentifierRenderer getIdentifierRenderer() {
    // Identifier names are case sensitive in MySQL
    return new IdentifierRenderer("[a-zA-Z][a-zA-Z0-9_]*", "`", "`", true);
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
          throw new UnsupportedFeatureException("Full outer joins are not supported in MySQL");
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
        throw new UnsupportedFeatureException("Pagination can only be rendered at the bottom in MySQL");
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
        throw new UnsupportedFeatureException("Pagination can only be rendered at the bottom in MySQL");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedFeatureException("Pagination can only be rendered at the bottom in MySQL");
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
      public void trunc(final QueryWriter w, final Expression<Number> x, final Expression<Number> places) {
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

}
