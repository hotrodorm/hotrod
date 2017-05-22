package org.hotrod.runtime.util;

public class SQLField {
  private SQLTable table;
  private String fieldName;

  public SQLField(SQLTable table, String fieldName) {
    this.table = table;
    this.fieldName = fieldName;
  }

  // Asume convención antigua del nombre de campo como <tablename>.<fieldname>
  @Deprecated
  public SQLField(String fieldName) {
    // extract tableName
    int p = fieldName.indexOf(".");
    if (p < 0) {
      // no contiene nombre de tabla
      this.table = null;
      this.fieldName = fieldName;
    } else {
      this.table = new SQLTable(fieldName.substring(0, p));
      this.fieldName = fieldName.substring(p + 1);
    }
  }

  // For backward compatibility
  @Deprecated
  public SQLField(SQLField field) {
    this.table = field.getTable();
    this.fieldName = field.getFieldName();
  }

  // For backward compatibility
  @Deprecated
  public SQLField(SQLTable table, SQLField field) {
    this.table = table;
    this.fieldName = field.getFieldName();
  }

  public String render() {
    if (table != null) {
      return (this.table.getAlias() != null ? this.table.getAlias() : this.table.getName()) + "." + this.fieldName;
    } else {
      // For backward compatibility
      return this.fieldName;
    }
  }

  public SQLTable getTable() {
    return table;
  }

  public String getFieldName() {
    return fieldName;
  }

  public SQLField using(SQLTable alias) {
    if (alias.getName().equals(table.getName())) {
      return new SQLField(alias, this.fieldName);
    }
    throw new RuntimeException("Provided table alias does not correspond to field's table");
  }

  // Required for backward compatibility
  @Override
  public String toString() {
    return this.render();
  }

  public static void main(String[] args) {
    SQLField f = new SQLField("a.b");
    SQLField f2 = new SQLField(new SQLTable("t"), "b");
    SQLField f3 = new SQLField(new SQLTable("t", "alias"), "b");

    System.out.println(f);
    System.out.println(f2);
    System.out.println(f3);
    System.out.println(f2.using(new SQLTable(new SQLTable("t", "alias"), "alias2")));
  }
}
