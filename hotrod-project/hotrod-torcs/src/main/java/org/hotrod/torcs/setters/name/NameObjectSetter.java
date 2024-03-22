package org.hotrod.torcs.setters.name;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.SQLType;

import org.hotrod.torcs.setters.index.CouldNotToGuessDataTypeException;

public class NameObjectSetter extends NameSetter {

  private int type;
  private Object x;
  private int targetSqlType;
  private int scaleOrLength;
  private SQLType targetSqlType2;

  public NameObjectSetter(String name, Object x) {
    super(name);
    this.type = 1;
    this.x = x;
  }

  public NameObjectSetter(String name, Object x, int targetSqlType) {
    super(name);
    this.type = 2;
    this.x = x;
    this.targetSqlType = targetSqlType;
  }

  public NameObjectSetter(String name, Object x, int targetSqlType, int scaleOrLength) {
    super(name);
    this.type = 3;
    this.x = x;
    this.targetSqlType = targetSqlType;
    this.scaleOrLength = scaleOrLength;
  }

  public NameObjectSetter(String name, Object x, SQLType targetSqlType) {
    super(name);
    this.type = 2;
    this.x = x;
    this.targetSqlType2 = targetSqlType;
  }

  public NameObjectSetter(String name, Object x, SQLType targetSqlType, int scaleOrLength) {
    super(name);
    this.type = 3;
    this.x = x;
    this.targetSqlType2 = targetSqlType;
    this.scaleOrLength = scaleOrLength;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    if (this.type == 1) {
      cs.setObject(this.name, this.x);
    } else if (this.type == 2) {
      cs.setObject(this.name, this.x, this.targetSqlType);
    } else if (this.type == 3) {
      cs.setObject(this.name, this.x, this.targetSqlType, this.scaleOrLength);
    } else if (this.type == 4) {
      cs.setObject(this.name, this.x, this.targetSqlType2);
    } else {
      cs.setObject(this.name, this.x, this.targetSqlType2, this.scaleOrLength);
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
