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

  private boolean extendedFormatting;
  private String headerPrefix;
  private String headerSuffix;
  private String separatorPrefix;
  private String separatorSuffix;
  private String tailPrefix;
  private String tailSuffix;
  private String[] removePrefixes;

  public TrimSentence(DynamicExpressionFactory factory, P parent, String header, String separator, String tail) {
    super(factory, null, parent);
    super.setMe(this);
    this.header = header;
    this.separator = separator;
    this.tail = tail;
    this.extendedFormatting = false;
  }

  public TrimSentence(DynamicExpressionFactory factory, P parent, String header, String separator, String tail,
      String headerPrefix, String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix,
      String tailSuffix, String... removePrefixes) {
    super(factory, null, parent);
    super.setMe(this);
    this.header = header;
    this.separator = separator;
    this.tail = tail;

    this.extendedFormatting = true;
    this.headerPrefix = headerPrefix;
    this.headerSuffix = headerSuffix;
    this.separatorPrefix = separatorPrefix;
    this.separatorSuffix = separatorSuffix;
    this.tailPrefix = tailPrefix;
    this.tailSuffix = tailSuffix;
    this.removePrefixes = removePrefixes;
  }

  public IfSentence<TrimSentence<P>> if_(String test) {
    IfSentence<TrimSentence<P>> s = new IfSentence(this.factory, this, test);
    return s;
  }

  public P endtrim() {
    List<IfSegment> ifSegments = this.segments.stream().map(s -> (IfSegment) s).collect(Collectors.toList());
    TrimSegment s;
    if (this.extendedFormatting) {
      s = new TrimSegment(this.header, this.separator, this.tail, ifSegments, super.factory);
    } else {
      s = new TrimSegment(this.header, this.separator, this.tail, ifSegments, super.factory, this.headerPrefix,
          this.headerSuffix, this.separatorPrefix, this.separatorSuffix, this.tailPrefix, this.tailSuffix,
          this.removePrefixes);
    }
    Shield.addSegment(this.parent, s);
    return this.parent;
  }

}
