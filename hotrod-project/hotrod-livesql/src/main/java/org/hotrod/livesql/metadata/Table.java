package org.hotrod.livesql.metadata;

public abstract class Table<M> extends AbstractTable<M> {

  public Table(final Name catalog, final Name schema, final Name name, final String type, final String alias,
      final Class<?> layoutClass, final Class<?> modelClass) {
    super(catalog, schema, name, type, alias, layoutClass, modelClass);
  }

}
