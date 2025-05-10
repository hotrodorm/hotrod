package org.hotrod.dynamicsql.assembler;

import java.util.List;

import org.hotrod.dynamicsql.segments.IfSegment;
import org.hotrod.dynamicsql.segments.QuerySegment;

public class Shield {
  
  public static List<QuerySegment> getSegments(Sentence sentence) {
    return sentence.segments;
  }

  public static List<IfSegment> getSegments(IfSentence sentence) {
    return sentence.segments;
  }

}
