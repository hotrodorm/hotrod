package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.WhenSegment;

public class WhenSentence<P extends ChooseSentence<?>> extends Sentence<WhenSentence<P>, P> {

  private String test;

  public WhenSentence(DynamicExpressionFactory factory, P parent, String test) {
    super(factory, null, parent);
    this.test = test;
    super.setMe(this);
  }

  public P endwhen() {
    Shield.addWhen(this.parent, new WhenSegment(this.test, super.segments, super.factory));
    return this.parent;
  }

}
