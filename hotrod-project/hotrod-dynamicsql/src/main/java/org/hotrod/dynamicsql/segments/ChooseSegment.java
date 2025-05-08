package org.hotrod.dynamicsql.segments;

import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.ParameterContext;

public class ChooseSegment extends ControlSegment {

  private List<WhenSegment> whens;
  private OtherwiseSegment otherwise = null;

  public ChooseSegment(List<WhenSegment> whens, OtherwiseSegment otherwise) {
    super();
    this.whens = whens;
    this.otherwise = otherwise;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context, int loopNestingLevel)
      throws DynamicExpressionException {

    for (WhenSegment w : this.whens) {
      boolean included = w.prepare(sc, context, loopNestingLevel);
      if (included) {
        return true;
      }
    }

    if (this.otherwise != null) {
      return this.otherwise.prepare(sc, context, loopNestingLevel);
    }

    return false;

  }

}
