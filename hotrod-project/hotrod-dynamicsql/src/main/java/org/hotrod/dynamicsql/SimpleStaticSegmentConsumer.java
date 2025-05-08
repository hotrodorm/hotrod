package org.hotrod.dynamicsql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hotrod.dynamicsql.parameters.ParameterInstance;
import org.hotrod.dynamicsql.segments.StaticSegmentConsumer;

public class SimpleStaticSegmentConsumer implements StaticSegmentConsumer {

  private StringBuilder sb = new StringBuilder();
  private List<ParameterInstance> parameters = new ArrayList<>();

  @Override
  public void consume(String literal) {
    this.sb.append(literal);
  }

  @Override
  public void consume(ParameterInstance p) {
    this.parameters.add(p);
  }

  @Override
  public void startNextEntry() {
    // Nothing to do
  }

  public String getSQL() {
    return sb.toString();
  }

  public List<ParameterInstance> getParameters() {
    return Collections.unmodifiableList(parameters);
  }

}
