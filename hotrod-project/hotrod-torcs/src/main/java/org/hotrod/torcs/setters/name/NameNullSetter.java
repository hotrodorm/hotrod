package org.hotrod.torcs.setters.name;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.hotrod.torcs.setters.index.CouldNotToGuessDataTypeException;

public class NameNullSetter extends NameSetter {

  private int type;
  private int sqlType;
  private String typeName;

  public NameNullSetter(String name, int sqlType) {
    super(name);
    this.type = 1;
    this.sqlType = sqlType;
  }

  public NameNullSetter(String name, int sqlType, String typeName) {
    super(name);
    this.type = 2;
    this.sqlType = sqlType;
    this.typeName = typeName;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    if (this.type == 1) {
      cs.setNull(this.name, this.sqlType);
    } else {
      cs.setNull(this.name, this.sqlType, this.typeName);
    }
  }

  @Override
  public Object value() {
    return this.sqlType;
  }

  @Override
  public String guessSQLServerDataType() throws CouldNotToGuessDataTypeException {
    throw new CouldNotToGuessDataTypeException();
  }

}
