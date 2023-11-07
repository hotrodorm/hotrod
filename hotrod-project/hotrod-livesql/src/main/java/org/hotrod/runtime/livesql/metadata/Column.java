package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.ReferenceableExpression;

public interface Column extends ReferenceableExpression {

  TableOrView getObjectInstance();

  String getCatalog();

  String getSchema();

  String getObjectName();

  String getType();

  Integer getColumnSize();

  Integer getDecimalDigits();

  String getProperty();

  void renderUnqualifiedNameTo(QueryWriter w);

}
