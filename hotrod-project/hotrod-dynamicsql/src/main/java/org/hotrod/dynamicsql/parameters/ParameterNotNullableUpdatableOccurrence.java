package org.hotrod.dynamicsql.parameters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.utils.ConverterUtil;

public class ParameterNotNullableUpdatableOccurrence extends ParameterOccurrence {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ParameterNotNullableUpdatableOccurrence.class.getName());

  public ParameterNotNullableUpdatableOccurrence(String originalParameterName, Integer index, Object value,
      TypeConverter<?, ?> converter) {
    super(originalParameterName, index, value, converter);
  }

  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  public void applyTo(PreparedStatement ps, int ordinal, Connection conn) throws SQLException {
    if (super.converter == null) {
      ps.setObject(ordinal, this.value);
    } else {
      Object raw = ConverterUtil.encode(super.value, super.converter, conn);
      ps.setObject(ordinal, raw);
    }
  }

}
