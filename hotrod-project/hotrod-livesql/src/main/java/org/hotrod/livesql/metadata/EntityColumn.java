package org.hotrod.livesql.metadata;

public interface EntityColumn {

  TableOrView<?> getObjectInstance();

  Name getCatalog();

  Name getSchema();

  Name getObjectName();

  Name getName();

  String getProperty();

  String getType();

  Integer getColumnSize();

  Integer getDecimalDigits();

}
