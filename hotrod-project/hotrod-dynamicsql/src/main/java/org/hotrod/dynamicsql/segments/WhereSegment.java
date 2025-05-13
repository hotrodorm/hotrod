package org.hotrod.dynamicsql.segments;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.assembler.ClauseFormatter;
import org.hotrod.dynamicsql.assembler.ListProcessor;

public class WhereSegment extends DynamicListSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(WhereSegment.class.getName());

  private static final ClauseFormatter DEFAULT_HEADER_FORMATTER = new ClauseFormatter("WHERE", "\n", " ");
  private static final String DEFAULT_MIDDLE_PREFIX = "\n  ";
  private static final String DEFAULT_MIDDLE_SUFFIX = " ";

  private List<IfSegment> segments;

  public WhereSegment(String separator, List<IfSegment> segments, DynamicExpressionFactory factory) {
    super(new ListProcessor(DEFAULT_HEADER_FORMATTER,
        new ClauseFormatter(separator, DEFAULT_MIDDLE_PREFIX, DEFAULT_MIDDLE_SUFFIX), null, null));
    this.segments = segments;
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
