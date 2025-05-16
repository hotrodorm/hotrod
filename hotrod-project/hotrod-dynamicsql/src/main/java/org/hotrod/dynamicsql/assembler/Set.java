package org.hotrod.dynamicsql.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.IfSegment;
import org.hotrod.dynamicsql.segments.SettersSegment;

public class Set<P extends AbstractSentence<?, ?>> extends AbstractSentence<Set<P>, P> {

  public Set(DynamicExpressionFactory factory, P parent) {
    super(factory, null, parent);
    super.setMe(this);
  }

  @SuppressWarnings("unchecked")
  public If<Set<P>> if_(String test) {
    @SuppressWarnings("rawtypes")
    If<Set<P>> s = new If(this.factory, this, test);
    return s;
  }

  public P endset() {
    List<IfSegment> ifSegments = this.segments.stream().map(s -> (IfSegment) s).collect(Collectors.toList());
    SettersSegment w = new SettersSegment(ifSegments, super.factory);
    Shield.addSegment(this.parent, w);
    return this.parent;
  }

}
