package org.hotrod.torcs.setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NullSetter extends Setter {

  private int type;
  private int sqlType;
  private String typeName;

//void  setNull(int parameterIndex, int sqlType)
//void  setNull(int parameterIndex, int sqlType, String typeName)

  public NullSetter(int index, int sqlType) {
    super(index);
    this.type = 1;
    this.sqlType = sqlType;
  }

  public NullSetter(int index, int sqlType, String typeName) {
    super(index);
    this.type = 2;
    this.sqlType = sqlType;
    this.typeName = typeName;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    if (this.type == 1) {
      ps.setNull(this.index, this.sqlType);
    } else {
      ps.setNull(this.index, this.sqlType, this.typeName);
    }
  }

  @Override
  public Object value() {
    return this.sqlType;
  }

}