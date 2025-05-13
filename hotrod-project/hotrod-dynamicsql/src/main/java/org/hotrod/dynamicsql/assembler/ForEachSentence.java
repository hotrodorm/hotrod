package org.hotrod.dynamicsql.assembler;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.ForEachSegment;

public class ForEachSentence<P extends AbstractSentence<?, ?>> extends Sentence<ForEachSentence<P>, P> {

  private String item;
  private String collection;
  private String open;
  private String separator;
  private String close;

  public ForEachSentence(DynamicExpressionFactory factory, P parent, String item, String collection, String open,
      String separator, String close) {
    super(factory, null, parent);
    super.setMe(this);
    this.item = item;
    this.collection = collection;
    this.open = open;
    this.separator = separator;
    this.close = close;
  }

  public P endforeach() {
    Shield.addSegment(this.parent, new ForEachSegment(this.item, this.collection, this.open, this.separator, this.close,
        super.segments, super.factory));
    return this.parent;
  }

}
