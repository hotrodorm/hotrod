package org.hotrod.config;

import org.hotrod.database.DatabaseAdapter;

public interface GenerationUnit<T extends GenerationUnit<T>> {

  boolean concludeGenerationTree(T unitCache, DatabaseAdapter adapter);

}
