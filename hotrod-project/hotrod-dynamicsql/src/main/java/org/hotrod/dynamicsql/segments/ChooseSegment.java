package org.hotrod.dynamicsql.segments;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;

public class ChooseSegment extends ControlSegment {

  private static final Logger log = Logger.getLogger(ChooseSegment.class.getName());

  private List<WhenSegment> whens;
  private OtherwiseSegment otherwise = null;
  private DynamicExpressionFactory factory;

  public ChooseSegment(List<WhenSegment> whens, OtherwiseSegment otherwise, DynamicExpressionFactory factory) {
    super();
    this.whens = whens;
    this.otherwise = otherwise;
    this.factory = factory;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
      throws DynamicExpressionException {

    log.info("this.whens=" + this.whens);
    for (WhenSegment w : this.whens) {
      log.info(">> w=" + w);
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
