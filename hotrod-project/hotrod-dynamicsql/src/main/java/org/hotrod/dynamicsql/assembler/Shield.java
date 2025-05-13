package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.segments.OtherwiseSegment;
import org.hotrod.dynamicsql.segments.QuerySegment;
import org.hotrod.dynamicsql.segments.WhenSegment;

public class Shield {

  public static void addSegment(AbstractSentence<?, ?> parent, QuerySegment segment) {
    parent.segments.add(segment);
  }

  public static void addWhen(ChooseSentence<?> parent, WhenSegment when) {
    parent.whens.add(when);
  }

  public static void addOtherwise(ChooseSentence<?> parent, OtherwiseSegment otherwise) {
    parent.otherwise = otherwise;
  }

}
