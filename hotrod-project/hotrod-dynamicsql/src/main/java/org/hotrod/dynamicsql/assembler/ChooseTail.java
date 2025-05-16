package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.DynamicExpressionFactory;

public class ChooseTail<P extends Choose<?>, G extends AbstractSentence<?, ?>>
    extends Sentence<ChooseTail<P, G>, P> {

  private G grandpa;

  public ChooseTail(DynamicExpressionFactory factory, P parent, G grandpa) {
    super(factory, null, parent);
    super.setMe(this);
    this.grandpa = grandpa;
  }

  public G endchoose() {
    this.parent.endchoose();
    return this.grandpa;
  }

}
