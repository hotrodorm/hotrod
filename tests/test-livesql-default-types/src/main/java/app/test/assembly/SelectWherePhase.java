package app.test.assembly;

import java.util.List;

public class SelectWherePhase<R> {

  private SelectQuery query;

  public SelectWherePhase(SelectQuery query) {
    this.query = query;
  }

  public List<R> execute() {
    return null;
  }

}
