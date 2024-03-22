package org.hotrod.torcs.setters.name;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.SQLXML;

public class NameSQLXMLSetter extends NameSetter {

  private SQLXML xmlObject;

//void  setSQLXML(int parameterIndex, SQLXML xmlObject)

  public NameSQLXMLSetter(String name, SQLXML xmlObject) {
    super(name);
    this.xmlObject = xmlObject;
  }

  @Override
  public void applyTo(CallableStatement cs) throws SQLException {
    cs.setSQLXML(this.name, this.xmlObject);
  }

  @Override
  public Object value() {
    return this.xmlObject;
  }

  @Override
  public String guessSQLServerDataType() {
    return "xml";
  }

}
