package org.hotrod.dynamic.segments;

import java.util.List;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;

public class WhereSegment extends DynamicSegment {

  private String delimiter;
  private List<IfSegment> segments;

  public WhereSegment(String delimiter, List<IfSegment> segments, DynamicExpressionFactory factory) {
    this.delimiter = delimiter;
    this.segments = segments;
  }

  @Override
  public void prepare(StaticSegmentConsumer sc, ParameterContext context) throws DynamicExpressionException {

    // 1. Evaluate the IF segments, and include the first one that evaluates to true
    WhereSegmentConsumer wc = new WhereSegmentConsumer(sc, this.delimiter);
    for (IfSegment s : this.segments) {
      s.prepare(wc, context);
    }

  }

  private class WhereSegmentConsumer implements StaticSegmentConsumer {

    private StaticSegmentConsumer parentConsumer;
    private String delimiter;
    private boolean first;

    public WhereSegmentConsumer(StaticSegmentConsumer parentConsumer, String delimiter) {
      this.parentConsumer = parentConsumer;
      this.delimiter = delimiter;
    }

    @Override
    public void consume(String literal) {
      if (this.first) {
        this.first = false;
        String ll = literal.toLowerCase();
        if (ll.startsWith("and")) {
          this.parentConsumer.consume(literal.substring("and".length()));
        } else if (ll.startsWith("or")) {
          this.parentConsumer.consume(literal.substring("or".length()));
        }
      }
    }

    @Override
    public void consume(ParameterSegment s) {
      this.first = false;
      this.parentConsumer.consume(s);
    }

  }

}
