package org.hotrod.livesql.metadata;

public interface EntityColumn extends EntityColumnMetadata {

  TableOrView<?> getObjectInstance();

  Name getCatalog();

  Name getSchema();

  Name getObjectName();

}
