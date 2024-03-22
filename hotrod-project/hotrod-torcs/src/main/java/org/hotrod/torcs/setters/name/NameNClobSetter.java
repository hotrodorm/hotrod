package org.hotrod.torcs.setters.name;

import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.NClob;
import java.sql.SQLException;

public class NameNClobSetter extends NameSetter {

  private int type;
  private NClob value;
  private Reader reader;
  private long length;

  public NameNClobSetter(String name, NClob value) {
    super(name);
    this.type = 1;
    this.value = value;
  }

  public NameNClobSetter(String name, Reader reader) {
    super(name);
    this.type = 2;
    this.reader = reader;
  }

  public NameNClobSetter(String name, Reader reader, long length) {
    super(name);
    this.type = 3;
    this.reader = reader;
    this.length = length;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    if (this.type == 1) {
      cs.setNClob(this.name, this.value);
    } else if (this.type == 2) {
      cs.setNClob(this.name, this.reader);
    } else {
      cs.setNClob(this.name, this.reader, this.length);
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
