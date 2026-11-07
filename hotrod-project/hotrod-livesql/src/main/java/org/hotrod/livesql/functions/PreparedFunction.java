package org.hotrod.livesql.functions;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.livesql.queries.QueryWriter;

public class PreparedFunction {

  private String name;
  private List<PreparedSegment> preparedSegments = new ArrayList<>();

  public PreparedFunction(String name) {
    this.name = name;
  }

  void add(PreparedSegment prepared) {
    this.preparedSegments.add(prepared);
  }

  public String getName() {
    return name;
  }

  public void renderTo(final QueryWriter w) {
    this.preparedSegments.forEach(s -> s.renderTo(w));
  }

}
