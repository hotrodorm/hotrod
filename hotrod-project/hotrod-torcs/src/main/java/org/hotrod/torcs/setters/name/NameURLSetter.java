package org.hotrod.torcs.setters.name;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.SQLException;

import org.hotrod.torcs.setters.index.DataTypeNotImplementedException;

public class NameURLSetter extends NameSetter {

  private URL x;

  public NameURLSetter(String name, URL x) {
    super(name);
    this.x = x;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    cs.setURL(this.name, this.x);
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
