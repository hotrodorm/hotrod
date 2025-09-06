package app.model;

import java.io.BufferedWriter;
import java.io.IOException;

public class Table {

  private String name;
  private Column[] columns;

  private Table(String name, Column[] columns) {
    this.name = name;
    this.columns = columns;
  }

  public static Table create(int ordinal, int columnsPerTable) {
    String name = "t" + ordinal;
    Column[] columns = new Column[columnsPerTable];
    for (int i = 0; i < columnsPerTable; i++) {
      String colName = "col" + i;
      String type = "INT";
      Column col = new Column(colName, type);
      columns[i] = col;
    }
    return new Table(name, columns);
  }

  // Build

  public void build(BufferedWriter w) throws IOException {
    w.write("\n");
    w.write("CREATE TABLE " + this.name + " (\n");
    for (int i = 0; i < this.columns.length; i++) {
      w.write("  ");
      this.columns[i].build(w);
      if (i == 0) {
        w.write(" PRIMARY KEY NOT NULL");
      }
      if (i < this.columns.length - 1) {
        w.write(",");
      }
      w.write("\n");
    }
    w.write(");\n");
  }

  // Clean

  public void clean(BufferedWriter w) throws IOException {
    w.write("\n");
    w.write("DROP TABLE " + this.name + ";\n");
  }

  // Getters

  public Column getPKColumn() {
    return columns[0];
  }

  public String getName() {
    return name;
  }

  public Column[] getColumns() {
    return columns;
  }

}
