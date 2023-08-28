package org.hotrod.runtime.livesql.dialects;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;

@Deprecated
public abstract class SetOperationRenderer {

  public enum SetOperation {
    UNION, //
    UNION_ALL, //
    INTERSECT, //
    INTERSECT_ALL, //
    EXCEPT, //
    EXCEPT_ALL //
    ;
  }

  public abstract void render(SetOperation setOperation, QueryWriter w);

}
