package org.hotrod.torcs.setters.name;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.hotrod.torcs.setters.index.CouldNotToGuessDataTypeException;
import org.hotrod.torcs.setters.index.DataTypeNotImplementedException;

public abstract class NameSetter {

  protected String name;

  protected NameSetter(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public boolean isConsummableParameter() {
    return false;
  }

  public boolean isLOBParameter() {
    return false;
  }

  public abstract void applyTo(CallableStatement cs) throws SQLException;

  public abstract Object value();

  public abstract String guessSQLServerDataType()
      throws DataTypeNotImplementedException, CouldNotToGuessDataTypeException;

}
