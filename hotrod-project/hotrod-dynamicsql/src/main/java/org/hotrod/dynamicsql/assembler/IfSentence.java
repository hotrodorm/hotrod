package org.hotrod.dynamicsql.assembler;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.IfSegment;

public class IfSentence {

  DynamicExpressionFactory factory;
  List<IfSegment> segments = new ArrayList<>();

  public IfSentence(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  // Segments

  public IfSentence if_(String test, Sentence sentence) {
    this.segments.add(new IfSegment(test, sentence, this.factory));
    return this;
  }

}
