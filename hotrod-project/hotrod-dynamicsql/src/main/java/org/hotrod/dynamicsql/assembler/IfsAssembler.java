package org.hotrod.dynamicsql.assembler;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.IfSegment;

public class IfsAssembler {

  private DynamicExpressionFactory factory;
  private List<IfSegment> ifSegments = new ArrayList<>();

  public IfsAssembler(DynamicExpressionFactory factory) {
    this.factory = factory;
  }

  // IF Segments

  public IfsAssembler if_(String test, Sentence sentence) {
    this.ifSegments.add(new IfSegment(test, sentence, this.factory));
    return this;
  }

  // end

  public List<IfSegment> end() {
    return this.ifSegments;
  }

}
