package org.hotrod.livesql.metadata;

public interface EntityColumn {

  String getCanonicalName();

  String getReferenceName();

  String getType();

  Integer getColumnSize();

  Integer getDecimalDigits();

  String getProperty();

}
