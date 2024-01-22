package app;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

import org.hotrod.runtime.converter.TypeConverter;

public class OffSetDateTimeConverter implements TypeConverter<Object, OffsetDateTime> {

  // Decoding is used when reading from the database

  // LiveSQL: dat2 (TIMESTAMP): 2024-01-02 12:34:56.123457 (oracle.sql.TIMESTAMP)

  @Override
  public OffsetDateTime decode(Object raw, Connection conn)  {
    System.out.println(">>> raw=" + raw + " (" + (raw == null ? "null" : raw.getClass().getName()) + ")");
    if (raw == null) {
      return null;
    }
    oracle.sql.TIMESTAMP ot = (oracle.sql.TIMESTAMP) raw;
    Timestamp ts;
    try {
      ts = ot.timestampValue();
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
    System.out.println(">>> ts=" + ts);
    OffsetDateTime now = OffsetDateTime.now();
    
//    ts.valueOf(dateTime);
    return null;
  }

  // Encoding is used when writing to the database

  @Override
  public Object encode(OffsetDateTime value, Connection conn) {
    if (value == null) {
      return null;
    }
    return null;
  }

}