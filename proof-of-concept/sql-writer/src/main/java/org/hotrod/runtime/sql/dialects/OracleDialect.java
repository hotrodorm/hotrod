package org.hotrod.runtime.sql.dialects;

import java.util.Date;
import java.util.List;

import org.hotrod.runtime.sql.FullOuterJoin;
import org.hotrod.runtime.sql.InnerJoin;
import org.hotrod.runtime.sql.Join;
import org.hotrod.runtime.sql.LeftOuterJoin;
import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.RightOuterJoin;
import org.hotrod.runtime.sql.exceptions.UnsupportedFeatureException;
import org.hotrod.runtime.sql.expressions.Expression;
import org.hotrod.runtime.sql.ordering.OrderingTerm;

import sql.util.Separator;

public class OracleDialect extends SQLDialect {

  public OracleDialect(final String productName, final String productVersion, final int majorVersion,
      final int minorVersion) {
    super(productName, productVersion, majorVersion, minorVersion);
  }

  // Identifier rendering

  @Override
  public IdentifierRenderer getIdentifierRenderer() {
    // Identifier names are by default upper case in Oracle
    return new IdentifierRenderer("[A-Z][A-Z0-9_]*", "\"", "\"", false);
  }

  // Join rendering

  @Override
  public JoinRenderer getJoinRenderer() {
    return new JoinRenderer() {

      @Override
      public String renderJoinKeywords(final Join join) throws UnsupportedFeatureException {
        if (join instanceof InnerJoin) {
          return "JOIN";
        } else if (join instanceof LeftOuterJoin) {
          return "LEFT OUTER JOIN";
        } else if (join instanceof RightOuterJoin) {
          return "RIGHT OUTER JOIN";
        } else if (join instanceof FullOuterJoin) {
          return "FULL OUTER JOIN";
        } else {
          return "CROSS JOIN";
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
        throw new UnsupportedFeatureException("Pagination cannot be rendered at the top.");
      }

      @Override
      public void renderBottomPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (!versionIsAtLeast(12, 1)) {
          throw new UnsupportedFeatureException(
              "Pagination cannot be rendered at the bottom in this version of Oracle.");
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
          throw new UnsupportedFeatureException(
              "Pagination cannot be rendered in an enclosing way in this version of Oracle.");
        }
        if (offset != null) {
          w.write("SELECT * FROM (");
        }
        w.write("SELECT x.*, rownum as \"" + HOTROD_ROWNUM_COLUMN + "\" FROM (");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (versionIsAtLeast(12, 1)) {
          throw new UnsupportedFeatureException(
              "Pagination cannot be rendered in an enclosing way in this version of Oracle.");
        }
        if (limit != null) {
          w.write(") x WHERE rownum <= " + (offset != null ? "" + offset + " + " : "") + limit);
        } else {
          w.write(") x");
        }
        if (offset != null) {
          w.write(") y WHERE \"" + HOTROD_ROWNUM_COLUMN + "\" > " + offset);
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
      public void groupConcat(final QueryWriter w, final boolean distinct, final Expression<String> value,
          final List<OrderingTerm> ordering, final Expression<String> separator) {
        if (distinct) {
          throw new UnsupportedFeatureException(
              "Oracle does not support DISTINCT on the GROUP_CONCAT() function (listagg()).");
        }
        if (ordering == null || ordering.isEmpty()) {
          throw new UnsupportedFeatureException("In Oracle GROUP_CONCAT() requires ordering columns.");
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

      // String functions

      @Override
      public void concat(final QueryWriter w, final List<Expression<String>> strings) {
        w.write("(");
        Separator sep = new Separator(" || ");
        for (Expression<String> s : strings) {
          w.write(sep.render());
          s.renderTo(w);
        }
        w.write(")");
      }

      @Override
      public void locate(final QueryWriter w, final Expression<String> substring, final Expression<String> string,
          final Expression<Number> from) {
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
      public void date(final QueryWriter w, final Expression<Date> datetime) {
        w.write("trunc(");
        datetime.renderTo(w);
        w.write(")");
      }

      @Override
      public void time(final QueryWriter w, final Expression<Date> datetime) {
        w.write("to_char(");
        datetime.renderTo(w);
        w.write(", 'HH24:MI:SS')");
      }

      @Override
      public void dateTime(final QueryWriter w, final Expression<Date> date, final Expression<Date> time) {
        w.write("to_date(to_char(");
        date.renderTo(w);
        w.write(", 'yyyymmdd') || ' ' || ");
        time.renderTo(w);
        w.write(", 'yyyymmdd hh24:mi:ss')");
      }

    };
  }

}
