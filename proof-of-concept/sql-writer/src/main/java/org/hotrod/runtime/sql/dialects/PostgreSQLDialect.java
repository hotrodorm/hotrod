package org.hotrod.runtime.sql.dialects;

import org.hotrod.runtime.sql.FullOuterJoin;
import org.hotrod.runtime.sql.InnerJoin;
import org.hotrod.runtime.sql.Join;
import org.hotrod.runtime.sql.LeftOuterJoin;
import org.hotrod.runtime.sql.QueryWriter;
import org.hotrod.runtime.sql.RightOuterJoin;
import org.hotrod.runtime.sql.exceptions.UnsupportedFeatureException;

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

}
