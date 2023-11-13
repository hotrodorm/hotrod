package org.hotrod.torcs.setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Setter {

  protected int index;

  protected Setter(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }

  public abstract void applyTo(PreparedStatement ps) throws SQLException;

  public abstract Object value();

}
