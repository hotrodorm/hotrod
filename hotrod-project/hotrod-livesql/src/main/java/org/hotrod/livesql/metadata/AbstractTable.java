package org.hotrod.livesql.metadata;

public abstract class AbstractTable<M> extends TableOrView<M> {

  public AbstractTable(final Name catalog, final Name schema, final Name name, final String type, final String alias,
      final Class<?> layoutClass, final Class<?> modelClass) {
    super(catalog, schema, name, type, alias, layoutClass, modelClass);
  }

}
