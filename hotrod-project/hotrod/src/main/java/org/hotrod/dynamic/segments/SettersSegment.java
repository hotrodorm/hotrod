package org.hotrod.dynamic.segments;

import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.dynamic.builder.ListFormatterConsumer;

public class SettersSegment extends DynamicSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(SettersSegment.class.getName());

  private List<IfSegment> ifSegments;
  private String prefix = "\nSET ";
  private String separator = ",\n    ";

  public SettersSegment(List<IfSegment> ifSegments, DynamicExpressionFactory factory) {
    this.ifSegments = ifSegments;
  }

  @Override
  public void prepare(StaticSegmentConsumer sc, ParameterContext context) throws DynamicExpressionException {
    try (ListFormatterConsumer wc = new ListFormatterConsumer(sc, this.prefix, this.separator, "", ",")) {
      for (IfSegment s : this.ifSegments) {
        wc.startNextEntry();
        s.prepare(wc, context);
      }
    }
  }

}
