package org.hotrod.torcs.setters.index;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UnicodeStreamSetter extends IndexSetter {

  private InputStream x;
  private int length;

//void  setUnicodeStream(int parameterIndex, InputStream x, int length)

  public UnicodeStreamSetter(int index, InputStream x, int length) {
    super(index);
    this.x = x;
    this.length = length;
  }

  @Override
  public boolean isConsummableParameter() {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setUnicodeStream(this.index, this.x, this.length);
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
