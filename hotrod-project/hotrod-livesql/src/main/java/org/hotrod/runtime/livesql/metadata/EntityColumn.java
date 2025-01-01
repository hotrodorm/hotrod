package org.hotrod.runtime.livesql.metadata;

public interface EntityColumn {

  TableOrView getObjectInstance();

  Name getCatalog();

  Name getSchema();

  Name getObjectName();

  String getCanonicalName();
  
  String getReferenceName();

  String getType();

  Integer getColumnSize();

  Integer getDecimalDigits();

  String getProperty();

}
