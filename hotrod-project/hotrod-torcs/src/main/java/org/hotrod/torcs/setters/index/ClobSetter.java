package org.hotrod.torcs.setters.index;

import java.io.Reader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClobSetter extends IndexSetter {

  private int type;
  private Clob x;
  private Reader reader;
  private long length;

//void  setClob(int parameterIndex, Clob x)
//void  setClob(int parameterIndex, Reader reader)
//void  setClob(int parameterIndex, Reader reader, long length)

  public ClobSetter(int index, Clob x) {
    super(index);
    this.type = 1;
    this.x = x;
  }

  public ClobSetter(int index, Reader reader) {
    super(index);
    this.type = 2;
    this.x = null;
    this.reader = reader;
  }

  public ClobSetter(int index, Reader reader, long length) {
    super(index);
    this.type = 3;
    this.x = null;
    this.reader = reader;
    this.length = length;
  }

  @Override
  public boolean isLOBParameter() {
    return true;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    if (this.type == 1) {
      ps.setClob(this.index, this.x);
    } else if (this.type == 2) {
      ps.setClob(this.index, this.reader);
    } else {
      ps.setClob(this.index, this.reader, this.length);
    }
  }

  @Override
  public Object value() {
    return this.x;
  }

  @Override
  public String guessSQLServerDataType() {
    return "varchar";
  }

}
