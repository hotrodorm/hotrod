package org.hotrod.runtime.livesql.dialects;

import java.util.List;

import org.hotrod.runtime.livesql.dialects.IdentifierRenderer.TypedIdentifierCase;
import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLStatementException;
import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.ordering.OrderingTerm;
import org.hotrod.runtime.livesql.queries.select.CrossJoin;
import org.hotrod.runtime.livesql.queries.select.FullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.InnerJoin;
import org.hotrod.runtime.livesql.queries.select.Join;
import org.hotrod.runtime.livesql.queries.select.LeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalFullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalInnerJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalLeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalRightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.RightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.UnionJoin;
import org.hotrodorm.hotrod.utils.Separator;

public class DerbyDialect extends LiveSQLDialect {

  public DerbyDialect(final boolean discovered, final String productName, final String productVersion,
      final int majorVersion, final int minorVersion) {
    super(discovered, productName, productVersion, majorVersion, minorVersion);
  }

  // Identifier rendering

  @Override
  public IdentifierRenderer getIdentifierRenderer() {
    // Identifier names are by default upper case in Apache Derby
    Quoter q = new Quoter("\"", "\"", "^[ -!#-~]$", "^[ -!#-~]$", "\"", "");
    return new IdentifierRenderer("[A-Z][A-Z0-9_]*", false, "[A-Za-z][A-Za-z0-9_]*", TypedIdentifierCase.LOWER, q);

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
        } else if (join instanceof CrossJoin) {
          return "CROSS JOIN";
        } else if (join instanceof NaturalInnerJoin) {
          return "NATURAL JOIN";
        } else if (join instanceof NaturalLeftOuterJoin) {
          return "NATURAL LEFT OUTER JOIN";
        } else if (join instanceof NaturalRightOuterJoin) {
          return "NATURAL RIGHT OUTER JOIN";
        } else if (join instanceof NaturalFullOuterJoin) {
          return "NATURAL FULL OUTER JOIN";
        } else if (join instanceof UnionJoin) {
          throw new UnsupportedLiveSQLFeatureException("Union joins are not supported in Derby database");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in Derby database");
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
          w.write("INTERSECT");
          break;
        case INTERSECT_ALL:
          throw new UnsupportedLiveSQLFeatureException(
              "Apache Derby does not support the INTERSECT ALL set operation. The database engine accepts it, but it executes it as an INTERSECT clause. "
                  + "Nevertheless, it can be simulated using a semi join");
        case EXCEPT:
          w.write("EXCEPT");
          break;
        case EXCEPT_ALL:
          throw new UnsupportedLiveSQLFeatureException(
              "Apache Derby does not support the EXCEPT ALL set operation. The database engine accepts it, but it executes it as an EXCEPT clause. "
                  + "Nevertheless, it can be simulated using an anti join");
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

      @Override
      public void groupConcat(final QueryWriter w, final boolean distinct, final StringExpression value,
          final List<OrderingTerm> ordering, final StringExpression separator) {
        throw new UnsupportedLiveSQLFeatureException("GROUP_CONCAT() is not supported in Derby database");
      }

      // Arithmetic functions

      @Override
      public void power(final QueryWriter w, final NumberExpression x, final NumberExpression exponent) {
        w.write("exp(");
        exponent.renderTo(w);
        w.write(" * ln(");
        x.renderTo(w);
        w.write("))");
      }

      @Override
      public void logarithm(final QueryWriter w, final NumberExpression x, final NumberExpression base) {
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
      public void round(final QueryWriter w, final NumberExpression x, final NumberExpression places) {
        throw new UnsupportedLiveSQLFeatureException("ROUND() is not supported in Derby database");
      }

      @Override
      public void trunc(final QueryWriter w, final NumberExpression x, final NumberExpression places) {
        throw new UnsupportedLiveSQLFeatureException("TRUNC() is not supported in Derby database");
      }

      // String functions

      @Override
      public void concat(final QueryWriter w, final List<StringExpression> strings) {
        w.write("(");
        Separator sep = new Separator(" || ");
        for (StringExpression s : strings) {
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
      public void dateTime(final QueryWriter w, final DateTimeExpression date, final DateTimeExpression time) {
        throw new UnsupportedLiveSQLFeatureException("DATETIME() is not supported in Derby database");
      }

      @Override
      public void extract(final QueryWriter w, final DateTimeExpression datetime, final DateTimeFieldExpression field) {
        throw new UnsupportedLiveSQLFeatureException("EXTRACT() is not supported in Derby database");
      }

    };
  }

}
