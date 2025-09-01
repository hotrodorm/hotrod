package org.hotrod.runtime.livesql.dialects;

import org.hotrod.runtime.livesql.QueryWriter;

public abstract class PaginationRenderer {

  public enum PaginationType {
    TOP, BOTTOM, ENCLOSE
  };

  public abstract PaginationType getPaginationType(Integer offset, Integer limit);

  public abstract void renderTopPagination(Integer offset, Integer limit, QueryWriter w);

  public abstract void renderBottomPagination(Integer offset, Integer limit, QueryWriter w);

  public abstract void renderBeginEnclosingPagination(Integer offset, Integer limit, QueryWriter w);

  public abstract void renderEndEnclosingPagination(Integer offset, Integer limit, QueryWriter w);

}
