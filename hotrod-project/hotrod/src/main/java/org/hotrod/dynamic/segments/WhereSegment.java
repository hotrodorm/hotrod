package org.hotrod.dynamic.segments;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.dynamic.builder.ClauseFormatter;
import org.hotrod.dynamic.builder.ListFormatterConsumer;
import org.hotrod.dynamic.builder.ListProcessor;

public class WhereSegment extends DynamicListSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(WhereSegment.class.getName());

  private static final ClauseFormatter DEFAULT_HEADER_FORMATTER = new ClauseFormatter("WHERE", "\n", " ");
  private static final String DEFAULT_MIDDLE_PREFIX = "\n  ";
  private static final String DEFAULT_MIDDLE_SUFFIX = " ";

  private List<IfSegment> ifSegments;

  public WhereSegment(String separator, List<IfSegment> ifSegments, DynamicExpressionFactory factory) {
    super(new ListProcessor(DEFAULT_HEADER_FORMATTER,
        new ClauseFormatter(separator, DEFAULT_MIDDLE_PREFIX, DEFAULT_MIDDLE_SUFFIX), null, null));
    this.ifSegments = ifSegments;
  }

  public WhereSegment(String separator, List<IfSegment> ifSegments, DynamicExpressionFactory factory,
      String headerPrefix, String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix,
      String tailSuffix, String... removePrefixes) {
    super(new ListProcessor( //
        new ClauseFormatter("WHERE", headerPrefix, headerSuffix), //
        new ClauseFormatter(separator, separatorPrefix, separatorSuffix), //
        removePrefixes, //
        new ClauseFormatter("", tailPrefix, tailSuffix) //
    ));
    this.ifSegments = ifSegments;
  }

  @Override
  public void prepare(StaticSegmentConsumer sc, ParameterContext context) throws DynamicExpressionException {
    try (ListFormatterConsumer wc = new ListFormatterConsumer(sc, super.processor)) {
      for (IfSegment s : this.ifSegments) {
        wc.startNextEntry();
        s.prepare(wc, context);
      }
    }
  }

}
