package org.hotrod.runtime.livesql.metadata;

public abstract class Table extends TableOrView {

  public Table(final Name catalog, final Name schema, final Name name, final String type, final String alias) {
    super(catalog, schema, name, type, alias);
  }

}
