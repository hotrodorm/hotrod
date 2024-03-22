package org.hotrod.torcs.setters.name;

import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.SQLException;

public class NameClobSetter extends NameSetter {

  private int type;
  private Clob x;
  private Reader reader;
  private long length;

  public NameClobSetter(String name, Clob x) {
    super(name);
    this.type = 1;
    this.x = x;
  }

  public NameClobSetter(String name, Reader reader) {
    super(name);
    this.type = 2;
    this.x = null;
    this.reader = reader;
  }

  public NameClobSetter(String name, Reader reader, long length) {
    super(name);
    this.type = 3;
    this.x = null;
    this.reader = reader;
    this.length = length;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    if (this.type == 1) {
      cs.setClob(this.name, this.x);
    } else if (this.type == 2) {
      cs.setClob(this.name, this.reader);
    } else {
      cs.setClob(this.name, this.reader, this.length);
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
