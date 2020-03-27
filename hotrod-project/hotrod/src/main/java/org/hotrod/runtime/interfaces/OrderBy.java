package org.hotrod.runtime.interfaces;

public interface OrderBy {

  String getTableName();

  String getColumnName();

  boolean isAscending();

}
