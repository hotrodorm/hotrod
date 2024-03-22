package org.hotrod.torcs.setters.name;

import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.SQLException;

import org.hotrod.torcs.setters.index.CouldNotToGuessDataTypeException;

public class NameNCharacterStreamSetter extends NameSetter {

  private int type;
  private Reader reader;
  private long length;

  public NameNCharacterStreamSetter(String name, Reader reader) {
    super(name);
    this.type = 1;
    this.reader = reader;
  }

  public NameNCharacterStreamSetter(String name, Reader reader, long length) {
    super(name);
    this.type = 2;
    this.reader = reader;
    this.length = length;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    if (this.type == 1) {
      cs.setCharacterStream(this.name, this.reader);
    } else {
      cs.setCharacterStream(this.name, this.reader, this.length);
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
