package org.hotrodorm.hotrod.utils;

public class SQLTable {
  private String name;
  private String alias;

  // @Deprecated
  public SQLTable(String name, String alias) {
    this.name = name;
    this.alias = alias;
  }

  public SQLTable(String name) {
    this.name = name;
  }

  public SQLTable(SQLTable table, String alias) {
    this.name = table.getName();
    this.alias = alias;
  }

  public String getName() {
    return name;
  }

  public String getAlias() {
    return alias;
  }

  public SQLTable usingAlias(String alias) {
    return new SQLTable(this.name, alias);
  }

  // Required for backward compatibility
  @Override
  public String toString() {
    return alias != null ? alias : name;
  }
}
