package org.hotrod.dynamicsql.assembler;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.IfSegment;

@Deprecated
public class IfSequence {

  DynamicExpressionFactory factory;
  List<IfSegment> segments = new ArrayList<>();

  public IfSequence(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  // Segments

  public IfSequence if_(String test, NestedSentence sentence) {
//    this.segments.add(new IfSegment(test, sentence, this.factory));
    return this;
  }

}
