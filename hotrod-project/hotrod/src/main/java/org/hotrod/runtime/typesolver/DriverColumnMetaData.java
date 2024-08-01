package org.hotrod.runtime.typesolver;

public interface DriverColumnMetaData {

  String getCatalog(); // Only for database object columns

  String getSchema(); // Only for database object columns

  String getTable(); // Only for database object columns

  String getName();

  String getLabel(); // Only for computed columns. May be different from name due to aliasing

  String getTypeName(); // Internal type name as reported by the driver

  Integer getDataType(); // Internal type id as reported by the driver

  String getDriverDefaultClassName(); // Only for computed columns. Driver would read using this this type by default

  Integer getDisplaySize(); // Only for computed columns. Size of char-like columns

  Integer getPrecision(); // Total number of digits

  Integer getScale(); // Number of decimal places

}
