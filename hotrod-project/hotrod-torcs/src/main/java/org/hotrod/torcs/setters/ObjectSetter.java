package org.hotrod.torcs.setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;

public class ObjectSetter extends Setter {

  private int type;
  private Object x;
  private int targetSqlType;
  private int scaleOrLength;
  private SQLType targetSqlType2;

//void  setObject(int parameterIndex, Object x)
//void  setObject(int parameterIndex, Object x, int targetSqlType)
//void  setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength)
//default void  setObject(int parameterIndex, Object x, SQLType targetSqlType)
//default void  setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength)

  public ObjectSetter(int index, Object x) {
    super(index);
    this.type = 1;
    this.x = x;
  }

  public ObjectSetter(int index, Object x, int targetSqlType) {
    super(index);
    this.type = 2;
    this.x = x;
    this.targetSqlType = targetSqlType;
  }

  public ObjectSetter(int index, Object x, int targetSqlType, int scaleOrLength) {
    super(index);
    this.type = 3;
    this.x = x;
    this.targetSqlType = targetSqlType;
    this.scaleOrLength = scaleOrLength;
  }

  public ObjectSetter(int index, Object x, SQLType targetSqlType) {
    super(index);
    this.type = 2;
    this.x = x;
    this.targetSqlType2 = targetSqlType;
  }

  public ObjectSetter(int index, Object x, SQLType targetSqlType, int scaleOrLength) {
    super(index);
    this.type = 3;
    this.x = x;
    this.targetSqlType2 = targetSqlType;
    this.scaleOrLength = scaleOrLength;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    if (this.type == 1) {
      ps.setObject(this.index, this.x);
    } else if (this.type == 2) {
      ps.setObject(this.index, this.x, this.targetSqlType);
    } else if (this.type == 3) {
      ps.setObject(this.index, this.x, this.targetSqlType, this.scaleOrLength);
    } else if (this.type == 4) {
      ps.setObject(this.index, this.x, this.targetSqlType2);
    } else {
      ps.setObject(this.index, this.x, this.targetSqlType2, this.scaleOrLength);
    }
  }

  @Override
  public Object value() {
    return this.x;
  }

}
