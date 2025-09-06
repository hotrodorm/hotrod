package app.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class Model {

  private Table[] tables;
  private View[] views;
  private LinkedHashMap<String, FK> fks;

  private int numberOfTables;
  private int numberOfViews;
  private double fksPerTable;
  private int columnsPerTable;

  private int totalFKs;

  private Model(int numberOfTables, int numberOfViews, double fksPerTable, int columnsPerTable) {
    this.numberOfTables = numberOfTables;
    this.numberOfViews = numberOfViews;
    this.fksPerTable = fksPerTable;
    this.totalFKs = (int) (this.numberOfTables * this.fksPerTable);
    this.columnsPerTable = columnsPerTable;

    this.tables = new Table[this.numberOfTables];
    this.views = new View[this.numberOfViews];
    this.fks = new LinkedHashMap<>();
  }

  public static Model of(int numberOfTables, int numberOfViews, double fksPerTable, int columnsPerTable) {
    return new Model(numberOfTables, numberOfViews, fksPerTable, columnsPerTable);
  }

  public void create() {
    for (int i = 0; i < this.numberOfTables; i++) {
      this.tables[i] = Table.create(i, columnsPerTable);
    }
    for (int i = 0; i < this.numberOfViews; i++) {
      this.views[i] = View.create(i, this.tables);
    }
    for (int i = 0; i < this.totalFKs; i++) {
      FK.create(i, this.tables, this.fks);
    }
  }

  public void build(File buildFile) throws IOException {
    try (BufferedWriter w = new BufferedWriter(new FileWriter(buildFile))) {
      w.write("-- Generated: " + LocalDateTime.now() + "\n");
      w.write("--   * Number of Tables: " + this.numberOfTables + "\n");
      w.write("--   * Number of Views: " + this.numberOfViews + "\n");
      w.write("--   * FKs per Table: " + this.fksPerTable + "\n");
      w.write("--   * Columns per Table: " + this.columnsPerTable + "\n");
      w.write("\n");

      w.write("-- TABLES (" + this.tables.length + ")\n");
      for (Table t : this.tables) {
        t.build(w);
      }
      w.write("\n-- VIEWS (" + this.views.length + ")\n");
      for (View v : this.views) {
        v.build(w);
      }
      w.write("\n-- FKS (" + this.fks.size() + ")\n");
      for (FK fk : this.fks.values()) {
        fk.build(w);
      }

    }
  }

  public void clean(File cleanFile) throws IOException {
    try (BufferedWriter w = new BufferedWriter(new FileWriter(cleanFile))) {
      w.write("-- Generated: " + LocalDateTime.now() + "\n");
      w.write("--   * Number of Tables: " + this.numberOfTables + "\n");
      w.write("--   * Number of Views: " + this.numberOfViews + "\n");
      w.write("--   * FKs per Table: " + this.fksPerTable + "\n");
      w.write("--   * Columns per Table: " + this.columnsPerTable + "\n");
      w.write("\n");

      w.write("-- FKS (" + this.fks.size() + ")\n");
      for (FK fk : this.fks.values()) {
        fk.clean(w);
      }

      w.write("\n-- VIEWS (" + this.views.length + ")\n");
      for (View v : this.views) {
        v.clean(w);
      }

      w.write("\n-- TABLES (" + this.tables.length + ")\n");
      for (Table t : this.tables) {
        t.clean(w);
      }

    }
  }

}
