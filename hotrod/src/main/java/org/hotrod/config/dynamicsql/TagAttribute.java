package org.hotrod.config.dynamicsql;

public class TagAttribute {

  private String name;
  private String value;

  public TagAttribute(final String name, final String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return this.name;
  }

  public String getValue() {
    return this.value;
  }

}
