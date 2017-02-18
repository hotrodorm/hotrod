package org.hotrod.metadata;

import org.hotrod.config.SQLParameter;
import org.hotrod.database.PropertyType;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrod.utils.identifiers.JavaIdentifier;

public class SelectParameterMetadata {

  private JavaIdentifier identifier;
  private SQLParameter p;

  public SelectParameterMetadata(final SQLParameter p) {
    this.identifier = new JavaIdentifier(p.getName(), new PropertyType(p.getJavaType(), JDBCType.VARCHAR, false));
    this.p = p;
  }

  // Getters

  public JavaIdentifier getIdentifier() {
    return identifier;
  }

  public SQLParameter getParameter() {
    return p;
  }

}
