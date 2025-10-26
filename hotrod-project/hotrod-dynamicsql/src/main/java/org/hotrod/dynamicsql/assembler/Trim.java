package org.hotrod.dynamicsql.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.QuerySegment;
import org.hotrod.dynamicsql.segments.StaticContentSegment;
import org.hotrod.dynamicsql.segments.TrimSegment;

public class Trim<P extends AbstractSentence<?, ?>> extends Sentence<Trim<P>, P> {

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

  public Trim(DynamicExpressionFactory factory, P parent, String header, String separator, String tail) {
    super(factory, null, parent);
    super.setMe(this);
    this.header = header;
    this.separator = separator;
    this.tail = tail;
    this.extendedFormatting = false;
  }

  public Trim(DynamicExpressionFactory factory, P parent, String header, String separator, String tail,
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

//  public If<Trim<P>> if_(String test) {
//    If<Trim<P>> s = new If<>(this.factory, this, test);
//    return s;
//  }
//
//  public Trim<P> literal(String text) {
//    this.segments.add(new StaticContentSegment(text));
//    return this;
//  }

  public P endtrim() {
    List<QuerySegment> ifSegments = this.segments.stream().map(s -> (QuerySegment) s).collect(Collectors.toList());
    TrimSegment s;
    if (this.extendedFormatting) {
      s = new TrimSegment(this.header, this.separator, this.tail, ifSegments, super.factory);
    } else {
      s = new TrimSegment(this.header, this.separator, this.tail, ifSegments, super.factory, this.headerPrefix,
          this.headerSuffix, this.separatorPrefix, this.separatorSuffix, this.tailPrefix, this.tailSuffix,
          this.removePrefixes);
    }
    DynShield.addSegment(this.parent, s);
    return this.parent;
  }

}
