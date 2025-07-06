package org.hotrod.livesql.metadata;

import org.hotrod.livesql.util.ToString;

public abstract class Table<M> extends TableOrView<M> {

  public Table(final Name catalog, final Name schema, final Name name, final String type, final String alias,
      final Class<?> modelClass) {
    super(catalog, schema, name, type, alias, modelClass);
  }

  public void log(ToString t) {
    t.printObject(this, this.getName() + "(" + this.getAlias() + ")");
  }

}
