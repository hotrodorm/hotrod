package org.hotrod.livesql.metadata;

import org.hotrod.livesql.queries.keys.GeneratedKeysInsertExecutor;

public abstract class TableWithGeneratedKey<M, T> extends AbstractTable<M> {

  private GeneratedKeysInsertExecutor<T> executor;

  public TableWithGeneratedKey(final Name catalog, final Name schema, final Name name, final String type,
      final String alias, final Class<?> layoutClass, final Class<?> modelClass,
      GeneratedKeysInsertExecutor<T> executor) {
    super(catalog, schema, name, type, alias, layoutClass, modelClass);
    this.executor = executor;
  }

  public GeneratedKeysInsertExecutor<T> getExecutor() {
    return executor;
  }

}
