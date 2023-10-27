package org.hotrod.runtime.livesql.dialects;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.exceptions.InvalidLiteralException;
import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
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

public class MySQLDialect extends LiveSQLDialect {

  public MySQLDialect(final boolean discovered, final String productName, final String productVersion,
      final int majorVersion, final int minorVersion) {
    super(discovered, productName, productVersion, majorVersion, minorVersion);
  }

  // WITH rendering

  @Override
  public WithRenderer getWithRenderer() {
    if (versionIsAtLeast(8, 0, 1)) {
      return (c) -> "WITH" + (c ? " RECURSIVE" : "");
    }
    throw new UnsupportedLiveSQLFeatureException(
        "LiveSQL supports Common Table Expressions (CTEs) starting in MySQL 8.0.1 or newer, "
            + "but the current version is " + renderVersion());
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
          throw new UnsupportedLiveSQLFeatureException("Full outer joins are not supported in MySQL");
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
          if (versionIsAtLeast(8, 0, 14)) {
            return "JOIN LATERAL";
          }
          throw new UnsupportedLiveSQLFeatureException(
              "LiveSQL supports lateral joins in the MySQL database starting in version 8.0.14, "
                  + "but the current version is " + renderVersion());
        } else if (join instanceof LeftJoinLateral) {
          if (versionIsAtLeast(8, 0, 14)) {
            return "LEFT JOIN LATERAL";
          }
          throw new UnsupportedLiveSQLFeatureException(
              "LiveSQL supports lateral joins in the PostgreSQL database starting in version 8.0.14, "
                  + "but the current version is " + renderVersion());
        } else if (join instanceof UnionJoin) {
          throw new UnsupportedLiveSQLFeatureException("Union joins are not supported in MariaDB database");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in MariaDB database");
        }
      }

      @Override
      public String renderOptionalOnPredicate(final Join join) throws UnsupportedLiveSQLFeatureException {
        if (join instanceof InnerJoin) {
          return "";
        } else if (join instanceof LeftOuterJoin) {
          return "";
        } else if (join instanceof RightOuterJoin) {
          return "";
        } else if (join instanceof FullOuterJoin) {
          return "";
        } else if (join instanceof CrossJoin) {
          return "";
        } else if (join instanceof NaturalInnerJoin) {
          return "";
        } else if (join instanceof NaturalLeftOuterJoin) {
          return "";
        } else if (join instanceof NaturalRightOuterJoin) {
          return "";
        } else if (join instanceof NaturalFullOuterJoin) {
          return "";
        } else if (join instanceof JoinLateral) {
          return " ON true";
        } else if (join instanceof LeftJoinLateral) {
          return " ON true";
        } else if (join instanceof UnionJoin) {
          throw new UnsupportedLiveSQLFeatureException("Union joins are not supported in Oracle database");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in Oracle database");
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
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in MySQL");
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
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in MySQL");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in MySQL");
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
        if (w.getSQLDialect().versionIsAtLeast(8, 0, 31)) {
          w.write("EXCEPT");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "MySQL does not support the EXCEPT set operator before version 8.0.31; " + "this version is "
                  + w.getSQLDialect().renderVersion()
                  + ". Nevertheless, this operator can be simulated using an anti join");
        }
      }

      @Override
      public void renderExceptAll(final QueryWriter w) {
        if (w.getSQLDialect().versionIsAtLeast(8, 0, 31)) {
          w.write("EXCEPT ALL");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "MySQL does not support the EXCEPT ALL set operator before version 8.0.31; " + "this version is "
                  + w.getSQLDialect().renderVersion()
                  + ". Nevertheless, this operator can be simulated using an anti join");
        }
      }

      @Override
      public void renderIntersect(final QueryWriter w) {
        if (w.getSQLDialect().versionIsAtLeast(8, 0, 31)) {
          w.write("INTERSECT");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "MySQL does not support the INTERSECT set operator before version 8.0.31; " + "this version is "
                  + w.getSQLDialect().renderVersion()
                  + ". Nevertheless, this operator can be simulated using a semi join");
        }
      }

      @Override
      public void renderIntersectAll(final QueryWriter w) {
        if (w.getSQLDialect().versionIsAtLeast(8, 0, 31)) {
          w.write("INTERSECT ALL");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "MySQL does not support the INTERSECT ALL set operator before version 8.0.31; " + "this version is "
                  + w.getSQLDialect().renderVersion()
                  + ". Nevertheless, this operator can be simulated using a semi join");
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
      public void trunc(final QueryWriter w, final NumberExpression x, final NumberExpression places) {
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

  // New SQL Identifier rendering

  // MySQL may be case sensitive or not depending on the OS where it's running.
  // Assume it's case sensitive and quote always.

  @Override
  public String naturalToCanonical(final String natural) {
    return natural;
  }

  @Override
  public String canonicalToNatural(final String canonical) {
    if (canonical == null)
      return null;
    return "`" + canonical.replace("`", "``") + "`";
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
        if (precision > 6) {
          throw new InvalidLiteralException(
              "MySQL's TIME literals accept a maximum precision of 6, but " + precision + " was specified.");
        }
        return "TIME '" + isoTime + "'";
      }

      @Override
      public String renderTimestamp(final String isoTimestamp, final int precision) {
        if (precision > 6) {
          throw new InvalidLiteralException(
              "MySQL's TIMESTAMP literals accept a maximum precision of 6, but " + precision + " was specified.");
        }
        return "TIMESTAMP '" + isoTimestamp + "'";
      }

      @Override
      public String renderOffsetTime(final String isoTime, final String isoOffset, final int precision) {
        throw new InvalidLiteralException("MySQL does not implement the TIME WITH TIME ZONE data type.");
      }

      @Override
      public String renderOffsetTimestamp(final String isoTimestamp, final String isoOffset, final int precision) {
        throw new InvalidLiteralException("MySQL does not implement the TIMESTAMP WITH TIME ZONE data type.");
      }

    };
  }

}
