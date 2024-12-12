package org.hotrod.dynamic.segments;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.dynamic.builder.ClauseFormatter;
import org.hotrod.dynamic.builder.ListFormatterConsumer;
import org.hotrod.dynamic.builder.ListProcessor;

public class SettersSegment extends DynamicListSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(SettersSegment.class.getName());

  private List<IfSegment> ifSegments;

  public SettersSegment(List<IfSegment> ifSegments, DynamicExpressionFactory factory) {
    super(new ListProcessor( //
        new ClauseFormatter("SET", "\n", " "), //
        new ClauseFormatter(",", null, "\n    "), null, null));
    this.ifSegments = ifSegments;
  }

  public SettersSegment(List<IfSegment> ifSegments, DynamicExpressionFactory factory, String headerPrefix,
      String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix,
      String[] removePrefixes) {
    super(new ListProcessor( //
        new ClauseFormatter("SET", headerPrefix, headerSuffix), //
        new ClauseFormatter(",", separatorPrefix, separatorSuffix), //
        removePrefixes, //
        new ClauseFormatter("", tailPrefix, tailSuffix) //
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
