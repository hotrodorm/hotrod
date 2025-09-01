package db.maven.plugin.modules.datetimejdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

public class AbstractTest {

  public static final List<DateTime<Timestamp>> LIST_TIMESTAMP = Arrays.asList( //
      new DateTime<>(Timestamp.valueOf(LocalDateTime.of(2020, 1, 2, 12, 34, 56))) //
  );

  public static final List<DateTime<LocalDateTime>> LIST_LOCALDATETIME = Arrays.asList( //
      new DateTime<>(LocalDateTime.of(2020, 1, 2, 12, 34, 56)) //
  );

  public static final List<DateTime<OffsetDateTime>> LIST_OFFSETDATETIME = Arrays.asList( //
      new DateTime<>(OffsetDateTime.of(2020, 1, 2, 12, 34, 56, 0, ZoneOffset.ofHoursMinutes(-3, -45))) //
  );

  public static final List<DateTime<ZonedDateTime>> LIST_ZONEDDATETIME = Arrays.asList( //
      new DateTime<>(ZonedDateTime.of(2020, 1, 2, 12, 34, 56, 0, ZoneId.of("America/New_York"))), //
      new DateTime<>(ZonedDateTime.of(2020, 1, 2, 12, 34, 56, 0, ZoneId.of("Africa/Nairobi"))), //
      new DateTime<>(ZonedDateTime.of(2020, 1, 2, 12, 34, 56, 0, ZoneId.of("UTC+01:00"))) //
  );

  protected void persistTimestamp(final Connection conn, final String dbType, final String javaType, final String col,
      List<DateTime<Timestamp>> list) {

    System.out.println();
    System.out.println("=== Using set/getTimestamp( " + javaType + " ) on a " + dbType + " column ===");

    try {
      {
        PreparedStatement ps = conn.prepareStatement("delete from t1");
        ps.execute();
        ps.close();
      }

      {
        String sql = "insert into t1 (id, " + col + ") values (?, ?)";
        //        System.out.println("sql: " + sql);
        PreparedStatement ps = conn.prepareStatement(sql);
        int id = 1;
        for (DateTime<Timestamp> e : list) {
          Timestamp value = e.getValue();
          ps.setInt(1, id);
          ps.setTimestamp(2, value);
          ps.execute();
          System.out.println(" - Inserted  #" + id + ": " + renderValue(value));
          id++;
        }
        ps.close();

      }

      {
        PreparedStatement ps = conn.prepareStatement("select id, " + col + " from t1 order by id");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          int id = rs.getInt(1);
          Object a = rs.getTimestamp(2);
          System.out.println(" - Retrieved #" + id + ": " + renderValue(a));
        }
        rs.close();
        ps.close();
      }
    } catch (Exception e) {
      System.out.println("ERROR " + e.getClass().getSimpleName() + ": " + e.getMessage());
      //      e.printStackTrace();
      sleep(100);
    }

  }

  private void sleep(final long delay) {
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }
  }

  protected <T> void persistObject(final Connection conn, final String dbType, final String javaType, final String col,
      List<DateTime<T>> list) {

    System.out.println();
    System.out.println("=== Using set/getObject( " + javaType + " ) on a " + dbType + " column ===");

    Class<?> klass = list.get(0).getValue().getClass();

    try {
      {
        PreparedStatement ps = conn.prepareStatement("delete from t1");
        ps.execute();
        ps.close();
      }

      {
        String sql = "insert into t1 (id, " + col + ") values (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        int id = 1;
        for (DateTime<T> e : list) {
          T value = e.getValue();
          ps.setInt(1, id);
          ps.setObject(2, value);
          ps.execute();
          System.out.println(" - Inserted  #" + id + ": " + renderValue(value));
          id++;
        }
        ps.close();

      }

      {
        PreparedStatement ps = conn.prepareStatement("select id, " + col + " from t1 order by id");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
          int id = rs.getInt(1);
          Object a = rs.getObject(2, klass);
          System.out.println(" - Retrieved #" + id + ": " + renderValue(a));
        }
        rs.close();
        ps.close();
      }
    } catch (Exception e) {
      System.out.println("ERROR " + e.getClass().getSimpleName() + ": " + e.getMessage());
      //      e.printStackTrace();
      sleep(100);
    }

  }

  private static String renderValue(final Object value) {
    return "" + value + (value == null ? "" : " (" + value.getClass() + ")");
  }

  public static class DateTime<T> {

    private T value;

    public DateTime(final T value) {
      this.value = value;
    }

    public T getValue() {
      return value;
    }

    public void setValue(final T value) {
      this.value = value;
    }

  }

}
