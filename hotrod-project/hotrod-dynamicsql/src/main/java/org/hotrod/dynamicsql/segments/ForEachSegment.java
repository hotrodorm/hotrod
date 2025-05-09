package org.hotrod.dynamicsql.segments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.DynamicExpression;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.ParameterContext;
import org.hotrod.dynamicsql.Utl;

public class ForEachSegment extends ControlSegment {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ForEachSegment.class.getName());

  private String item;
  private String collection;
  private StaticContentSegment open;
  private StaticContentSegment separator;
  private StaticContentSegment close;

  private DynamicExpressionFactory factory;
  private DynamicExpression collectionExpression;

  private List<QuerySegment> segments = new ArrayList<>();

  private Object[] array;
  private Collection<?> coll;

  public ForEachSegment(String item, String collection, String open, String separator, String close,
      List<QuerySegment> segments, DynamicExpressionFactory factory) {
    super();

    this.factory = factory;

    // Item

    this.item = item == null ? null : item.trim();

    // Collection

    this.collection = collection;

    // Open, Separator, Close

    this.open = new StaticContentSegment(Utl.coalesce(open, ""));
    this.separator = new StaticContentSegment(Utl.coalesce(separator, ""));
    this.close = new StaticContentSegment(Utl.coalesce(close, ""));

    // Segments

    this.segments = segments;

  }

  private boolean validated = false;

  private synchronized void validate() throws DynamicExpressionException {
    if (!this.validated) {

      // Item

      if (Utl.isEmpty(this.item)) {
        throw new DynamicExpressionException("The 'item' property of a Dynamic SQL FOREACH cannot be empty.");
      }

      // Collection

      if (Utl.isEmpty(this.collection)) {
        throw new DynamicExpressionException("The 'collection' property of a Dynamic SQL FOREACH cannot be empty.");
      }
      this.collectionExpression = this.factory.expression(this.collection);

      // Segments

      if (this.segments == null) {
        throw new DynamicExpressionException("The 'body' of a Dynamic SQL FOREACH cannot be empty.");
      }

      this.validated = true;
    }
  }

  @Override
  public boolean prepare(StaticSegmentConsumer sc, ParameterContext context, int loopNestingLevel)
      throws DynamicExpressionException {

    if (!this.validated) {
      this.validate();
    }

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
      for (QuerySegment w : this.segments) {
        w.prepare(sc, context, loopNestingLevel + 1);
      }
    }
    context.unbind(this.item);

    this.close.prepare(sc, context, loopNestingLevel);

    return true;

  }

}
