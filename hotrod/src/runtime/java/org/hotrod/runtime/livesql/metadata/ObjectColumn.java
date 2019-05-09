package org.hotrod.runtime.livesql.metadata;

public class ObjectColumn extends Column<Object> {

  public ObjectColumn(final TableOrView objectIntance, final String name, final String property) {
    super(objectIntance, name, property);
  }

}
