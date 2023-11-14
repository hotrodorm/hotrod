package org.hotrod.torcs.setters;

import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CharacterStreamSetter extends Setter {

  private int type;
  private Reader reader;
  private int ilength;
  private long llength;

//void  setCharacterStream(int parameterIndex, Reader reader)
//void  setCharacterStream(int parameterIndex, Reader reader, int length)
//void  setCharacterStream(int parameterIndex, Reader reader, long length)

  public CharacterStreamSetter(int index, Reader reader) {
    super(index);
    this.type = 1;
    this.reader = reader;
  }

  public CharacterStreamSetter(int index, Reader reader, int length) {
    super(index);
    this.type = 2;
    this.reader = reader;
    this.ilength = length;
  }

  public CharacterStreamSetter(int index, Reader reader, long length) {
    super(index);
    this.type = 3;
    this.reader = reader;
    this.llength = length;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    if (this.type == 1) {
      ps.setCharacterStream(this.index, this.reader);
    } else if (this.type == 2) {
      ps.setCharacterStream(this.index, this.reader, this.ilength);
    } else {
      ps.setCharacterStream(this.index, this.reader, this.llength);
    }
  }

  @Override
  public Object value() {
    return this.reader;
  }

}
