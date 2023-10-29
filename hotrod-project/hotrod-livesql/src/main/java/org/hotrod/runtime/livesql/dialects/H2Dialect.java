package org.hotrod.runtime.livesql.dialects;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.CrossJoin;
import org.hotrod.runtime.livesql.queries.select.FullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.InnerJoin;
import org.hotrod.runtime.livesql.queries.select.Join;
import org.hotrod.runtime.livesql.queries.select.JoinLateral;
import org.hotrod.runtime.livesql.queries.select.LeftJoinLateral;
import org.hotrod.runtime.livesql.queries.select.LeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalFullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalInnerJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalLeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalRightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.RightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.UnionJoin;

public class H2Dialect extends LiveSQLDialect {

  public H2Dialect(final boolean discovered, final String productName, final String productVersion,
      final int majorVersion, final int minorVersion) {
    super(discovered, productName, productVersion, majorVersion, minorVersion);
  }

  // WITH rendering

  @Override
  public WithRenderer getWithRenderer() {
    return (c) -> "WITH";
  }

  // From rendering

  @Override
  public FromRenderer getFromRenderer() {
    return () -> "";
  }

  // Table Expression rendering

  @Override
  public TableExpressionRenderer getTableExpressionRenderer() {
    return (columns) -> " ("
        + Arrays.stream(columns).map(c -> this.canonicalToNatural(c)).collect(Collectors.joining(", ")) + ")";
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
          return "LEFT JOIN";
        } else if (join instanceof RightOuterJoin) {
          return "RIGHT JOIN";
        } else if (join instanceof FullOuterJoin) {
          return "FULL JOIN";
        } else if (join instanceof CrossJoin) {
          return "CROSS JOIN";
        } else if (join instanceof NaturalInnerJoin) {
          return "NATURAL JOIN";
        } else if (join instanceof NaturalLeftOuterJoin) {
          return "NATURAL LEFT JOIN";
        } else if (join instanceof NaturalRightOuterJoin) {
          return "NATURAL RIGHT JOIN";
        } else if (join instanceof NaturalFullOuterJoin) {
          return "NATURAL FULL JOIN";
        } else if (join instanceof JoinLateral) {
          throw new UnsupportedLiveSQLFeatureException("Lateral joins are not supported in the H2 database");
        } else if (join instanceof LeftJoinLateral) {
          throw new UnsupportedLiveSQLFeatureException("Lateral left joins are not supported in the H2 database");
        } else if (join instanceof UnionJoin) {
          throw new UnsupportedLiveSQLFeatureException("Union joins are not supported in the H2 database");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in the H2 database");
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
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in H2");
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
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in H2");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in H2");
      }

    };
  }

  // Set operation rendering

  @Override
  public SetOperatorRenderer getSetOperationRenderer() {
    return new SetOperatorRenderer() {

      @Override
      public void renderUnion(final QueryWriter w) {
        w.write("UNION");
      }

      @Override
      public void renderUnionAll(final QueryWriter w) {
        w.write("UNION ALL");
      }

      @Override
      public void renderExcept(final QueryWriter w) {
        w.write("EXCEPT");
      }

      @Override
      public void renderExceptAll(final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("The H2 database does not support the EXCEPT ALL set operator. "
            + "Nevertheless, this operator can be simulated using an anti join");
      }

      @Override
      public void renderIntersect(final QueryWriter w) {
        w.write("INTERSECT");
      }

      @Override
      public void renderIntersectAll(final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("The H2 database does not support the INTERSECT ALL set operator. "
            + "Nevertheless, this operator can be simulated using a semi join");
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

      public void trunc(final QueryWriter w, final NumberExpression x, final NumberExpression places) {
        if (places == null) {
          throw new UnsupportedLiveSQLFeatureException(
              "H2 requires the number of decimal places to be specified when using the TRUNC() function");
        }
        this.write(w, "trunc", x, places);
      }

      // String functions

      // Date/Time functions

      @Override
      public void date(final QueryWriter w, final DateTimeExpression datetime) {
        w.write("cast(");
        datetime.renderTo(w);
        w.write(" as date)");
      }

      @Override
      public void time(final QueryWriter w, final DateTimeExpression datetime) {
        w.write("cast(");
        datetime.renderTo(w);
        w.write(" as time)");
      }

      @Override
      public void dateTime(final QueryWriter w, final DateTimeExpression date, final DateTimeExpression time) {
        w.write("(");
        date.renderTo(w);
        w.write(" + ");
        time.renderTo(w);
        w.write(")");
      }

    };
  }

  // New SQL Identifier rendering

  private final String UNQUOTED_NATURAL = "[A-Za-z][A-Za-z0-9_]*";
  private final String UNQUOTED_CANONICAL = "[A-Z][A-Z0-9_]*";

  @Override
  public String naturalToCanonical(final String natural) {
    if (natural == null) {
      return null;
    }
    if (natural.matches(UNQUOTED_NATURAL)) {
      return natural.toUpperCase();
    }
    return natural;
  }

  @Override
  public String canonicalToNatural(final String canonical) {
    if (canonical == null)
      return null;
    if (canonical.matches(UNQUOTED_CANONICAL)) {
      return canonical.toLowerCase();
    } else {
      return "\"" + canonical.replace("\"", "\"\"") + "\"";
    }
  }

  @Override
  public DateTimeLiteralRenderer getDateTimeLiteralRenderer() {
    return new DateTimeLiteralRenderer() {

      @Override
      public String renderDate(final String isoDate) {
        return "DATE '" + isoDate + "'";
      }

      @Override
      public String renderTime(final String isoTime, final int precision) {
        return "TIME '" + isoTime + "'";
      }

      @Override
      public String renderTimestamp(final String isoTimestamp, final int precision) {
        return "TIMESTAMP '" + isoTimestamp + "'";
      }

      @Override
      public String renderOffsetTime(final String isoTime, final String isoOffset, final int precision) {
        return "TIME WITH TIME ZONE '" + isoTime + isoOffset + "'";
      }

      @Override
      public String renderOffsetTimestamp(final String isoTimestamp, final String isoOffset, final int precision) {
        return "TIMESTAMP WITH TIME ZONE '" + isoTimestamp + isoOffset + "'";
      }

    };

  }

  @Override
  public BooleanLiteralRenderer getBooleanLiteralRenderer() {
    return new BooleanLiteralRenderer() {

      @Override
      public void renderTrue(final LiveSQLContext context, final QueryWriter w) {
        w.write("true");
      }

      @Override
      public void renderFalse(final LiveSQLContext context, final QueryWriter w) {
        w.write("false");
      }

    };
  }

}
