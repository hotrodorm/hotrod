package app.model;

import java.io.BufferedWriter;
import java.io.IOException;

public class View {

  private String name;
  private Table from;

  private View(String name, Table from) {
    this.name = name;
    this.from = from;
  }

  public static View create(int ordinal, Table[] tables) {
    String name = "v" + ordinal;
    Table from = tables[R.nextInt(tables.length)];
    return new View(name, from);
  }

  // Build

  public void build(BufferedWriter w) throws IOException {
    w.write("\n");
    w.write("CREATE VIEW " + this.name + "\n");
    w.write("  AS SELECT * FROM " + this.from.getName() + ";\n");
  }

  // Clean

  public void clean(BufferedWriter w) throws IOException {
    w.write("\n");
    w.write("DROP VIEW " + this.name + ";\n");
  }

}
