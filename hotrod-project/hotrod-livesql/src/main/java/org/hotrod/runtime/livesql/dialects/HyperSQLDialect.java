package org.hotrod.runtime.livesql.dialects;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
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

public class HyperSQLDialect extends LiveSQLDialect {

  public HyperSQLDialect(final boolean discovered, final String productName, final String productVersion,
      final int majorVersion, final int minorVersion) {
    super(discovered, productName, productVersion, majorVersion, minorVersion);
  }

  // WITH rendering

  @Override
  public WithRenderer getWithRenderer() {
    return (c) -> "WITH" + (c ? " RECURSIVE" : "");
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
          return "LEFT OUTER JOIN";
        } else if (join instanceof RightOuterJoin) {
          return "RIGHT OUTER JOIN";
        } else if (join instanceof FullOuterJoin) {
          return "FULL OUTER JOIN";
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
          throw new UnsupportedLiveSQLFeatureException("Lateral joins are not supported in the HyperSQL database");
        } else if (join instanceof LeftJoinLateral) {
          throw new UnsupportedLiveSQLFeatureException("Lateral left joins are not supported in the HyperSQL database");
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

  // For Update rendering

  @Override
  public ForUpdateRenderer getForUpdateRenderer() {
    throw new UnsupportedLiveSQLFeatureException("HyperSQL does not support locking rows with the FOR UPDATE clause");
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

      @Override
      public void round(final QueryWriter w, final NumberExpression x, final NumberExpression places) {
        if (places == null) {
          throw new UnsupportedLiveSQLFeatureException(
              "HyperSQL requires the number of decimal places to be specified when using the ROUND() function");
        }
        this.write(w, "round", x, places);
      }

      // String functions

      @Override
      public void substr(final QueryWriter w, final StringExpression string, final NumberExpression from,
          final NumberExpression length) {
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
        return "TIME '" + isoTime + isoOffset + "'";
      }

      @Override
      public String renderOffsetTimestamp(final String isoTimestamp, final String isoOffset, final int precision) {
        return "TIMESTAMP '" + isoTimestamp + isoOffset + "'";
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
    return true;
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
