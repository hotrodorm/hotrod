package org.hotrod.runtime.livesql.queries;

import org.hotrod.runtime.livesql.expressions.TypeHandler;

public class QueryColumn {

  private String alias;
  private TypeHandler typeHandler;

  public QueryColumn(final String alias, final TypeHandler typeHandler) {
    this.alias = alias;
    this.typeHandler = typeHandler;
  }

  public String getName() {
    return this.alias;
  }

  public TypeHandler getTypeHandler() {
    return this.typeHandler;
  }

}
