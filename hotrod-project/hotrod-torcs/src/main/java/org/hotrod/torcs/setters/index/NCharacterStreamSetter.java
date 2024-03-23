package org.hotrod.torcs.setters.index;

import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NCharacterStreamSetter extends IndexSetter {

  private int type;
  private Reader reader;
  private long length;

//void  setNCharacterStream(int parameterIndex, Reader value)
//void  setNCharacterStream(int parameterIndex, Reader value, long length)

  public NCharacterStreamSetter(int index, Reader reader) {
    super(index);
    this.type = 1;
    this.reader = reader;
  }

  public NCharacterStreamSetter(int index, Reader reader, long length) {
    super(index);
    this.type = 2;
    this.reader = reader;
    this.length = length;
  }

  @Override
  public boolean isConsumableParameter() {
    return true;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    if (this.type == 1) {
      ps.setCharacterStream(this.index, this.reader);
    } else {
      ps.setCharacterStream(this.index, this.reader, this.length);
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
