package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.IfSegment;

public class If<P extends AbstractSentence<?, ?>> extends Sentence<If<P>, P> {

  private String test;

  public If(DynamicExpressionFactory factory, P parent, String test) {
    super(factory, null, parent);
    super.setMe(this);
    this.test = test;
  }

  public P endif() {
    Shield.addSegment(this.parent, new IfSegment(this.test, super.segments, super.factory));
    return this.parent;
  }

}
