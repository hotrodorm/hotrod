package app.test.types;

import java.sql.Connection;
import java.sql.SQLException;

public interface ValueConverter<R, A> {

  A decode(R raw, Connection conn) throws SQLException; // used when reading from the database

  R encode(A value, Connection conn) throws SQLException; // used when writing to the database

}
