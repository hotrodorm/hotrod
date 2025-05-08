package org.hotrod.dynamicsql.parameters;

/**
 * <pre>
 * - QuerySegment
     - StaticSegment
       - ParameterSegment
         - TypedParameterSegment
           - ParameterInstanceValueSegment
         - VariableInstanceValueSegment
       - ParameterInjectionSegment
     - ParameterOccurenceSegment
 * </pre>
 */

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hotrod.dynamicsql.segments.DynamicContentSegment;

public abstract class ParameterInstance {

  private static Map<DynamicContentSegment, Integer> counters = new HashMap<>();

  public static Integer getCounterAndIncrement(DynamicContentSegment dynamicContentSegment, int loopNestingLevel) {
    if (loopNestingLevel < 1) {
      return null;
    }
    Integer current = counters.get(dynamicContentSegment);
    if (current == null) {
      current = 0;
    }
    int value = current;
    counters.put(dynamicContentSegment, current + 1);
    return value;
  }

  private String name;
  private Integer index;
  protected Object value;

  public ParameterInstance(String name, Integer index, Object value) {
    this.name = name;
    this.index = index;
    this.value = value;
  }

  public String getName() {
    return this.index == null ? this.name : this.name + "#" + this.index;
  }

  public Object getValue() {
    return this.value;
  }

  public abstract void applyTo(PreparedStatement ps, int ordinal) throws SQLException;

}
