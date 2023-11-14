package org.hotrod.torcs.setters;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class URLSetter extends Setter {

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

}
