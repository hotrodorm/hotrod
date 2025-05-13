package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.DynamicExpressionFactory;

public class NestedSentence<P extends Sentence<?, ?>> extends Sentence<NestedSentence<P>, P> {

  public NestedSentence(DynamicExpressionFactory factory, NestedSentence<P> me, P parent) {
    super(factory, me, parent);
    super.setMe(this);
  }

//  public NestedSentence(DynamicExpressionFactory factory) {
//    super(factory);
//    super.setMe(this);
//  }

//  // Segments
//
//  public NestedSentence set(IfSequence ifSentence) {
//    this.segments.add(new SettersSegment(ifSentence, this.factory));
//    return this;
//  }
//
//  public NestedSentence set(IfSequence ifSentence, String headerPrefix, String headerSuffix, String separatorPrefix,
//      String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
//    this.segments.add(new SettersSegment(ifSentence, this.factory, headerPrefix, headerSuffix, separatorPrefix,
//        separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
//    return this;
//  }
//
//  public NestedSentence where(String separator, IfSequence ifSentence) {
//    this.segments.add(new WhereSegment(separator, ifSentence, this.factory));
//    return this;
//  }
//
//  public NestedSentence where(String separator, IfSequence ifSentence, String headerPrefix, String headerSuffix,
//      String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix, String... removePrefixes) {
//    this.segments.add(new WhereSegment(separator, ifSentence, this.factory, headerPrefix, headerSuffix, separatorPrefix,
//        separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
//    return this;
//  }
//
//  public ChooseSentence choose() {
//    return new ChooseSentence(this);
//  }
//
//  public NestedSentence trim(String header, String separator, String tail, IfSequence ifSentence) {
//    this.segments.add(new TrimSegment(header, separator, tail, ifSentence, this.factory));
//    return this;
//  }
//
//  public NestedSentence trim(String header, String separator, String tail, IfSequence ifSentence, String headerPrefix,
//      String headerSuffix, String separatorPrefix, String separatorSuffix, String tailPrefix, String tailSuffix,
//      String... removePrefixes) {
//    this.segments.add(new TrimSegment(header, separator, tail, ifSentence, this.factory, headerPrefix, headerSuffix,
//        separatorPrefix, separatorSuffix, tailPrefix, tailSuffix, removePrefixes));
//    return this;
//  }
//
//  public NestedSentence foreach(String item, String collection, String open, String separator, String close,
//      NestedSentence content) {
//    this.segments.add(new ForEachSegment(item, collection, open, separator, close, content.segments, this.factory));
//    return this;
//  }
//
//  public NestedSentence bind(String name, String value) {
//    this.segments.add(new BindSegment(name, value, this.factory));
//    return this;
//  }

}
