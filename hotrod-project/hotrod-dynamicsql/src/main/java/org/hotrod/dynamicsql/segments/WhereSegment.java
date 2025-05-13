package org.hotrod.dynamicsql.segments;

import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.dynamicsql.assembler.ClauseFormatter;
import org.hotrod.dynamicsql.assembler.IfSequence;
import org.hotrod.dynamicsql.assembler.ListFormatterConsumer;
import org.hotrod.dynamicsql.assembler.ListProcessor;
import org.hotrod.dynamicsql.assembler.Shield;

public class WhereSegment extends DynamicListSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(WhereSegment.class.getName());

  private static final ClauseFormatter DEFAULT_HEADER_FORMATTER = new ClauseFormatter("WHERE", "\n", " ");
  private static final String DEFAULT_MIDDLE_PREFIX = "\n  ";
  private static final String DEFAULT_MIDDLE_SUFFIX = " ";

  private IfSequence ifSentence;

  public WhereSegment(String separator, IfSequence ifSentence, DynamicExpressionFactory factory) {
    super(new ListProcessor(DEFAULT_HEADER_FORMATTER,
        new ClauseFormatter(separator, DEFAULT_MIDDLE_PREFIX, DEFAULT_MIDDLE_SUFFIX), null, null));
    this.ifSentence = ifSentence;
  }

  public WhereSegment(String separator, IfSequence ifSentence, DynamicExpressionFactory factory, String headerPrefix,
      String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix,
      String... removePrefixes) {
    super(new ListProcessor( //
        new ClauseFormatter("WHERE", headerPrefix, headerSuffix), //
        new ClauseFormatter(separator, separatorPrefix, separatorSuffix), //
        removePrefixes, //
        new ClauseFormatter("", tailPrefix, tailSuffix) //
    ));
    this.ifSentence = ifSentence;
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, Parameters context, int loopNestingLevel)
      throws DynamicExpressionException {
    try (ListFormatterConsumer wc = new ListFormatterConsumer(sc, super.processor)) {
//      for (IfSegment s : Shield.getSegments(this.ifSentence)) {
//        wc.startNextEntry();
//        s.prepare(wc, context, loopNestingLevel);
//      }
    }
    return true;
  }

}
