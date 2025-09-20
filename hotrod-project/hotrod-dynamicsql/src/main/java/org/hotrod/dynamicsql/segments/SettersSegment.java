package org.hotrod.dynamicsql.segments;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.assembler.ClauseFormatter;
import org.hotrod.dynamicsql.assembler.ListProcessor;

public class SettersSegment extends DynamicListSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(SettersSegment.class.getName());

  private List<IfSegment> segments;
  @SuppressWarnings("unused")
  private DynamicExpressionFactory factory;

  public SettersSegment(List<IfSegment> segments, DynamicExpressionFactory factory) {
    super(new ListProcessor( //
        new ClauseFormatter("SET", "\n", " "), //
        new ClauseFormatter(",", null, "\n    "), null, new ClauseFormatter("\n", null, null) //
    ));
    this.segments = segments;
    this.factory = factory;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
      throws DynamicExpressionException {
    try (ListFormatterConsumer wc = new ListFormatterConsumer(sc, super.processor)) {
      for (IfSegment s : this.segments) {
        wc.startNextEntry();
        s.prepare(wc, context, loopNestingLevel);
      }
    }
    return true;
  }

}
