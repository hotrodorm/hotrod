package org.hotrod.dynamicsql.parameters;

import java.sql.Connection;

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
import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;

public abstract class ParameterOccurrence {

  private static final Logger log = Logger.getLogger(ParameterOccurrence.class.getName());

  private String name;
  private Integer index;
  protected Object value;
  protected TypeConverter<?, ?> converter;

  protected ParameterOccurrence(String name, Integer index, Object value, TypeConverter<?, ?> converter) {
    log.fine("init");
    this.name = name;
    this.index = index;
    this.value = value;
    this.converter = converter;
  }

  public String getName() {
    return this.index == null ? this.name : this.name + "#" + this.index;
  }

  public Object getValue() {
    return this.value;
  }

  public abstract void applyTo(PreparedStatement ps, int ordinal, Connection conn) throws SQLException;

}
