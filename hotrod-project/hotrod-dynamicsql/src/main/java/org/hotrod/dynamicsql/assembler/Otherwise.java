package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.OtherwiseSegment;

public class Otherwise<P extends Choose<?>, G extends AbstractSentence<?, ?>> extends Sentence<Otherwise<P, G>, P> {

  private G grandpa;

  public Otherwise(DynamicExpressionFactory factory, P parent, G grandpa) {
    super(factory, null, parent);
    super.setMe(this);
    this.grandpa = grandpa;
  }

  public ChooseTail<P, G> endotherwise() {
    DynShield.addOtherwise(this.parent, new OtherwiseSegment(super.segments, super.factory));
    ChooseTail<P, G> es = new ChooseTail<>(this.factory, this.parent, this.grandpa);
    return es;
  }

}
