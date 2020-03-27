package org.hotrod.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.hotrodorm.hotrod.utils.SUtil;

public class ValueTypeFactory implements Serializable {

  private static final long serialVersionUID = 1L;

  public static interface ValueTypeManager<T> extends Serializable {

    T getFromResultSet(ResultSet rs, int columnIndex) throws SQLException;

    String renderJdbcGetter(String jdbcObject, String jdbcParameter);

    String renderJdbcSetter(String jdbcObject, String columnIndex, String jdbcParameter, String transientVar);

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

      private static final long serialVersionUID = 1L;

      @Override
      public Byte getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        byte v = rs.getByte(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJdbcGetter(final String jdbcObject, final String jdbcParameter) {
        return jdbcObject + ".getByte(" + jdbcParameter + ")";
      }

      @Override
      public String renderJdbcSetter(final String jdbcObject, final String columnIndex, final String jdbcParameter,
          final String transientVar) {
        return jdbcObject + ".setByte(" + columnIndex + ", " + jdbcParameter + ");";
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

      private static final long serialVersionUID = 1L;

      @Override
      public Short getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        short v = rs.getShort(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJdbcGetter(final String jdbcObject, final String jdbcParameter) {
        return jdbcObject + ".getShort(" + jdbcParameter + ")";
      }

      @Override
      public String renderJdbcSetter(final String jdbcObject, final String columnIndex, final String jdbcParameter,
          final String transientVar) {
        return jdbcObject + ".setShort(" + columnIndex + ", " + jdbcParameter + ");";
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

      private static final long serialVersionUID = 1L;

      @Override
      public Integer getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        int v = rs.getInt(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJdbcGetter(final String jdbcObject, final String jdbcParameter) {
        return jdbcObject + ".getInt(" + jdbcParameter + ")";
      }

      @Override
      public String renderJdbcSetter(final String jdbcObject, final String columnIndex, final String jdbcParameter,
          final String transientVar) {
        return jdbcObject + ".setInt(" + columnIndex + ", " + jdbcParameter + ");";
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

      private static final long serialVersionUID = 1L;

      @Override
      public Long getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        long v = rs.getLong(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJdbcGetter(final String jdbcObject, final String jdbcParameter) {
        return jdbcObject + ".getLong(" + jdbcParameter + ")";
      }

      @Override
      public String renderJdbcSetter(final String jdbcObject, final String columnIndex, final String jdbcParameter,
          final String transientVar) {
        return jdbcObject + ".setLong(" + columnIndex + ", " + jdbcParameter + ");";
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

      private static final long serialVersionUID = 1L;

      @Override
      public Double getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        double v = rs.getDouble(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJdbcGetter(final String jdbcObject, final String jdbcParameter) {
        return jdbcObject + ".getDouble(" + jdbcParameter + ")";
      }

      @Override
      public String renderJdbcSetter(final String jdbcObject, final String columnIndex, final String jdbcParameter,
          final String transientVar) {
        return jdbcObject + ".setDouble(" + columnIndex + ", " + jdbcParameter + ");";
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

      private static final long serialVersionUID = 1L;

      @Override
      public Float getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        float v = rs.getFloat(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJdbcGetter(final String jdbcObject, final String jdbcParameter) {
        return jdbcObject + ".getFloat(" + jdbcParameter + ")";
      }

      @Override
      public String renderJdbcSetter(final String jdbcObject, final String columnIndex, final String jdbcParameter,
          final String transientVar) {
        return jdbcObject + ".setFloat(" + columnIndex + ", " + jdbcParameter + ");";
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

      private static final long serialVersionUID = 1L;

      @Override
      public BigDecimal getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        BigDecimal v = rs.getBigDecimal(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJdbcGetter(final String jdbcObject, final String jdbcParameter) {
        return jdbcObject + ".getBigDecimal(" + jdbcParameter + ")";
      }

      @Override
      public String renderJdbcSetter(final String jdbcObject, final String columnIndex, final String jdbcParameter,
          final String transientVar) {
        return jdbcObject + ".setBigDecimal(" + columnIndex + ", " + jdbcParameter + ");";
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

      private static final long serialVersionUID = 1L;

      @Override
      public String getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        String v = rs.getString(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJdbcGetter(final String jdbcObject, final String jdbcParameter) {
        return jdbcObject + ".getString(" + jdbcParameter + ")";
      }

      @Override
      public String renderJdbcSetter(final String jdbcObject, final String columnIndex, final String jdbcParameter,
          final String transientVar) {
        return jdbcObject + ".setString(" + columnIndex + ", " + jdbcParameter + ");";
      }

      @Override
      public String renderJavaValue(final Object obj) {
        return obj == null ? "null" : "\"" + SUtil.escapeJavaString((String) obj) + "\"";
      }

      @Override
      public String getValueClassName() {
        return String.class.getName();
      }

    });
    VALID_VALUE_TYPES.put(Boolean.class.getName(), new ValueTypeManager<Boolean>() {

      private static final long serialVersionUID = 1L;

      @Override
      public Boolean getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        boolean v = rs.getBoolean(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJdbcGetter(final String jdbcObject, final String jdbcParameter) {
        return jdbcObject + ".getBoolean(" + jdbcParameter + ")";
      }

      @Override
      public String renderJdbcSetter(final String jdbcObject, final String columnIndex, final String jdbcParameter,
          final String transientVar) {
        return jdbcObject + ".setBoolean(" + columnIndex + ", " + jdbcParameter + ");";
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

      private static final long serialVersionUID = 1L;

      @Override
      public java.sql.Date getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        Date v = rs.getDate(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJdbcGetter(final String jdbcObject, final String jdbcParameter) {
        return jdbcObject + ".getDate(" + jdbcParameter + ")";
      }

      @Override
      public String renderJdbcSetter(final String jdbcObject, final String columnIndex, final String jdbcParameter,
          final String transientVar) {
        return jdbcObject + ".setDate(" + columnIndex + ", " + jdbcParameter + ");";
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

      private static final long serialVersionUID = 1L;

      @Override
      public java.sql.Timestamp getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp v = rs.getTimestamp(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJdbcGetter(final String jdbcObject, final String jdbcParameter) {
        return jdbcObject + ".getTimestamp(" + jdbcParameter + ")";
      }

      @Override
      public String renderJdbcSetter(final String jdbcObject, final String columnIndex, final String jdbcParameter,
          final String transientVar) {
        return jdbcObject + ".setTimestamp(" + columnIndex + ", " + jdbcParameter + ");";
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

      private static final long serialVersionUID = 1L;

      @Override
      public java.util.Date getFromResultSet(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp v = rs.getTimestamp(columnIndex);
        return rs.wasNull() ? null : v;
      }

      @Override
      public String renderJdbcGetter(final String jdbcObject, final String jdbcParameter) {
        return jdbcObject + ".getTimestamp(" + jdbcParameter + ")";
      }

      @Override
      public String renderJdbcSetter(final String jdbcObject, final String columnIndex, final String jdbcParameter,
          final String transientVar) {
        return jdbcObject + ".setTimestamp(" + columnIndex + ", new java.sql.Timestamp(" + jdbcParameter
            + ".getTime()));";
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
