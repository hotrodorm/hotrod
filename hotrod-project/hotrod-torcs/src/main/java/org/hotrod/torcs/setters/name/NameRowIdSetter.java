package org.hotrod.torcs.setters.name;

import java.sql.CallableStatement;
import java.sql.RowId;
import java.sql.SQLException;

import org.hotrod.torcs.setters.index.DataTypeNotImplementedException;

public class NameRowIdSetter extends NameSetter {

  private RowId x;

  public NameRowIdSetter(String name, RowId x) {
    super(name);
    this.x = x;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    cs.setRowId(this.name, this.x);
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
