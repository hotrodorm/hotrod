package org.hotrod.torcs.setters.name;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.SQLException;

import org.hotrod.torcs.setters.index.CouldNotToGuessDataTypeException;

public class NameBinaryStreamSetter extends NameSetter {

  private int type;
  private InputStream value;
  private int ilength;
  private long llength;

  public NameBinaryStreamSetter(String name, InputStream value) {
    super(name);
    this.type = 1;
    this.value = value;
  }

  public NameBinaryStreamSetter(String name, InputStream value, int length) {
    super(name);
    this.type = 2;
    this.value = value;
    this.ilength = length;
  }

  public NameBinaryStreamSetter(String name, InputStream value, long length) {
    super(name);
    this.type = 3;
    this.value = value;
    this.llength = length;
  }

  @Override
  public boolean isConsumableParameter() {
    return false;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    if (this.type == 1) {
      cs.setBinaryStream(this.name, this.value, this.ilength);
    } else if (this.type == 2) {
      cs.setBinaryStream(this.name, this.value, this.llength);
    } else {
      cs.setBinaryStream(this.name, this.value);
    }
  }

  @Override
  public Object value() {
    return this.value;
  }

  @Override
  public String guessSQLServerDataType() throws CouldNotToGuessDataTypeException {
    throw new CouldNotToGuessDataTypeException();
  }

}
