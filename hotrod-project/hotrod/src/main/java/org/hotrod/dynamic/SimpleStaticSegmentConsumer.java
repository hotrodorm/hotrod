package org.hotrod.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hotrod.dynamic.segments.ParameterSegment;
import org.hotrod.dynamic.segments.StaticSegmentConsumer;
import org.hotrod.dynamic.segments.TypedParameterSegment;

public class SimpleStaticSegmentConsumer implements StaticSegmentConsumer {

  private StringBuilder sb = new StringBuilder();
  private List<ParameterSegment> parameters = new ArrayList<>();

  @Override
  public void consume(String literal) {
    this.sb.append(literal);
  }

  @Override
  public void consume(ParameterSegment p) {
    this.parameters.add(p);
  }

  @Override
  public void startNextEntry() {
    // Nothing to do
  }

  public String getSQL() {
    return sb.toString();
  }

  public List<ParameterSegment> getParameters() {
    return Collections.unmodifiableList(parameters);
  }

}
