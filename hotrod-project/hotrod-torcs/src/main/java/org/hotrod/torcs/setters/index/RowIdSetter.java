package org.hotrod.torcs.setters.index;

import java.sql.PreparedStatement;
import java.sql.RowId;
import java.sql.SQLException;

public class RowIdSetter extends IndexSetter {

  private RowId x;

//void  setRowId(int parameterIndex, RowId x)

  public RowIdSetter(int index, RowId x) {
    super(index);
    this.x = x;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setRowId(this.index, this.x);
  }

  @Override
  public Object value() {
    return this.x;
  }

  @Override
  public String guessSQLServerDataType() throws DataTypeNotImplementedException {
    throw new DataTypeNotImplementedException();
  }

}
