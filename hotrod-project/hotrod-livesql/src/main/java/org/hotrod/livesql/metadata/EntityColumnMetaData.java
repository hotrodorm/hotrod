package org.hotrod.livesql.metadata;

import org.hotrod.livesql.queries.QueryWriter;
import org.hotrod.livesql.queries.typesolver.TypeHandler;

public abstract class EntityColumnMetaData {

  private Name name;

  protected EntityColumnMetaData(Name name) {
    this.name = name;
  }

  public final void renderTo(QueryWriter w) {
    if (this.name.isQuoted()) {
      w.write(w.getSQLDialect().canonicalToNatural(this.name));
    } else {
      w.write(w.getSQLDialect().canonicalToNatural(w.getSQLDialect().naturalToCanonical(this.name.getName())));
    }
  }

  public final Name getName() {
    return this.name;
  }

  public abstract String getReferenceName();

  public abstract String getType();

  public abstract Integer getColumnSize();

  public abstract Integer getDecimalDigits();

  public abstract String getProperty();

  public abstract TypeHandler<?, ?> getTypeHandler();

}
