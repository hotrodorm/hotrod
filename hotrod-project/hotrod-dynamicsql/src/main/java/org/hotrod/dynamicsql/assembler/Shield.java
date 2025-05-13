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

//  public static List<QuerySegment> getSegments(MainSentence sentence) {
//    return sentence.segments;
//  }
//
//  public static List<QuerySegment> getSegments(NestedSentence sentence) {
//    return sentence.segments;
//  }
//
//  public static List<IfSegment> getSegments(IfSequence sentence) {
//    return sentence.segments;
//  }

}
