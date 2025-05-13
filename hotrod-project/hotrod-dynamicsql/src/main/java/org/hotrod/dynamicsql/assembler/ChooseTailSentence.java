package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.DynamicExpressionFactory;

public class ChooseTailSentence<P extends ChooseSentence<?>, G extends AbstractSentence<?, ?>>
    extends Sentence<ChooseTailSentence<P, G>, P> {

  private G grandpa;

  public ChooseTailSentence(DynamicExpressionFactory factory, P parent, G grandpa) {
    super(factory, null, parent);
    super.setMe(this);
    this.grandpa = grandpa;
  }

  public G endchoose() {
    this.parent.endchoose();
    return this.grandpa;
  }

}
