package org.hotrod.runtime.livesql.dialects;

import java.util.List;

import org.hotrod.runtime.livesql.dialects.IdentifierRenderer.NaturalIdentifierCase;
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

public class SybaseASEDialect extends LiveSQLDialect {

  public SybaseASEDialect(final boolean discovered, final String productName, final String productVersion,
      final int majorVersion, final int minorVersion) {
    super(discovered, productName, productVersion, majorVersion, minorVersion);
  }

  // Identifier rendering

  @Override
  public IdentifierRenderer getIdentifierRenderer() {
    // Identifier names are case insensitive in SQL Server
    Quoter q = new Quoter("[", "]", "^[ -Z\\\\-~]$", "^[ -Z\\\\-~]$", "\"", "");
    return new IdentifierRenderer("[a-zA-Z][a-zA-Z0-9_]*", false, "[A-Za-z][A-Za-z0-9_]*", NaturalIdentifierCase.LOWER,
        q);
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
          throw new UnsupportedLiveSQLFeatureException("Full outer joins are not supported in Sybase ASE database");
        } else if (join instanceof CrossJoin) {
          throw new UnsupportedLiveSQLFeatureException("Cross joins are not supported in Sybase ASE database");
        } else if (join instanceof NaturalInnerJoin) {
          throw new UnsupportedLiveSQLFeatureException("Natural joins are not supported in Sybase ASE database");
        } else if (join instanceof NaturalLeftOuterJoin) {
          throw new UnsupportedLiveSQLFeatureException("Natural joins are not supported in Sybase ASE database");
        } else if (join instanceof NaturalRightOuterJoin) {
          throw new UnsupportedLiveSQLFeatureException("Natural joins are not supported in Sybase ASE database");
        } else if (join instanceof NaturalFullOuterJoin) {
          throw new UnsupportedLiveSQLFeatureException("Natural joins are not supported in Sybase ASE database");
        } else if (join instanceof UnionJoin) {
          throw new UnsupportedLiveSQLFeatureException("Union joins are not supported in Sybase ASE database");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in Sybase ASE database");
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
          throw new UnsupportedLiveSQLFeatureException("OFFSET is not supported Sybase ASE");
        }
        return PaginationType.TOP;
      }

      @Override
      public void renderTopPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (offset == null) {
          w.write(" top " + limit);
        } else {
          throw new UnsupportedLiveSQLFeatureException("OFFSET is not supported Sybase ASE");
        }
      }

      @Override
      public void renderBottomPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination cannot be rendered at the bottom in Sybase ASE");
      }

      @Override
      public void renderBeginEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException(
            "Pagination cannot be rendered in enclosing fashion in Sybase ASE");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException(
            "Pagination cannot be rendered in enclosing fashion in Sybase ASE");
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
          throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not support the INTERSECT set operation. "
              + "Nevertheless, it can be simulated using a semi join");
        case INTERSECT_ALL:
          throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not support the INTERSECT ALL set operation. "
              + "Nevertheless, it can be simulated using a semi join");
        case EXCEPT:
          throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not support the EXCEPT set operation. "
              + "Nevertheless, it can be simulated using an anti join");
        case EXCEPT_ALL:
          throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not support the EXCEPT ALL set operation. "
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
        throw new UnsupportedLiveSQLFeatureException("GROUP_CONCAT() is not supported in Sybase ASE");
      }

      // Arithmetic functions

      @Override
      public void logarithm(final QueryWriter w, final NumberExpression x, final NumberExpression base) {
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
      public void round(final QueryWriter w, final NumberExpression x, final NumberExpression places) {
        if (places == null) {
          throw new UnsupportedLiveSQLFeatureException(
              "Sybase ASE requires the number of decimal places to be specified on the ROUND() function");
        }
        this.write(w, "round", x, places);
      }

      @Override
      public void trunc(final QueryWriter w, final NumberExpression x, final NumberExpression places) {
        throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not support the TRUNC()function");
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

      @Override
      public void length(final QueryWriter w, final StringExpression string) {
        this.write(w, "char_length", string);
      }

      @Override
      public void locate(final QueryWriter w, final StringExpression substring, final StringExpression string,
          final NumberExpression from) {
        if (from == null) {
          this.write(w, "charindex", substring, string);
        } else {
          this.write(w, "charindex", substring, string, from);
        }
      }

      @Override
      public void substr(final QueryWriter w, final StringExpression string, final NumberExpression from,
          final NumberExpression length) {
        if (length == null) {
          throw new UnsupportedLiveSQLFeatureException(
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
      public void date(final QueryWriter w, final DateTimeExpression datetime) {
        throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not suppor the DATE() function");
      }

      @Override
      public void time(final QueryWriter w, final DateTimeExpression datetime) {
        throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not suppor the TIME() function");
      }

      @Override
      public void dateTime(final QueryWriter w, final DateTimeExpression date, final DateTimeExpression time) {
        throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not suppor the DATETIME() function");
      }

      @Override
      public void extract(final QueryWriter w, final DateTimeExpression datetime, final DateTimeFieldExpression field) {
        w.write("datepart(");
        field.renderTo(w);
        w.write(", ");
        datetime.renderTo(w);
        w.write(")");
      }

    };
  }

}
