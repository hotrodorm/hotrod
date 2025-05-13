package org.hotrod.dynamicsql.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.IfSegment;
import org.hotrod.dynamicsql.segments.TrimSegment;

public class TrimSentence<P extends AbstractSentence<?, ?>> extends AbstractSentence<TrimSentence<P>, P> {

  private String header;
  private String separator;
  private String tail;

  public TrimSentence(DynamicExpressionFactory factory, P parent, String header, String separator, String tail) {
    super(factory, null, parent);
    super.setMe(this);
    this.header = header;
    this.separator = separator;
    this.tail = tail;
  }

  public IfSentence<TrimSentence<P>> if_(String test) {
    IfSentence<TrimSentence<P>> s = new IfSentence(this.factory, this, test);
    return s;
  }

  public P endtrim() {
    List<IfSegment> ifSegments = this.segments.stream().map(s -> (IfSegment) s).collect(Collectors.toList());
    Shield.addSegment(this.parent, new TrimSegment(this.header, this.separator, this.tail, ifSegments, super.factory));
    return this.parent;
  }

}
