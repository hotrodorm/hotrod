package org.hotrod.dynamicsql.segments;

import org.hotrod.dynamicsql.assembler.ListProcessor;

public abstract class DynamicListSegment extends ControlSegment {

  protected ListProcessor processor;

  public DynamicListSegment(ListProcessor processor) {
    super();
    this.processor = processor;
  }

}
