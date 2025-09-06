package app.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public class FK {

  private String name;
  private Table from;
  private Column fromColumn;
  private Table to;
  private Column toColumn;

  private FK(String name, Table from, Column fromColumn, Table to, Column toColumn) {
    this.name = name;
    this.from = from;
    this.fromColumn = fromColumn;
    this.to = to;
    this.toColumn = toColumn;
  }

  public static void create(int ordinal, Table[] tables, Map<String, FK> fks) {
    String name = "x_1w8_fk_" + ordinal;
    while (true) {
      Table from = tables[R.nextInt(tables.length)];
      Column fromColumn = from.getColumns()[R.nextInt(from.getColumns().length)];
      Table to = tables[R.nextInt(tables.length)];
      Column toColumn = from.getPKColumn();
      String code = from.getName() + "." + fromColumn.getName() + ":" + to.getName() + "." + toColumn.getName();
      FK fk = new FK(name, from, fromColumn, to, toColumn);
      boolean unique = !fks.containsKey(code);
      if (unique) {
        fks.put(code, fk);
        return;
      }
    }
  }

  // Build

  public void build(BufferedWriter w) throws IOException {
    w.write("\n");
    w.write("ALTER TABLE " + this.from.getName() + "\n");
    w.write("  ADD CONSTRAINT " + this.name + " FOREIGN KEY (" + this.fromColumn.getName() + ") REFERENCES "
        + this.to.getName() + " (" + this.toColumn.getName() + ");\n");
  }

  // Clean

  public void clean(BufferedWriter w) throws IOException {
    w.write("\n");
    w.write("ALTER TABLE " + this.from.getName() + "\n");
    w.write("  DROP CONSTRAINT " + this.name + ";\n");
  }

}
