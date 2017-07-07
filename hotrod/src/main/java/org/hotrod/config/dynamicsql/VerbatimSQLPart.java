package org.hotrod.config.dynamicsql;

public class VerbatimSQLPart extends DynamicSQLPart {

  private String text;

  public VerbatimSQLPart(final String text) {
    super("not-a-tag-but-sql-content");
    this.text = text;
  }

  @Override
  public String render() {
    return this.text;
  }

}
