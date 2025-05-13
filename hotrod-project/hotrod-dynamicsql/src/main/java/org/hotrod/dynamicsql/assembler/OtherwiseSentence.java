package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.OtherwiseSegment;

public class OtherwiseSentence<P extends ChooseSentence<?>> extends Sentence<OtherwiseSentence<P>, P> {

  public OtherwiseSentence(DynamicExpressionFactory factory, P parent) {
    super(factory, null, parent);
    super.setMe(this);
  }

  public ChooseEndSentence<P> endOtherwise() {
    Shield.addOtherwise(this.parent, new OtherwiseSegment(super.segments, super.factory));
    ChooseEndSentence<P> es = new ChooseEndSentence<P>(this.factory, this.parent);
    return es;
  }

}
