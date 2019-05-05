package org.hotrod.runtime.sql.dialects;

import org.hotrod.runtime.sql.QueryWriter;

public abstract class PaginationRenderer {

  public enum PaginationType {
    TOP, BOTTOM
  };

  public abstract PaginationType getPaginationType(Integer offset, Integer limit);

  public abstract void renderTopPagination(Integer offset, Integer limit, QueryWriter w);

  public abstract void renderBottomPagination(Integer offset, Integer limit, QueryWriter w);

}
