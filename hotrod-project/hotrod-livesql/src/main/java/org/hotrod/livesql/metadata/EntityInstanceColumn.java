package org.hotrod.livesql.metadata;

public interface EntityInstanceColumn extends EntityColumn {

  TableOrView<?> getObjectInstance();

  Name getCatalog();

  Name getSchema();

  Name getObjectName();

}
