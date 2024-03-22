package org.hotrod.torcs.setters.index;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class URLSetter extends IndexSetter {

  private URL x;

//void  setURL(int parameterIndex, URL x)

  public URLSetter(int index, URL x) {
    super(index);
    this.x = x;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setURL(this.index, this.x);
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
