package org.hotrod.runtime.livesql.dialects;

import org.hotrod.runtime.livesql.exceptions.InvalidLiteralException;
import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.metadata.DatabaseObject;
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

public class MariaDBDialect extends LiveSQLDialect {

  public MariaDBDialect(final boolean discovered, final String productName, final String productVersion,
      final int majorVersion, final int minorVersion) {
    super(discovered, productName, productVersion, majorVersion, minorVersion);
  }

  // WITH rendering

  @Override
  public WithRenderer getWithRenderer() {
    if (versionIsAtLeast(10, 2, 1)) {
      return (c) -> "WITH" + (c ? " RECURSIVE" : "");
    }
    throw new UnsupportedLiveSQLFeatureException(
        "LiveSQL supports Common Table Expressions (CTEs) in MariaDB 10.2.1 or newer, " + "but the current version is "
            + renderVersion());
  }

  // DISTINCT ON rendering

  @Override
  public DistinctOnRenderer getDistinctOnRenderer() {
    throw new UnsupportedLiveSQLFeatureException("The MariaDB database does not support the DISTINCT ON clause.");
  }

  // From rendering

  @Override
  public FromRenderer getFromRenderer() {
    return () -> "";
  }

  // Table Expression rendering

  @Override
  public TableExpressionRenderer getTableExpressionRenderer() {
    throw new UnsupportedLiveSQLFeatureException(
        "MariaDB does not support naming the columns of a table expression right after the table expression name.");
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
          throw new UnsupportedLiveSQLFeatureException("Full outer joins are not supported in MariaDB");
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
          throw new UnsupportedLiveSQLFeatureException("Lateral joins are not supported in the MariaDB database");
        } else if (join instanceof LeftJoinLateral) {
          throw new UnsupportedLiveSQLFeatureException("Lateral left joins are not supported in the MariaDB database");
        } else if (join instanceof UnionJoin) {
          throw new UnsupportedLiveSQLFeatureException("Union joins are not supported in the MariaDB database");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in the MariaDB database");
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
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in MariaDB");
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
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in MariaDB");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination can only be rendered at the bottom in MariaDB");
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
        if (w.getSQLDialect().versionIsAtLeast(10, 4)) {
          w.write("EXCEPT");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "LiveSQL supports the EXCEPT set operator starting in MariaDB version 10.4; " + "this version is "
                  + w.getSQLDialect().renderVersion()
                  + ". Nevertheless, this operator can be simulated using an anti join");
        }
      }

      @Override
      public void renderExceptAll(final QueryWriter w) {
        if (w.getSQLDialect().versionIsAtLeast(10, 4)) {
          w.write("EXCEPT ALL");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "LiveSQL supports the EXCEPT ALL set operator starting in MariaDB version 10.4; " + "this version is "
                  + w.getSQLDialect().renderVersion()
                  + ". Nevertheless, this operator can be simulated using an anti join");
        }
      }

      @Override
      public void renderIntersect(final QueryWriter w) {
        if (w.getSQLDialect().versionIsAtLeast(10, 4)) {
          w.write("INTERSECT");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "LiveSQL supports the INTERSECT set operator starting in MariaDB version 10.4; " + "this version is "
                  + w.getSQLDialect().renderVersion()
                  + ". Nevertheless, this operator can be simulated using a semi join");
        }
      }

      @Override
      public void renderIntersectAll(final QueryWriter w) {
        if (w.getSQLDialect().versionIsAtLeast(10, 4)) {
          w.write("INTERSECT ALL");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "LiveSQL supports the INTERSECT ALL set operator starting in MariaDB version 10.4; " + "this version is "
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

      @Override
      public void currentDateTime(final QueryWriter w) {
        w.write("now()");
      }

    };
  }

  // New SQL Identifier rendering

  // MariaDB may be case sensitive or not depending on the OS where it's running.
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
              "MariaDB's TIME literals accept a maximum precision of 6, but " + precision + " was specified.");
        }
        return "TIME '" + isoTime + "'";
      }

      @Override
      public String renderTimestamp(final String isoTimestamp, final int precision) {
        if (precision > 6) {
          throw new InvalidLiteralException(
              "MariaDB's TIMESTAMP literals accept a maximum precision of 6, but " + precision + " was specified.");
        }
        return "TIMESTAMP '" + isoTimestamp + "'";
      }

      @Override
      public String renderOffsetTime(final String isoTime, final String isoOffset, final int precision) {
        throw new InvalidLiteralException("MariaDB does not implement the TIME WITH TIME ZONE data type.");
      }

      @Override
      public String renderOffsetTimestamp(final String isoTimestamp, final String isoOffset, final int precision) {
        throw new InvalidLiteralException("MariaDB does not implement the TIMESTAMP WITH TIME ZONE data type.");
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

  public String canonicalToNatural(final DatabaseObject databaseObject) {
    if (databaseObject == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    if (databaseObject.getCatalog() != null) {
      sb.append(this.canonicalToNatural(databaseObject.getCatalog()));
      sb.append(".");
    }
    sb.append(this.canonicalToNatural(databaseObject.getName()));
    return sb.toString();
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
