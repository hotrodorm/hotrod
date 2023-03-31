package org.hotrod.runtime.livesql.metadata;

import org.hotrod.runtime.livesql.queries.select.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.ReferenceableExpression;

public interface Column extends ReferenceableExpression {

  String getName();

  TableOrView getObjectInstance();

  String getCatalog();

  String getSchema();

  String getObjectName();

  String getType();

  Integer getColumnSize();

  Integer getDecimalDigits();

//  boolean isNullable();
//
//  String getDefaultValue();
//
//  boolean isLob();

  String getProperty();

  void renderSimpleNameTo(QueryWriter w);

}
