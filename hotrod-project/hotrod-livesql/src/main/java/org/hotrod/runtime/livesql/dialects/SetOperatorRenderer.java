package org.hotrod.runtime.livesql.dialects;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public abstract class SetOperatorRenderer {

  public enum SetOperation {
    UNION, //
    UNION_ALL, //
    INTERSECT, //
    INTERSECT_ALL, //
    EXCEPT, //
    EXCEPT_ALL //
    ;
  }

  public abstract void renderUnion(QueryWriter w);

  public abstract void renderUnionAll(QueryWriter w);

  public abstract void renderExcept(QueryWriter w);

  public abstract void renderExceptAll(QueryWriter w);

  public abstract void renderIntersect(QueryWriter w);

  public abstract void renderIntersectAll(QueryWriter w);

}
