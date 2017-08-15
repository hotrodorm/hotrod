package org.hotrod.utils;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.hotrod.runtime.util.SUtils;

public class ValueTypeFactory {

  public static interface ValueTypeManager<T> {

    T getFromResultSet(ResultSet rs, int columnIndex) throws SQLException;

    String renderJavaValue(Object obj);

    String getValueClassName();

  }

  public static ValueTypeManager<?> getValueManager(final String valueType) {
    return VALID_VALUE_TYPES.get(valueType);
  }

  public static Set<String> getSupportedTypes() {
    return VALID_VALUE_TYPES.keySet();
  }

  private static Map<String, ValueTypeManager<?>> VALID_VALUE_TYPES = new LinkedHashMap<String, ValueTypeManager<?>>();
  static {
    VALID_VALUE_TYPES.put(Byte.class.getName(), new ValueTypeManager<Byte>() {
      @Override
      public Byte getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        byte v = rs.getByte(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "(byte) " + obj;
      }

      @Override
      public String getValueClassName() {
        return Byte.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(Short.class.getName(), new ValueTypeManager<Short>() {
      @Override
      public Short getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        short v = rs.getShort(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "(short) " + obj;
      }

      @Override
      public String getValueClassName() {
        return Short.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(Integer.class.getName(), new ValueTypeManager<Integer>() {
      @Override
      public Integer getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        int v = rs.getInt(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "" + obj;
      }

      @Override
      public String getValueClassName() {
        return Integer.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(Long.class.getName(), new ValueTypeManager<Long>() {
      @Override
      public Long getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        long v = rs.getLong(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "" + obj + "L";
      }

      @Override
      public String getValueClassName() {
        return Long.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(Double.class.getName(), new ValueTypeManager<Double>() {
      @Override
      public Double getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        double v = rs.getDouble(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "" + obj;
      }

      @Override
      public String getValueClassName() {
        return Double.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(Float.class.getName(), new ValueTypeManager<Float>() {
      @Override
      public Float getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        float v = rs.getFloat(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "" + obj + "f";
      }

      @Override
      public String getValueClassName() {
        return Float.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(BigDecimal.class.getName(), new ValueTypeManager<BigDecimal>() {
      @Override
      public BigDecimal getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        BigDecimal v = rs.getBigDecimal(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "new java.math.BigDecimal(\"" + obj.toString() + "\")";
      }

      @Override
      public String getValueClassName() {
        return BigDecimal.class.getName();
      }

    });

    VALID_VALUE_TYPES.put(String.class.getName(), new ValueTypeManager<String>() {
      @Override
      public String getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        String v = rs.getString(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "\"" + SUtils.escapeJavaString((String) obj) + "\"";
      }

      @Override
      public String getValueClassName() {
        return String.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(Boolean.class.getName(), new ValueTypeManager<Boolean>() {
      @Override
      public Boolean getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        boolean v = rs.getBoolean(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "" + obj;
      }

      @Override
      public String getValueClassName() {
        return Boolean.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(java.sql.Date.class.getName(), new ValueTypeManager<java.sql.Date>() {
      @Override
      public java.sql.Date getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        Date v = rs.getDate(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "java.sql.Date.valueOf(\"" + obj.toString() + "\")";
      }

      @Override
      public String getValueClassName() {
        return java.sql.Date.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(java.sql.Timestamp.class.getName(), new ValueTypeManager<java.sql.Timestamp>() {
      @Override
      public java.sql.Timestamp getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp v = rs.getTimestamp(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "java.sql.Timestamp.valueOf(\"" + obj.toString() + "\")";
      }

      @Override
      public String getValueClassName() {
        return java.sql.Timestamp.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(java.util.Date.class.getName(), new ValueTypeManager<java.util.Date>() {
      @Override
      public java.util.Date getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp v = rs.getTimestamp(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJavaValue(final Object obj) {
        java.util.Date d = (java.util.Date) obj;
        return obj == null ? "null" : "new java.util.Date(" + d.getTime() + "L)";
      }

      @Override
      public String getValueClassName() {
        return java.util.Date.class.getName();
      }

    });
  }

}
