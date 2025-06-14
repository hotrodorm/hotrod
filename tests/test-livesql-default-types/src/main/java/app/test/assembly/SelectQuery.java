package app.test.assembly;

import java.util.ArrayList;
import java.util.List;

import app.test.base.Table;

public class SelectQuery {

  private List<Table> tables = new ArrayList<>();

  public void add(Table t) {
    this.tables.add(t);
  }

}
