package org.hotrod.dynamicsql.segments;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.assembler.ClauseFormatter;
import org.hotrod.dynamicsql.assembler.ListProcessor;

public class TrimSegment extends DynamicListSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(TrimSegment.class.getName());

  private static final String DEFAULT_HEADER_PREFIX = "";
  private static final String DEFAULT_HEADER_SUFFIX = "";
  private static final String DEFAULT_SEPARATOR_PREFIX = "";
  private static final String DEFAULT_SEPARATOR_SUFFIX = "";
  private static final String DEFAULT_TAIL_PREFIX = "";
  private static final String DEFAULT_TAIL_SUFFIX = "";

  private List<IfSegment> segments;
  @SuppressWarnings("unused")
  private DynamicExpressionFactory factory;

  public TrimSegment(String header, String separator, String tail, List<IfSegment> segments,
      DynamicExpressionFactory factory) {
    super(new ListProcessor( //
        new ClauseFormatter(header, DEFAULT_HEADER_PREFIX, DEFAULT_HEADER_SUFFIX), //
        new ClauseFormatter(separator, DEFAULT_SEPARATOR_PREFIX, DEFAULT_SEPARATOR_SUFFIX), //
        null, //
        new ClauseFormatter(tail, DEFAULT_TAIL_PREFIX, DEFAULT_TAIL_SUFFIX) //
    ));
    this.segments = segments;
    this.factory = factory;
  }

  public TrimSegment(String header, String separator, String tail, List<IfSegment> segments,
      DynamicExpressionFactory factory, String headerPrefix, String headerSuffix, String separatorPrefix,
      String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    super(new ListProcessor( //
        new ClauseFormatter(header, headerPrefix, headerSuffix), //
        new ClauseFormatter(separator, separatorPrefix, separatorSuffix), //
        removePrefixes, //
        new ClauseFormatter(tail, tailPrefix, tailSuffix) //
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
