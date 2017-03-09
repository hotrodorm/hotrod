package org.hotrod.runtime.util;

public class SQLTable {
  private String name;
  private String alias;

  public SQLTable(String name, String alias) {
    this.name = name;
    this.alias = alias;
  }

  public SQLTable(String name) {
    this(name, null);
  }

  public String getName() {
    return name;
  }

  public String getAlias() {
    return alias;
  }

  //Required for backward compatibility
  @Override
  public String toString() {
    return name;
  }
}
