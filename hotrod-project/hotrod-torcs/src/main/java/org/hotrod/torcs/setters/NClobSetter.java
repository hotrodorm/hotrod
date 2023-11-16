package org.hotrod.torcs.setters;

import java.io.Reader;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NClobSetter extends Setter {

  private int type;
  private NClob value;
  private Reader reader;
  private long length;

//void  setNClob(int parameterIndex, NClob value)
//void  setNClob(int parameterIndex, Reader reader)
//void  setNClob(int parameterIndex, Reader reader, long length)

  public NClobSetter(int index, NClob value) {
    super(index);
    this.type = 1;
    this.value = value;
  }

  public NClobSetter(int index, Reader reader) {
    super(index);
    this.type = 2;
    this.reader = reader;
  }

  public NClobSetter(int index, Reader reader, long length) {
    super(index);
    this.type = 3;
    this.reader = reader;
    this.length = length;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    if (this.type == 1) {
      ps.setNClob(this.index, this.value);
    } else if (this.type == 2) {
      ps.setNClob(this.index, this.reader);
    } else {
      ps.setNClob(this.index, this.reader, this.length);
    }
  }

  @Override
  public Object value() {
    return this.value;
  }

  @Override
  public String guessSQLServerDataType() {
    return "varchar";
  }

}
