package org.hotrod.livesql.metadata;

import org.hotrod.livesql.queries.UpdateTablePhase;

public abstract class Table<M, U extends UpdateTablePhase> extends AbstractTable<M, U> {

  public Table(final Name catalog, final Name schema, final Name name, final String type, final String alias,
      final Class<?> layoutClass, final Class<?> modelClass) {
    super(catalog, schema, name, type, alias, layoutClass, modelClass);
  }

}
