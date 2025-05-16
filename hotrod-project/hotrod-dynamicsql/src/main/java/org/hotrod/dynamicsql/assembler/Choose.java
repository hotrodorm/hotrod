package org.hotrod.dynamicsql.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.ChooseSegment;
import org.hotrod.dynamicsql.segments.OtherwiseSegment;
import org.hotrod.dynamicsql.segments.WhenSegment;

public class Choose<P extends AbstractSentence<?, ?>> extends AbstractSentence<Choose<P>, P> {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(Choose.class.getName());

  List<WhenSegment> whens = new ArrayList<>();
  OtherwiseSegment otherwise = null;

  public Choose(DynamicExpressionFactory factory, P parent) {
    super(factory, null, parent);
    super.setMe(this);
  }

  public When<Choose<P>> when(String test) {
    When<Choose<P>> ws = new When<Choose<P>>(this.factory, this, test);
    return ws;
  }

  public Otherwise<Choose<P>, P> otherwise() {
    Otherwise<Choose<P>, P> ces = new Otherwise<Choose<P>, P>(this.factory, this,
        this.parent);
    return ces;
  }

  public P endchoose() {
    Shield.addSegment(this.parent, new ChooseSegment(this.whens, this.otherwise, this.factory));
    return this.parent;
  }

}
