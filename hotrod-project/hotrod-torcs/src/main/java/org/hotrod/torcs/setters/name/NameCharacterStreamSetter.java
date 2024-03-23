package org.hotrod.torcs.setters.name;

import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.SQLException;

import org.hotrod.torcs.setters.index.CouldNotToGuessDataTypeException;

public class NameCharacterStreamSetter extends NameSetter {

  private int type;
  private Reader reader;
  private int ilength;
  private long llength;

  public NameCharacterStreamSetter(String name, Reader reader) {
    super(name);
    this.type = 1;
    this.reader = reader;
  }

  public NameCharacterStreamSetter(String name, Reader reader, int length) {
    super(name);
    this.type = 2;
    this.reader = reader;
    this.ilength = length;
  }

  public NameCharacterStreamSetter(String name, Reader reader, long length) {
    super(name);
    this.type = 3;
    this.reader = reader;
    this.llength = length;
  }

  @Override
  public boolean isConsumableParameter() {
    return false;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    if (this.type == 1) {
      cs.setCharacterStream(this.name, this.reader);
    } else if (this.type == 2) {
      cs.setCharacterStream(this.name, this.reader, this.ilength);
    } else {
      cs.setCharacterStream(this.name, this.reader, this.llength);
    }
  }

  @Override
  public Object value() {
    return this.reader;
  }

  @Override
  public String guessSQLServerDataType() throws CouldNotToGuessDataTypeException {
    throw new CouldNotToGuessDataTypeException();
  }

}
