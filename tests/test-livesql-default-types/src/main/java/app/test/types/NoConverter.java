package app.test.types;

import java.sql.Connection;
import java.sql.SQLException;

public class NoConverter<A> implements ValueConverter<A, A> {

  @Override
  public A decode(A raw, Connection conn) throws SQLException {
    return raw;
  }

  @Override
  public A encode(A value, Connection conn) throws SQLException {
    return value;
  }

}
