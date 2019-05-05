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

public class PostgreSQLDialect extends SQLDialect {

  public PostgreSQLDialect(final String productName, final String productVersion, final int majorVersion,
      final int minorVersion) {
    super(productName, productVersion, majorVersion, minorVersion);
  }

  // Identifier rendering

  @Override
  public IdentifierRenderer getIdentifierRenderer() {
    // Identifier names are by default lower case in PostgreSQL
    return new IdentifierRenderer("[a-z][a-z0-9_]*", "\"", "\"");
  }

  // Join rendering

  @Override
  public JoinRenderer getJoinRenderer() {
    return new JoinRenderer() {

      @Override
      public String renderJoinKeywords(final Join join) throws UnsupportedFeatureException {
        if (join instanceof InnerJoin) {
          return "join";
        } else if (join instanceof LeftOuterJoin) {
          return "left outer join";
        } else if (join instanceof RightOuterJoin) {
          return "right outer join";
        } else if (join instanceof FullOuterJoin) {
          return "full outer join";
        } else {
          return "cross join";
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
            w.write("limit " + limit + " offset " + offset);
          } else {
            w.write("limit " + limit);
          }
        } else {
          w.write("offset " + offset);
        }
      }

    };
  }

  // Function rendering

  @Override
  public FunctionRenderer getFunctionRenderer() {
    return new FunctionRenderer() {

      // Math functions

      public void logarithm(final QueryWriter w, final Expression<Number> x, final Expression<Number> base) {
        if (base == null) {
          this.write(w, "LN", x);
        } else {
          this.write(w, "LOG", base, x);
        }
      }

      // String functions

      public void locate(final QueryWriter w, final Expression<String> substring, final Expression<String> string,
          final Expression<Number> from) {
        if (from == null) {
          this.write(w, "STRPOS", string, substring);
        } else {
          throw new UnsupportedFeatureException(
              "PostgreSQL does not support the parameter 'from' in the LOCATE function ('strpos' in PostgreSQL lingo).");
        }
      }

      // Date/Time functions

      public void currentDate(final QueryWriter w) {
        w.write("CURRENT_DATE");
      }

      public void currentTime(final QueryWriter w) {
        w.write("CURRENT_TIME");
      }

      public void currentDateTime(final QueryWriter w) {
        w.write("CURRENT_TIMESTAMP");
      }

      public void date(final QueryWriter w, final Expression<Date> datetime) {
        datetime.renderTo(w);
        w.write("::DATE");
      }

      public void time(final QueryWriter w, final Expression<Date> datetime) {
        datetime.renderTo(w);
        w.write("::TIME");
      }

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
