package org.hotrod.dynamicsql.assembler;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.ChooseSegment;
import org.hotrod.dynamicsql.segments.OtherwiseSegment;
import org.hotrod.dynamicsql.segments.WhenSegment;

public class ChooseEndSentence<P extends ChooseSentence<?>> extends Sentence<ChooseEndSentence<P>, P> {

  private List<WhenSegment> whens = new ArrayList<>();
  private OtherwiseSegment otherwise = null;

  public ChooseEndSentence(DynamicExpressionFactory factory, P parent) {
    super(factory, null, parent);
    super.setMe(this);
  }

  public P endChoose() {
    Shield.addSegment(this.parent, new ChooseSegment(this.whens, this.otherwise, super.factory));
    return this.parent;
  }

}
