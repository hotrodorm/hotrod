package org.hotrod.torcs.setters.index;

import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.SQLException;

public class RefSetter extends IndexSetter {

  private Ref x;

//void  setRef(int parameterIndex, Ref x)

  public RefSetter(int index, Ref x) {
    super(index);
    this.x = x;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setRef(this.index, this.x);
  }

  @Override
  public Object value() {
    return this.x;
  }

  @Override
  public String guessSQLServerDataType() throws DataTypeNotImplementedException {
    throw new DataTypeNotImplementedException();
  }

  
}
