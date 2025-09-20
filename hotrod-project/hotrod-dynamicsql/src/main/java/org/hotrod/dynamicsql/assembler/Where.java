package org.hotrod.dynamicsql.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.IfSegment;
import org.hotrod.dynamicsql.segments.WhereSegment;

public class Where<P extends AbstractSentence<?, ?>> extends AbstractSentence<Where<P>, P> {

  private String separator;

  public Where(DynamicExpressionFactory factory, P parent, String separator) {
    super(factory, null, parent);
    super.setMe(this);
    this.separator = separator;
  }

  @SuppressWarnings("unchecked")
  public If<Where<P>> if_(String test) {
    @SuppressWarnings("rawtypes")
    If<Where<P>> s = new If(this.factory, this, test);
    return s;
  }

  public P endwhere() {
    List<IfSegment> ifSegments = this.segments.stream().map(s -> (IfSegment) s).collect(Collectors.toList());
    WhereSegment s = new WhereSegment(this.separator, ifSegments, super.factory);
    DynShield.addSegment(this.parent, s);
    return this.parent;
  }

}
