package org.hotrod.dynamic.segments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamic.DynamicExpression;
import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;
import org.hotrod.utils.SUtil;

public class ForEachSegment extends DynamicSegment {

  private static final Logger log = Logger.getLogger(ForEachSegment.class.getName());

  private String item;
  private String collection;
  private LiteralSegment open;
  private LiteralSegment separator;
  private LiteralSegment close;

  private DynamicExpressionFactory factory;
  private DynamicExpression collectionExpression;

  private List<QuerySegment> segments = new ArrayList<>();

  private Object[] array;
  private Collection<?> coll;

  public ForEachSegment(String item, String collection, String open, String separator, String close,
      SegmentList segmentList, DynamicExpressionFactory factory) throws DynamicExpressionException {
    super();

    this.factory = factory;

    // Item

    if (SUtil.isEmpty(item)) {
      throw new DynamicExpressionException("The 'item' property of a Dynamic SQL FOREACH cannot be empty.");
    }
    this.item = item.trim();

    // Collection

    if (SUtil.isEmpty(collection)) {
      throw new DynamicExpressionException("The 'collection' property of a Dynamic SQL FOREACH cannot be empty.");
    }
    this.collection = collection;
    this.collectionExpression = this.factory.expression(this.collection);

    // Open, Separator, Close

    this.open = new LiteralSegment(SUtil.coalesce(open, ""));
    this.separator = new LiteralSegment(SUtil.coalesce(separator, ""));
    this.close = new LiteralSegment(SUtil.coalesce(close, ""));

    // Segments

    if (segmentList == null) {
      throw new DynamicExpressionException("The 'body' of a Dynamic SQL FOREACH cannot be empty.");
    }
    this.segments = segmentList.getSegments();
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context, int loopNestingLevel)
      throws DynamicExpressionException {

    Object obj = this.collectionExpression.evaluate(context);
    if (obj == null) {
      return false;
    }
    if (obj.getClass().isArray()) {
      this.array = (Object[]) obj;
      this.coll = null;
    } else if (Collection.class.isAssignableFrom(obj.getClass())) {
      this.array = null;
      this.coll = (Collection<?>) obj;
    } else {
      throw new DynamicExpressionException(
          "The 'collection' property of a Dynamic SQL FOREACH must evaluate to a java.util.Collection class or an array, but it evaluated to:"
              + obj.getClass().getName());
    }

    if (context.hasParameter(this.item)) {
      throw new DynamicExpressionException("The variable '" + this.item
          + "' defined by the 'item' property of a Dynamic SQL FOREACH already exists. Cannot shadow an existing variable");
    }

    Collection<?> elements = this.array != null ? Arrays.asList(this.array) : this.coll;

    this.open.prepare(sc, context, loopNestingLevel);

    boolean firstElement = true;
    for (Object o : elements) {
      if (firstElement) {
        firstElement = false;
      } else {
        this.separator.prepare(sc, context, loopNestingLevel + 1);
      }
      context.bind(this.item, o);
//      log.info("o: " + o);
      for (QuerySegment w : this.segments) {
        w.prepare(sc, context, loopNestingLevel + 1);
      }
    }
    context.unbind(this.item);

    this.close.prepare(sc, context, loopNestingLevel);

    return true;

  }

}
