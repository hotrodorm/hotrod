package org.hotrod.dynamicsql.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.IfSegment;
import org.hotrod.dynamicsql.segments.SettersSegment;

public class SetSentence<P extends AbstractSentence<?, ?>> extends AbstractSentence<SetSentence<P>, P> {

  public SetSentence(DynamicExpressionFactory factory, P parent) {
    super(factory, null, parent);
    super.setMe(this);
  }

  public IfSentence<SetSentence<P>> if_(String test) {
    IfSentence<SetSentence<P>> s = new IfSentence(this.factory, this, test);
    return s;
  }

  public P endSet() {
    List<IfSegment> ifSegments = this.segments.stream().map(s -> (IfSegment) s).collect(Collectors.toList());
    SettersSegment w;
    w = new SettersSegment(ifSegments, super.factory);
    Shield.addSegment(this.parent, w);
    return this.parent;
  }

}
