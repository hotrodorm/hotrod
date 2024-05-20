package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.ReferenceableExpression;

public interface Column extends ReferenceableExpression {

  TableOrView getObjectInstance();

  Name getCatalog();

  Name getSchema();

  Name getObjectName();

  String getType();

  Integer getColumnSize();

  Integer getDecimalDigits();

  String getProperty();

  void renderUnqualifiedNameTo(QueryWriter w);

}
