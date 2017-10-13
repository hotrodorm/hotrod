package org.hotrod.metadata;

public class AllottedColumnMetadata extends ColumnMetadata {

  private String alias;

  public AllottedColumnMetadata(final ColumnMetadata cm, final String alias) {
    super(cm);
    this.alias = alias;
  }

  public String getAlias() {
    return alias;
  }

}
