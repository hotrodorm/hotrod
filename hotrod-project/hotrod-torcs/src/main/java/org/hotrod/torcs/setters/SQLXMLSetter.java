package org.hotrod.torcs.setters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLXML;

public class SQLXMLSetter extends Setter {

  private SQLXML xmlObject;

//void  setSQLXML(int parameterIndex, SQLXML xmlObject)

  public SQLXMLSetter(int index, SQLXML xmlObject) {
    super(index);
    this.xmlObject = xmlObject;
  }

  @Override
  public void applyTo(PreparedStatement ps) throws SQLException {
    ps.setSQLXML(this.index, this.xmlObject);
  }

  @Override
  public Object value() {
    return this.xmlObject;
  }

}