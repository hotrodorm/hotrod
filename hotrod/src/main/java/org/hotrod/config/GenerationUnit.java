package org.hotrod.config;

import org.hotrod.database.DatabaseAdapter;

public interface GenerationUnit<T extends GenerationUnit<T>> {

  boolean concludeGeneration(T cache, DatabaseAdapter adapter);

}
