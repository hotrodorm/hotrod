package org.hotrod.livesql.metadata;

public abstract class Table<M> extends TableOrView {

  public Table(final Name catalog, final Name schema, final Name name, final String type, final String alias) {
    super(catalog, schema, name, type, alias);
  }

}
