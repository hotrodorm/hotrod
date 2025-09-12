package org.hotrod.dynamicsql.parameters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.hotrod.converter.TypeConverter;
import org.hotrod.utils.ConverterUtil;

public class ParameterNullableInstance extends ParameterInstance {

  @SuppressWarnings("unused")
  private static final Logger log = Logger.getLogger(ParameterNullableInstance.class.getName());

  private int sqlType;

  public ParameterNullableInstance(int sqlType, String originalParameterName, Integer index, Object value,
      TypeConverter<?, ?> converter) {
    super(originalParameterName, index, value, converter);
    this.sqlType = sqlType;
  }

  public int getSQLType() {
    return sqlType;
  }

  @Override
  public void applyTo(PreparedStatement ps, int ordinal, Connection conn) throws SQLException {
    if (super.converter == null) {
      if (super.value != null) {
        ps.setObject(ordinal, this.value);
      } else {
        ps.setNull(ordinal, this.sqlType);
      }
    } else {
      Object raw = ConverterUtil.encode(super.value, super.converter, conn);
      if (raw != null) {
        ps.setObject(ordinal, raw);
      } else {
        ps.setNull(ordinal, this.sqlType);
      }
    }
  }

}
