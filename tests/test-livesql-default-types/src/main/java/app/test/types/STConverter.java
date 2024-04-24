package app.test.types;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class STConverter implements ValueConverter<String, LocalDateTime> {

  @Override
  public LocalDateTime decode(String raw, Connection conn) throws SQLException {
    return null;
  }

  @Override
  public String encode(LocalDateTime value, Connection conn) throws SQLException {
    return null;
  }

}
