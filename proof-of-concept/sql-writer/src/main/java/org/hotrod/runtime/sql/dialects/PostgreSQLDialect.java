package org.hotrod.runtime.sql.dialects;

import java.util.List;

import org.hotrod.runtime.sql.FullOuterJoin;
import org.hotrod.runtime.sql.InnerJoin;
import org.hotrod.runtime.sql.Join;
import org.hotrod.runtime.sql.LeftOuterJoin;
import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.RightOuterJoin;
import org.hotrod.runtime.sql.exceptions.UnsupportedFeatureException;
import org.hotrod.runtime.sql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.sql.expressions.strings.StringExpression;

import sql.util.Separator;

public class PostgreSQLDialect extends SQLDialect {

  public PostgreSQLDialect() {
    super("[a-z][a-z0-9_]*", "\"", "\"");
  }

  // Join keywords

  @Override
  public String renderJoinKeywords(final Join join) throws UnsupportedFeatureException {
    if (join instanceof InnerJoin) {
      return "join";
    } else if (join instanceof LeftOuterJoin) {
      return "left outer join";
    } else if (join instanceof RightOuterJoin) {
      return "right outer join";
    } else if (join instanceof FullOuterJoin) {
      return "full outer join";
    } else {
      return "cross join";
    }
  }

  // Pagination

  @Override
  public PaginationType getPaginationType(final Integer offset, final Integer limit) {
    return offset != null || limit != null ? PaginationType.BOTTOM : null;
  }

  @Override
  public void renderTopPagination(final Integer offset, final Integer limit, final QueryWriter w) {
    throw new UnsupportedFeatureException("Pagination can only be rendered at the bottom in PostgreSQL");
  }

  @Override
  public void renderBottomPagination(final Integer offset, final Integer limit, final QueryWriter w) {
    if (limit != null) {
      if (offset != null) {
        w.write("limit " + limit + " offset " + offset);
      } else {
        w.write("limit " + limit);
      }
    } else {
      w.write("offset " + offset);
    }
  }

  @Override
  public FunctionTranslator getFunctionTranslator() {
    return new FunctionTranslator() {

      @Override
      public String getLog() {
        return "log";
      }

      @Override
      public String getPow() {
        return "power";
      }

      @Override
      public String getRound() {
        return "round";
      }

      @Override
      public String getSignum() {
        return "sign";
      }

      @Override
      public String getCoalesce() {
        return "coalesce";
      }

    };
  }

  @Override
  public FunctionRenderer getFunctionRenderer() {
    return new FunctionRenderer() {

      @Override
      public void concat(QueryWriter w, List<StringExpression> strings) {

        Separator sep = new Separator(" || ");
        for (StringExpression s : strings) {
          w.write(sep.render());
          // super.renderInner(s, w);
        }
        w.write(")");

      }

      @Override
      public void length(QueryWriter w, StringExpression string) {
        // TODO Auto-generated method stub

      }

      @Override
      public void lower(QueryWriter w, StringExpression string) {
        // TODO Auto-generated method stub

      }

      @Override
      public void upper(QueryWriter w, StringExpression string) {
        // TODO Auto-generated method stub

      }

      @Override
      public void trim(QueryWriter w, StringExpression string) {
        // TODO Auto-generated method stub

      }

      @Override
      public void position(QueryWriter w, StringExpression substring, StringExpression string, NumberExpression from) {
        // TODO Auto-generated method stub

      }

      @Override
      public void substring(QueryWriter w, StringExpression substring, NumberExpression from, NumberExpression length) {
        // TODO Auto-generated method stub

      }

    };
  }

}
