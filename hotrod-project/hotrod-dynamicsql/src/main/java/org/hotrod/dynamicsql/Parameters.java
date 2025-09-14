package org.hotrod.dynamicsql;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.hotrod.dynamicsql.segments.DynamicContentSegment;

public abstract class Parameters {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(Parameters.class.getName());

  protected Map<String, Object> paramValues = new HashMap<>();

  private Map<DynamicContentSegment, Integer> paramOccurrenceIndexes = new HashMap<>();

  public Integer getIndexAndIncrement(DynamicContentSegment dynamicContentSegment, int loopNestingLevel) {
    if (loopNestingLevel < 1) {
      return null;
    }
    Integer current = this.paramOccurrenceIndexes.get(dynamicContentSegment);
    if (current == null) {
      current = 0;
    }
    this.paramOccurrenceIndexes.put(dynamicContentSegment, current + 1);
    return current;
  }

  public void add(String key, Object value) {
    this.paramValues.put(key, value);
  }

  protected abstract boolean hasParameter(String name);

  protected abstract Object getParameterValue(String name);

  protected abstract void bind(String name, Object obj) throws DynamicExpressionException;

  protected abstract Object unbind(String name);

}
