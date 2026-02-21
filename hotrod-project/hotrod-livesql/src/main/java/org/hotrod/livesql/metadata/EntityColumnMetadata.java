package org.hotrod.livesql.metadata;

public interface EntityColumnMetadata {

  String getCanonicalName();

  String getReferenceName();

  String getType();

  Integer getColumnSize();

  Integer getDecimalDigits();

  String getProperty();

}
