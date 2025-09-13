package org.hotrod.dynamicsql.parameters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.utils.ConverterUtil;

public class VariableOccurrence extends ParameterOccurrence {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(VariableOccurrence.class.getName());

  public VariableOccurrence(String originalParameterName, Integer index, Object value, TypeConverter<?, ?> converter) {
    super(originalParameterName, index, value, converter);
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
