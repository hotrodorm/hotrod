package org.hotrod.dynamic.segments;

import org.hotrod.dynamic.assembler.ListProcessor;

public abstract class DynamicListSegment extends DynamicSegment {

  protected ListProcessor processor;

  public DynamicListSegment(ListProcessor processor) {
    super();
    this.processor = processor;
  }

}
