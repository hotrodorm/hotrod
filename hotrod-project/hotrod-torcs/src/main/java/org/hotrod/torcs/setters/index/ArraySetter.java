package org.hotrod.torcs.setters.index;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArraySetter extends IndexSetter {

  private Array value;

  public ArraySetter(int index, Array value) {
    super(index);
    this.value = value;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setArray(this.index, this.value);
  }

  @Override
  public Object value() {
    return this.value;
  }

  @Override
  public String guessSQLServerDataType() throws DataTypeNotImplementedException {
    throw new DataTypeNotImplementedException();
  }

}
