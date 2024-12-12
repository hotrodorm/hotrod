package org.hotrod.dynamic.segments;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.dynamic.builder.ClauseFormatter;
import org.hotrod.dynamic.builder.ListFormatterConsumer;
import org.hotrod.dynamic.builder.ListProcessor;

public class TrimSegment extends DynamicListSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(TrimSegment.class.getName());

  private static final String DEFAULT_HEADER_PREFIX = "\n";
  private static final String DEFAULT_HEADER_SUFFIX = " ";
  private static final String DEFAULT_SEPARATOR_PREFIX = "\n";
  private static final String DEFAULT_SEPARATOR_SUFFIX = " ";
  private static final String DEFAULT_TAIL_PREFIX = "\n";
  private static final String DEFAULT_TAIL_SUFFIX = "";

  private List<IfSegment> ifSegments;

  public TrimSegment(String header, String separator, String tail, List<IfSegment> ifSegments,
      DynamicExpressionFactory factory) {
    super(new ListProcessor( //
        new ClauseFormatter(header, DEFAULT_HEADER_PREFIX, DEFAULT_HEADER_SUFFIX), //
        new ClauseFormatter(separator, DEFAULT_SEPARATOR_PREFIX, DEFAULT_SEPARATOR_SUFFIX), //
        null, //
        new ClauseFormatter(tail, DEFAULT_TAIL_PREFIX, DEFAULT_TAIL_SUFFIX) //
    ));
    this.ifSegments = ifSegments;
  }

  public TrimSegment(String header, String separator, String tail, List<IfSegment> ifSegments,
      DynamicExpressionFactory factory, String headerPrefix, String headerSuffix, String separatorPrefix,
      String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
    super(new ListProcessor( //
        new ClauseFormatter(header, headerPrefix, headerSuffix), //
        new ClauseFormatter(separator, separatorPrefix, separatorSuffix), //
        removePrefixes, //
        new ClauseFormatter(tail, tailPrefix, tailSuffix) //
    ));
    this.ifSegments = ifSegments;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context) throws DynamicExpressionException {
    try (ListFormatterConsumer wc = new ListFormatterConsumer(sc, super.processor)) {
      for (IfSegment s : this.ifSegments) {
        wc.startNextEntry();
        s.prepare(wc, context);
      }
    }
    return true;
  }

}
