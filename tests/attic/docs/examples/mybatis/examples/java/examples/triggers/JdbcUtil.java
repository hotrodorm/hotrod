package examples.triggers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

public final class JdbcUtil {

  private JdbcUtil() {
  }

  public static Connection buildStandAloneConnection(final String dbDriverClassName, final String dbUrl,
      final String username, final String password) throws SQLException, ClassNotFoundException {

    log("dbDriverClassName=" + dbDriverClassName);
    Class.forName(dbDriverClassName);
    log("getConnection 4. dbUrl=" + dbUrl);
    log("getConnection 5. username=" + username);
    log("getConnection 6. password=" + password);
    Connection conn = DriverManager.getConnection(dbUrl, username, password);
    log("Succesfull database connection.");
    return conn;
  }

  public static void closeDbResources(final Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        log("Cannot close connection.", e);
      }
    }
  }

  public static void closeDbResources(final Connection conn, /* */
      final Statement s) {
    if (s != null) {
      try {
        s.close();
      } catch (SQLException e) {
        log("Cannot close Statement.", e);
      }
    }
    closeDbResources(conn);
  }

  public static void closeDbResources(final Connection conn, final Statement s, final ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        log("Cannot close ResultSet.", e);
      }
    }
    closeDbResources(conn, s);
  }

  public static void closeDbResources(final ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        log("Cannot close ResultSet.", e);
      }
    }
  }

  public static void closeDbResources(final PreparedStatement st) {
    if (st != null) {
      try {
        st.close();
      } catch (SQLException e) {
        log("Cannot close ResultSet.", e);
      }
    }
  }

  public static void closeDbResources(final PreparedStatement st, final ResultSet rs) {
    try {
      JdbcUtil.closeDbResources(rs);
    } finally {
      JdbcUtil.closeDbResources(st);
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

  public static Date getUtilDate(final ResultSet rs, final int col) throws SQLException {
    Timestamp value = rs.getTimestamp(col);
    if (rs.wasNull()) {
      return null;
    } else {
      return new Date(value.getTime());
    }
  }

  public static Timestamp getTimestamp(final ResultSet rs, final int col) throws SQLException {
    Timestamp value = rs.getTimestamp(col);
    if (rs.wasNull()) {
      return null;
    } else {
      return value;
    }
  }

  public static java.sql.Date getSQLDate(final ResultSet rs, final int col) throws SQLException {
    java.sql.Date value = rs.getDate(col);
    if (rs.wasNull()) {
      return null;
    } else {
      return value;
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

  public static void setUtilDate(final PreparedStatement st, final int col, final java.util.Date value)
      throws SQLException {
    if (value == null) {
      st.setNull(col, Types.TIMESTAMP);
    } else {
      Timestamp ts = new Timestamp(value.getTime());
      st.setTimestamp(col, ts);
    }
  }

  public static void setSQLDate(final PreparedStatement st, final int col, final java.sql.Date value)
      throws SQLException {
    if (value == null) {
      st.setNull(col, Types.DATE);
    } else {
      st.setDate(col, value);
    }
  }

  public static void setTimestamp(final PreparedStatement st, final int col, final Timestamp value)
      throws SQLException {
    if (value == null) {
      st.setNull(col, Types.TIMESTAMP);
    } else {
      st.setTimestamp(col, value);
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

  private static void log(final String txt) {
    System.out.println("[" + new Object() {
    }.getClass().getEnclosingClass().getName() + "] " + txt);
  }

  private static void log(final String txt, final Throwable t) {
    System.out.println("[" + new Object() {
    }.getClass().getEnclosingClass().getName() + "] ERROR - " + txt + ":");
    t.printStackTrace();
  }

}
