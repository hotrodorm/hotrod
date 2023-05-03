package org.hotrod.runtime.livesql.dialects;

import java.util.List;

import org.hotrod.runtime.livesql.exceptions.InvalidLiveSQLStatementException;
import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
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

public class OracleDialect extends SQLDialect {

  public OracleDialect(final String productName, final String productVersion, final int majorVersion,
      final int minorVersion) {
    super(productName, productVersion, majorVersion, minorVersion);
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

  // -- offset 5, limit 3
  // select * from (
  // select x.*, rownum as "_HotRodRowNum_" from (
  //
  // select * from account order by id -- untouched original query
  //
  // ) x where rownum <= 5 + 3
  // ) y where "_HotRodRowNum_" > 5

  public PaginationRenderer getPaginationRenderer() {
    return new PaginationRenderer() {

      @Override
      public PaginationType getPaginationType(final Integer offset, final Integer limit) {
        return versionIsAtLeast(12, 1) ? PaginationType.BOTTOM : PaginationType.ENCLOSE;
      }

      @Override
      public void renderTopPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination cannot be rendered at the top");
      }

      @Override
      public void renderBottomPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (!versionIsAtLeast(12, 1)) {
          throw new UnsupportedLiveSQLFeatureException(
              "Pagination cannot be rendered at the bottom in this version of Oracle");
        }
        if (offset != null) {
          w.write("\nOFFSET " + offset + " ROWS");
        }
        if (limit != null) {
          w.write("\nFETCH NEXT " + limit + " ROWS ONLY");
        }
      }

      private static final String HOTROD_ROWNUM_COLUMN = "_HotRod_RowNum_";

      @Override
      public void renderBeginEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (versionIsAtLeast(12, 1)) {
          throw new UnsupportedLiveSQLFeatureException(
              "Pagination cannot be rendered in an enclosing way in this version of Oracle.");
        }
        if (offset != null) {
          w.write("SELECT * FROM (");
          w.enterLevel();
          w.write("\n");
        }
        w.write("SELECT x.*, rownum as \"" + HOTROD_ROWNUM_COLUMN + "\" FROM (");
        w.enterLevel();
        w.write("\n");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (versionIsAtLeast(12, 1)) {
          throw new UnsupportedLiveSQLFeatureException(
              "Pagination cannot be rendered in an enclosing way in this version of Oracle");
        }
        if (limit != null) {
          w.exitLevel();
          w.write("\n");
          w.write(") x WHERE rownum <= " + (offset != null ? "" + offset + " + " : "") + limit);
        } else {
          w.exitLevel();
          w.write("\n");
          w.write(") x");
        }
        if (offset != null) {
          w.exitLevel();
          w.write("\n");
          w.write(") y WHERE \"" + HOTROD_ROWNUM_COLUMN + "\" > " + offset);
        }
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
              "Oracle database does not support the INTERSECT ALL set operation. "
                  + "Nevertheless, it can be simulated using a semi join");
        case EXCEPT:
          w.write("MINUS");
          break;
        case EXCEPT_ALL:
          throw new UnsupportedLiveSQLFeatureException(
              "Oracle database does not support the EXCEPT ALL (MINUS ALL) set operation. "
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
        if (distinct) {
          throw new UnsupportedLiveSQLFeatureException(
              "Oracle does not support DISTINCT on the GROUP_CONCAT() function (listagg())");
        }
        if (ordering == null || ordering.isEmpty()) {
          throw new UnsupportedLiveSQLFeatureException("In Oracle GROUP_CONCAT() requires ordering columns");
        }
        w.write("listagg(");
        value.renderTo(w);
        if (separator != null) {
          w.write(", ");
          separator.renderTo(w);
        }
        w.write(")");
        w.write(" withing group (ORDER BY ");
        Separator sep = new Separator();
        for (OrderingTerm t : ordering) {
          w.write(sep.render());
          t.renderTo(w);
        }
        w.write(")");
      }

      // Arithmetic functions

      @Override
      public void remainder(final QueryWriter w, final NumberExpression a, final NumberExpression b) {
        w.write("mod(");
        a.renderTo(w);
        w.write(", ");
        b.renderTo(w);
        w.write(")");
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
      public void locate(final QueryWriter w, final StringExpression substring, final StringExpression string,
          final NumberExpression from) {
        if (from == null) {
          this.write(w, "instr", string, substring);
        } else {
          this.write(w, "instr", string, substring, from);
        }
      }

      // Date/Time functions

      @Override
      public void currentDate(final QueryWriter w) {
        w.write("trunc(current_date)");
      }

      @Override
      public void currentTime(final QueryWriter w) {
        w.write("to_char(current_date, 'HH24:MI:SS')");
      }

      @Override
      public void currentDateTime(final QueryWriter w) {
        w.write("current_timestamp");
      }

      @Override
      public void date(final QueryWriter w, final DateTimeExpression datetime) {
        w.write("trunc(");
        datetime.renderTo(w);
        w.write(")");
      }

      @Override
      public void time(final QueryWriter w, final DateTimeExpression datetime) {
        w.write("to_char(");
        datetime.renderTo(w);
        w.write(", 'HH24:MI:SS')");
      }

      @Override
      public void dateTime(final QueryWriter w, final DateTimeExpression date, final DateTimeExpression time) {
        w.write("to_date(to_char(");
        date.renderTo(w);
        w.write(", 'yyyymmdd') || ' ' || ");
        time.renderTo(w);
        w.write(", 'yyyymmdd hh24:mi:ss')");
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

}
