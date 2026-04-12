package org.hotrod.livesql.metadata;

public interface EntityColumn {

  TableOrView<?> getObjectInstance();

  Name getCatalog();

  Name getSchema();

  Name getObjectName();

  @Deprecated
  String getCanonicalName();

  String getProperty();

  String getType();

  Integer getColumnSize();

  Integer getDecimalDigits();

}
