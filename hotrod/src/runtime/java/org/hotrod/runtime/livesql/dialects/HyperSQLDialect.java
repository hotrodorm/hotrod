package org.hotrod.runtime.livesql.dialects;

import java.util.Date;

import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLStatementException;
import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.runtime.livesql.expressions.Expression;
import org.hotrod.runtime.livesql.queries.select.CrossJoin;
import org.hotrod.runtime.livesql.queries.select.FullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.InnerJoin;
import org.hotrod.runtime.livesql.queries.select.Join;
import org.hotrod.runtime.livesql.queries.select.LeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalJoin;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.RightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.UnionJoin;

public class HyperSQLDialect extends SQLDialect {

  public HyperSQLDialect(final String productName, final String productVersion, final int majorVersion,
      final int minorVersion) {
    super(productName, productVersion, majorVersion, minorVersion);
  }

  // Identifier rendering

  @Override
  public IdentifierRenderer getIdentifierRenderer() {
    // Identifier names are by default upper case in HyperSQL
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
          return "FULL OUTER JOIN";
        } else if (join instanceof CrossJoin) {
          return "CROSS JOIN";
        } else if (join instanceof NaturalJoin) {
          return "NATURAL JOIN";
        } else if (join instanceof UnionJoin) {
          return "UNION JOIN";
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in HyperSQL database");
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
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in HyperSQL");
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
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in HyperSQL");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in HyperSQL");
      }

    };
  }

  // Set operation rendering

  @Override
  public SetOperationRenderer getSetOperationRenderer() {
    return new SetOperationRenderer() {

      @Override
      public void render(final SetOperation setOperation, final QueryWriter w) {
        switch (setOperation) {
        case UNION:
          w.write("UNION");
          break;
        case UNION_ALL:
          w.write("UNION ALL");
          break;
        case INTERSECT:
          w.write("INTERSECT DISTINCT");
          break;
        case INTERSECT_ALL:
          w.write("INTERSECT");
          break;
        case EXCEPT:
          w.write("EXCEPT DISTINCT");
          break;
        case EXCEPT_ALL:
          w.write("EXCEPT");
          break;
        default:
          throw new InvalidLiveSQLStatementException("Invalid set operation '" + setOperation + "'.");
        }
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
          throw new UnsupportedLiveSQLFeatureException(
              "HyperSQL requires the number of decimal places to be specified when using the ROUND() function");
        }
        this.write(w, "round", x, places);
      }

      // String functions

      @Override
      public void substr(final QueryWriter w, final Expression<String> string, final Expression<Number> from,
          final Expression<Number> length) {
        if (length == null) {
          throw new UnsupportedLiveSQLFeatureException(
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
