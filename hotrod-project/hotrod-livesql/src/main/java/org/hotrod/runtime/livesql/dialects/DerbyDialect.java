package org.hotrod.runtime.livesql.dialects;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.exceptions.InvalidLiteralException;
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
import org.hotrodorm.hotrod.utils.Separator;

public class DerbyDialect extends LiveSQLDialect {

  public DerbyDialect(final boolean discovered, final String productName, final String productVersion,
      final int majorVersion, final int minorVersion) {
    super(discovered, productName, productVersion, majorVersion, minorVersion);
  }

  // WITH rendering

  @Override
  public WithRenderer getWithRenderer() {
    throw new UnsupportedLiveSQLFeatureException(
        "LiveSQL does not support Common Table Expressions (CTEs) in Apache Derby.");
  }

  // From rendering

  @Override
  public FromRenderer getFromRenderer() {
    return () -> "FROM sysibm.sysdummy1";
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
          return "LEFT OUTER JOIN";
        } else if (join instanceof RightOuterJoin) {
          return "RIGHT OUTER JOIN";
        } else if (join instanceof FullOuterJoin) {
          throw new UnsupportedLiveSQLFeatureException("Full outer joins are not supported in the Derby database");
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
        } else if (join instanceof JoinLateral) {
          throw new UnsupportedLiveSQLFeatureException("Lateral joins are not supported in the Derby database");
        } else if (join instanceof LeftJoinLateral) {
          throw new UnsupportedLiveSQLFeatureException("Lateral left joins are not supported in the Derby database");
        } else if (join instanceof UnionJoin) {
          throw new UnsupportedLiveSQLFeatureException("Union joins are not supported in the Derby database");
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

  // For Update rendering

  @Override
  public ForUpdateRenderer getForUpdateRenderer() {
    return new ForUpdateRenderer() {

      @Override
      public String renderAfterFromClause() {
        return null;
      }

      @Override
      public String renderAfterLimitClause() {
        return "FOR UPDATE";
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
        w.write("EXCEPT ALL");
      }

      @Override
      public void renderIntersect(final QueryWriter w) {
        w.write("INTERSECT");
      }

      @Override
      public void renderIntersectAll(final QueryWriter w) {
        w.write("INTERSECT ALL");
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
      return "\"" + canonical.replace("\"", "\"\"").replace("'", "''") + "\"";
    }
  }

  @Override
  public DateTimeLiteralRenderer getDateTimeLiteralRenderer() {
    return new DateTimeLiteralRenderer() {

      @Override
      public String renderDate(final String isoDate) {
        return "date('" + isoDate + "')";
      }

      @Override
      public String renderTime(final String isoTime, final int precision) {
        return "time('" + isoTime + "')";
      }

      @Override
      public String renderTimestamp(final String isoTimestamp, final int precision) {
        return "timestamp('" + isoTimestamp + "')";
      }

      @Override
      public String renderOffsetTime(final String isoTime, final String isoOffset, final int precision) {
        throw new InvalidLiteralException("Apache Derby does not implement the TIME WITH TIME ZONE data type.");
      }

      @Override
      public String renderOffsetTimestamp(final String isoTimestamp, final String isoOffset, final int precision) {
        throw new InvalidLiteralException("Apache Derby does not implement the TIMESTAMP WITH TIME ZONE data type.");
      }

    };

  }

  @Override
  public BooleanLiteralRenderer getBooleanLiteralRenderer() {
    return new BooleanLiteralRenderer() {

      @Override
      public void renderTrue(final QueryWriter w) {
        w.write("true");
      }

      @Override
      public void renderFalse(final QueryWriter w) {
        w.write("false");
      }

    };
  }

  @Override
  public boolean mandatoryColumnNamesInRecursiveCTEs() {
    return false;
  }

  // Update rendering

  @Override
  public UpdateRenderer getUpdateRenderer() {
    return new UpdateRenderer() {

      @Override
      public boolean removeMainTableAlias() {
        return false;
      }

    };
  }

}
