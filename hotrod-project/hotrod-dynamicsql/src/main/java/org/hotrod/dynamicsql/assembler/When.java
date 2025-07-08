package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.WhenSegment;

public class When<P extends Choose<?>> extends Sentence<When<P>, P> {

  private String test;

  public When(DynamicExpressionFactory factory, P parent, String test) {
    super(factory, null, parent);
    this.test = test;
    super.setMe(this);
  }

  public P endwhen() {
    DynShield.addWhen(this.parent, new WhenSegment(this.test, super.segments, super.factory));
    return this.parent;
  }

}
