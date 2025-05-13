package org.hotrod.dynamicsql.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.IfSegment;
import org.hotrod.dynamicsql.segments.WhereSegment;

public class WhereSentence<P extends AbstractSentence<?, ?>> extends AbstractSentence<WhereSentence<P>, P> {

  private String separator;

  public WhereSentence(DynamicExpressionFactory factory, P parent, String separator) {
    super(factory, null, parent);
    super.setMe(this);
    this.separator = separator;
  }

  public WhereSentence(DynamicExpressionFactory factory, P parent, String separator, String headerPrefix,
      String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix,
      String... removePrefixes) {
    super(factory, null, parent);
    super.setMe(this);
    this.separator = separator;
  }

  public IfSentence<WhereSentence<P>> if_(String test) {
    IfSentence<WhereSentence<P>> s = new IfSentence(this.factory, this, test);
    return s;
  }

  public P endwhere() {
    List<IfSegment> ifSegments = this.segments.stream().map(s -> (IfSegment) s).collect(Collectors.toList());
    WhereSegment s = new WhereSegment(this.separator, ifSegments, super.factory);
    Shield.addSegment(this.parent, s);
    return this.parent;
  }

}
