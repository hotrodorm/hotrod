package org.hotrod.torcs.setters.index;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BinaryStreamSetter extends IndexSetter {

  private int type;
  private InputStream value;
  private int ilength;
  private long llength;

//void  setBinaryStream(int parameterIndex, InputStream x)
//void  setBinaryStream(int parameterIndex, InputStream x, int length)
//void  setBinaryStream(int parameterIndex, InputStream x, long length)

  public BinaryStreamSetter(int index, InputStream value) {
    super(index);
    this.type = 1;
    this.value = value;
  }

  public BinaryStreamSetter(int index, InputStream value, int length) {
    super(index);
    this.type = 2;
    this.value = value;
    this.ilength = length;
  }

  public BinaryStreamSetter(int index, InputStream value, long length) {
    super(index);
    this.type = 3;
    this.value = value;
    this.llength = length;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    if (this.type == 1) {
      ps.setBinaryStream(this.index, this.value, this.ilength);
    } else if (this.type == 2) {
      ps.setBinaryStream(this.index, this.value, this.llength);
    } else {
      ps.setBinaryStream(this.index, this.value);
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
