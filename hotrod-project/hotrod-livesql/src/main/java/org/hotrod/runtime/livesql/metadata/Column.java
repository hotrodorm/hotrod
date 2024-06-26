package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.queries.QueryWriter;

public interface Column {

  TableOrView getObjectInstance();

  Name getCatalog();

  Name getSchema();

  Name getObjectName();

  String getName();

  String getType();

  Integer getColumnSize();

  Integer getDecimalDigits();

  String getProperty();

  void renderUnqualifiedNameTo(QueryWriter w);

}
