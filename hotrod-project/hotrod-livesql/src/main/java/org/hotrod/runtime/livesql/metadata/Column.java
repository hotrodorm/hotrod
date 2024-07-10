package org.hotrod.runtime.livesql.metadata;

public interface Column {

  TableOrView getObjectInstance();

  Name getCatalog();

  Name getSchema();

  Name getObjectName();

  String getReferenceName();

  String getType();

  Integer getColumnSize();

  Integer getDecimalDigits();

  String getProperty();

}
