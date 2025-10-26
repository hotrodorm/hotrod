package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.BeginSegment;

public class Begin<P extends AbstractSentence<?, ?>> extends Sentence<Begin<P>, P> {

  public Begin(DynamicExpressionFactory factory, P parent) {
    super(factory, null, parent);
    super.setMe(this);
  }

  public P end() {
    DynShield.addSegment(this.parent, new BeginSegment(super.segments, super.factory));
    return this.parent;
  }

}
