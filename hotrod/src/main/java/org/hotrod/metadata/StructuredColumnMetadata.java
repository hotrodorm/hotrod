package org.hotrod.metadata;

public class StructuredColumnMetadata extends ColumnMetadata {

  private String alias;

  public StructuredColumnMetadata(final ColumnMetadata cm, final String alias) {
    super(cm);
    this.alias = alias;
  }

  public String getAlias() {
    return alias;
  }

}
