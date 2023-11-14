package org.hotrod.torcs.setters;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AsciiStreamSetter extends Setter {

  private int type;
  private InputStream x;
  private int ilength;
  private long llength;

//void  setAsciiStream(int parameterIndex, InputStream x)
//void  setAsciiStream(int parameterIndex, InputStream x, int length)
//void  setAsciiStream(int parameterIndex, InputStream x, long length)

  public AsciiStreamSetter(int index, InputStream value) {
    super(index);
    this.type = 1;
    this.x = value;
  }

  public AsciiStreamSetter(int index, InputStream value, int length) {
    super(index);
    this.type = 2;
    this.x = value;
    this.ilength = length;
  }

  public AsciiStreamSetter(int index, InputStream value, long length) {
    super(index);
    this.type = 3;
    this.x = value;
    this.llength = length;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    if (this.type == 1) {
      ps.setAsciiStream(this.index, this.x);
    } else if (this.type == 2) {
      ps.setAsciiStream(this.index, this.x, this.ilength);
    } else {
      ps.setAsciiStream(this.index, this.x, this.llength);
    }
  }

  @Override
  public Object value() {
    return this.x;
  }

}
