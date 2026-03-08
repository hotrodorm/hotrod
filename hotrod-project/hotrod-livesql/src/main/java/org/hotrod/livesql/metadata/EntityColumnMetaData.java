package org.hotrod.livesql.metadata;

import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public abstract class EntityColumnMetaData {

  private String canonicalName;

  protected EntityColumnMetaData(String canonicalName) {
    this.canonicalName = canonicalName;
  }

  public final void renderTo(QueryWriter w) {
    w.write(w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.canonicalName)));
  }

  public final String getCanonicalName() {
    return canonicalName;
  }

  public abstract String getReferenceName();

  public abstract String getType();

  public abstract Integer getColumnSize();

  public abstract Integer getDecimalDigits();

  public abstract String getProperty();

  public abstract TypeHandler<?, ?> getTypeHandler();

}
