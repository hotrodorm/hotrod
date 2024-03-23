package org.hotrod.torcs.setters.name;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.SQLException;

import org.hotrod.torcs.setters.index.CouldNotToGuessDataTypeException;

public class NameAsciiStreamSetter extends NameSetter {

  private int type;
  private InputStream x;
  private int ilength;
  private long llength;

  public NameAsciiStreamSetter(String name, InputStream value) {
    super(name);
    this.type = 1;
    this.x = value;
  }

  public NameAsciiStreamSetter(String name, InputStream value, int length) {
    super(name);
    this.type = 2;
    this.x = value;
    this.ilength = length;
  }

  public NameAsciiStreamSetter(String name, InputStream value, long length) {
    super(name);
    this.type = 3;
    this.x = value;
    this.llength = length;
  }

  @Override
  public boolean isConsummableParameter() {
    return false;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    if (this.type == 1) {
      cs.setAsciiStream(this.name, this.x);
    } else if (this.type == 2) {
      cs.setAsciiStream(this.name, this.x, this.ilength);
    } else {
      cs.setAsciiStream(this.name, this.x, this.llength);
    }
  }

  @Override
  public Object value() {
    return this.x;
  }

  @Override
  public String guessSQLServerDataType() throws CouldNotToGuessDataTypeException {
    throw new CouldNotToGuessDataTypeException();
  }

}
