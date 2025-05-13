package org.hotrod.dynamicsql.assembler;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.segments.QuerySegment;

public abstract class AbstractSentence<M extends AbstractSentence<?, ?>, P> {

  protected M me;
  protected P parent;
  protected DynamicExpressionFactory factory;
  protected List<QuerySegment> segments = new ArrayList<>();

  public AbstractSentence(DynamicExpressionFactory factory, M me, P parent) {
    this.factory = factory;
    this.me = me;
    this.parent = parent;
  }

  protected void setMe(M me) {
    this.me = me;
  }

}
