package org.hotrod.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import org.apache.log4j.Logger;

public final class JdbcUtils {

  private static Logger logger = Logger.getLogger(JdbcUtils.class);

  private JdbcUtils() {
  }

  public static Connection buildStandAloneConnection(final String dbDriverClassName, final String dbUrl,
      final String username, final String password) throws SQLException, ClassNotFoundException {

    logger.debug("dbDriverClassName=" + dbDriverClassName);
    Class.forName(dbDriverClassName);
    logger.debug("getConnection 4. dbUrl=" + dbUrl);
    logger.debug("getConnection 5. username=" + username);
    logger.debug("getConnection 6. password=" + password);
    Connection conn = DriverManager.getConnection(dbUrl, username, password);
    logger.debug("Succesfull database connection.");
    return conn;
  }

  public static void closeDbResources(final Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        logger.error("Cannot close connection.", e);
      }
    }
  }

  public static void closeDbResources(final Statement s) {
    if (s != null) {
      try {
        s.close();
      } catch (SQLException e) {
        logger.error("Cannot close Statement.", e);
      }
    }
  }

  public static void closeDbResources(final Connection conn, /* */
      final Statement s) {
    if (s != null) {
      try {
        s.close();
      } catch (SQLException e) {
        logger.error("Cannot close Statement.", e);
      }
    }
    closeDbResources(conn);
  }

  public static void closeDbResources(final Connection conn, final Statement s, final ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        logger.error("Cannot close ResultSet.", e);
      }
    }
    closeDbResources(conn, s);
  }

  public static void closeDbResources(final Statement s, final ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        logger.error("Cannot close ResultSet.", e);
      }
    }
    closeDbResources(s);
  }

  public static void closeDbResources(final ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        logger.error("Cannot close ResultSet.", e);
      }
    }
  }

  public static Long getLongObj(final ResultSet rs, final int col) throws SQLException {
    long value = rs.getLong(col);
    if (rs.wasNull()) {
      return null;
    } else {
      return new Long(value);
    }
  }

  public static long getLong(final ResultSet rs, final int col) throws SQLException {
    long value = rs.getLong(col);
    if (rs.wasNull()) {
      throw new SQLException("Not null value expected on column " + col + ".");
    } else {
      return value;
    }
  }

  public static Double getDoubleObj(final ResultSet rs, final int col) throws SQLException {
    double value = rs.getDouble(col);
    if (rs.wasNull()) {
      return null;
    } else {
      return new Double(value);
    }
  }

  public static double getDouble(final ResultSet rs, final int col) throws SQLException {
    double value = rs.getDouble(col);
    if (rs.wasNull()) {
      throw new SQLException("Not null value expected on column " + col + ".");
    } else {
      return value;
    }
  }

  public static Integer getIntObj(final ResultSet rs, final int col) throws SQLException {
    int value = rs.getInt(col);
    if (rs.wasNull()) {
      return null;
    } else {
      return new Integer(value);
    }
  }

  public static int getInt(final ResultSet rs, final int col) throws SQLException {
    int value = rs.getInt(col);
    if (rs.wasNull()) {
      throw new SQLException("Not null value expected on column " + col + ".");
    } else {
      return value;
    }
  }

  public static Short getShortObj(final ResultSet rs, final int col) throws SQLException {
    short value = rs.getShort(col);
    if (rs.wasNull()) {
      return null;
    } else {
      return new Short(value);
    }
  }

  public static short getShort(final ResultSet rs, final int col) throws SQLException {
    short value = rs.getShort(col);
    if (rs.wasNull()) {
      throw new SQLException("Not null value expected on column " + col + ".");
    } else {
      return value;
    }
  }

  public static Boolean getBooleanObj(final ResultSet rs, final int col) throws SQLException {
    String value = rs.getString(col);
    if (rs.wasNull()) {
      return null;
    } else {
      return stringToBoolean(value);
    }
  }

  public static boolean getBoolean(final ResultSet rs, final int col) throws SQLException {
    String value = rs.getString(col);
    if (rs.wasNull()) {
      throw new SQLException("Not null value expected on column " + col + ".");
    } else {
      return stringToBoolean(value).booleanValue();
    }
  }

  public static Date getDate(final ResultSet rs, final int col) throws SQLException {
    Timestamp value = rs.getTimestamp(col);
    if (rs.wasNull()) {
      return null;
    } else {
      return new Date(value.getTime());
    }
  }

  public static String getString(final ResultSet rs, final int col) throws SQLException {
    String value = rs.getString(col);
    if (rs.wasNull()) {
      return null;
    } else {
      return value;
    }
  }

  public static Object getObject(final ResultSet rs, final int col) throws SQLException {
    Object value = rs.getObject(col);
    if (rs.wasNull()) {
      return null;
    } else {
      return value;
    }
  }

  public static void setLong(final PreparedStatement st, final int col, final Long value) throws SQLException {
    if (value == null) {
      st.setObject(col, null, Types.NUMERIC);
    } else {
      st.setObject(col, value, Types.NUMERIC);
    }
  }

  public static void setLong(final PreparedStatement st, final int col, final long value) throws SQLException {
    setLong(st, col, new Long(value));
  }

  public static void setDouble(final PreparedStatement st, final int col, final Double value) throws SQLException {
    if (value == null) {
      st.setObject(col, null, Types.NUMERIC);
    } else {
      st.setObject(col, value, Types.NUMERIC);
    }
  }

  public static void setDouble(final PreparedStatement st, final int col, final double value) throws SQLException {
    setDouble(st, col, new Double(value));
  }

  public static void setInt(final PreparedStatement st, final int col, final Integer value) throws SQLException {
    if (value == null) {
      st.setObject(col, null, Types.NUMERIC);
    } else {
      st.setObject(col, value, Types.NUMERIC);
    }
  }

  public static void setInt(final PreparedStatement st, final int col, final int value) throws SQLException {
    setInt(st, col, new Integer(value));
  }

  public static void setBoolean(final PreparedStatement st, final int col, final Boolean value) throws SQLException {
    if (value == null) {
      st.setObject(col, null, Types.VARCHAR);
    } else {
      st.setObject(col, booleanToString(value), Types.VARCHAR);
    }
  }

  public static void setBoolean(final PreparedStatement st, final int col, final boolean value) throws SQLException {
    setBoolean(st, col, new Boolean(value));
  }

  public static void setDate(final PreparedStatement st, final int col, final Date value) throws SQLException {
    if (value == null) {
      st.setObject(col, null, Types.TIMESTAMP);
    } else {
      st.setObject(col, new Timestamp(value.getTime()), Types.TIMESTAMP);
    }
  }

  public static void setString(final PreparedStatement st, final int col, final String txt) throws SQLException {
    if (txt == null) {
      st.setObject(col, null, Types.VARCHAR);
    } else {
      st.setObject(col, txt, Types.VARCHAR);
    }
  }

  public static String booleanToString(final Boolean value) {
    if (value == null) {
      return null;
    } else if (value.booleanValue()) {
      return "T";
    } else {
      return "F";
    }
  }

  public static Boolean stringToBoolean(final String value) {
    if (value == null) {
      return null;
    } else if (value.equals("T")) {
      return new Boolean(true);
    } else {
      return new Boolean(false);
    }
  }

  public static String replaceNull(final String s, final String valueIfNull) {
    if (s == null) {
      return valueIfNull;
    } else {
      return s;
    }
  }

  public static Date replaceNull(final Date d, final Date valueIfNull) {
    if (d == null) {
      return valueIfNull;
    } else {
      return d;
    }
  }

  public static Boolean replaceNull(final Boolean b, /* */
      final Boolean valueIfNull) {
    if (b == null) {
      return valueIfNull;
    } else {
      return b;
    }
  }

  public static Long replaceNull(final Long l, final Long valueIfNull) {
    if (l == null) {
      return valueIfNull;
    } else {
      return l;
    }
  }

  public static Integer replaceNull(final Integer i, /* */
      final Integer valueIfNull) {
    if (i == null) {
      return valueIfNull;
    } else {
      return i;
    }
  }

  public static Double replaceNull(final Double d, final Double valueIfNull) {
    if (d == null) {
      return valueIfNull;
    } else {
      return d;
    }
  }

  public static boolean unwrap(final Boolean b, final boolean valueIfNull) {
    if (b == null) {
      return valueIfNull;
    } else {
      return b.booleanValue();
    }
  }

  public static long unwrap(final Long l, final long valueIfNull) {
    if (l == null) {
      return valueIfNull;
    } else {
      return l.longValue();
    }
  }

  public static int unwrap(final Integer i, final int valueIfNull) {
    if (i == null) {
      return valueIfNull;
    } else {
      return i.intValue();
    }
  }

  public static double unwrap(final Double d, final double valueIfNull) {
    if (d == null) {
      return valueIfNull;
    } else {
      return d.doubleValue();
    }
  }

  public static String standarizeIdentifier(final String ident) {
    return ident.trim();
  }

}
