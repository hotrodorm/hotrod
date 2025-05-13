package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.OtherwiseSegment;

public class OtherwiseSentence<P extends ChooseSentence<?>, G extends AbstractSentence<?, ?>>
    extends Sentence<OtherwiseSentence<P, G>, P> {

  private G grandpa;

  public OtherwiseSentence(DynamicExpressionFactory factory, P parent, G grandpa) {
    super(factory, null, parent);
    super.setMe(this);
    this.grandpa = grandpa;
  }

  public ChooseTailSentence<P, G> endotherwise() {
    Shield.addOtherwise(this.parent, new OtherwiseSegment(super.segments, super.factory));
    ChooseTailSentence<P, G> es = new ChooseTailSentence<P, G>(this.factory, this.parent, this.grandpa);
    return es;
  }

}
