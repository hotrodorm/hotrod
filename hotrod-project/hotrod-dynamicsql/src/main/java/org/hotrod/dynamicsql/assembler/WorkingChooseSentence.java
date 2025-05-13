package org.hotrod.dynamicsql.assembler;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.ChooseSegment;
import org.hotrod.dynamicsql.segments.OtherwiseSegment;
import org.hotrod.dynamicsql.segments.WhenSegment;

public class WorkingChooseSentence<P extends AbstractSentence<?, ?>> extends AbstractSentence<WorkingChooseSentence<P>, P> {

  List<WhenSegment> whens = new ArrayList<>();
  OtherwiseSegment otherwise = null;

  public WorkingChooseSentence(DynamicExpressionFactory factory, P parent) {
    super(factory, null, parent);
    super.setMe(this);
  }
//
//  public WhenSentence<WorkingChooseSentence<P>> when(String test) {
//    WhenSentence<WorkingChooseSentence<P>> ws = new WhenSentence<WorkingChooseSentence<P>>(this.factory, this.parent, this, test);
//    return ws;
//  }
//
//  public OtherwiseSentence<WorkingChooseSentence<P>> otherwise() {
//    OtherwiseSentence<WorkingChooseSentence<P>> ces = new OtherwiseSentence<WorkingChooseSentence<P>>(this.factory, this.parent,
//        this);
//    return ces;
//  }

  public P endChoose() {
    Shield.addSegment(this.parent, new ChooseSegment(this.whens, this.otherwise, this.factory));
    return this.parent;
  }

}
