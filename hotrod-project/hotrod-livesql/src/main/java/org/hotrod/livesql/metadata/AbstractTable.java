package org.hotrod.livesql.metadata;

import org.hotrod.livesql.queries.UpdateTablePhase;

public abstract class AbstractTable<M, U extends UpdateTablePhase> extends TableOrView<M, U> {

  public AbstractTable(final Name catalog, final Name schema, final Name name, final String type, final String alias,
      final Class<?> layoutClass, final Class<?> modelClass) {
    super(catalog, schema, name, type, alias, layoutClass, modelClass);
  }

}
