package org.hotrod.torcs.setters.index;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class IndexSetter {

  protected int index;

  protected IndexSetter(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }

  public boolean isConsumableParameter() {
    return false;
  }

  public boolean isLOBParameter() {
    return false;
  }

  public abstract void applyTo(PreparedStatement ps) throws SQLException;

  public abstract Object value();

  public abstract String guessSQLServerDataType()
      throws DataTypeNotImplementedException, CouldNotToGuessDataTypeException;

}
