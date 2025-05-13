package org.hotrod.dynamicsql.assembler;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.ChooseSegment;
import org.hotrod.dynamicsql.segments.OtherwiseSegment;
import org.hotrod.dynamicsql.segments.WhenSegment;

public class ChooseSentence<P extends AbstractSentence<?, ?>> extends AbstractSentence<ChooseSentence<P>, P> {

  List<WhenSegment> whens = new ArrayList<>();
  OtherwiseSegment otherwise = null;

  public ChooseSentence(DynamicExpressionFactory factory, P parent) {
    super(factory, null, parent);
    super.setMe(this);
  }

  public WhenSentence<ChooseSentence<P>> when(String test) {
    WhenSentence<ChooseSentence<P>> ws = new WhenSentence<ChooseSentence<P>>(this.factory, this, test);
    return ws;
  }

  public OtherwiseSentence<ChooseSentence<P>> otherwise() {
    OtherwiseSentence<ChooseSentence<P>> ces = new OtherwiseSentence<ChooseSentence<P>>(this.factory, this);
    return ces;
  }

  public P endChoose() {
    Shield.addSegment(this.parent, new ChooseSegment(this.whens, this.otherwise, this.factory));
    return this.parent;
  }

}
