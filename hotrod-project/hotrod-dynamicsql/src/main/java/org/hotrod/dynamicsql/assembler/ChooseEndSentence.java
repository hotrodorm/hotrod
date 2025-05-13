package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.DynamicExpressionFactory;

public class ChooseEndSentence<P extends ChooseSentence<?>, G extends AbstractSentence<?, ?>>
    extends Sentence<ChooseEndSentence<P, G>, P> {

  private G grandpa;

  public ChooseEndSentence(DynamicExpressionFactory factory, P parent, G grandpa) {
    super(factory, null, parent);
    super.setMe(this);
    this.grandpa = grandpa;
  }

  public G endChoose() {
    this.parent.endChoose();
    return this.grandpa;
  }

}
