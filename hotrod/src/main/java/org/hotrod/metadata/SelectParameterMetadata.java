package org.hotrod.metadata;

import java.io.Serializable;

import org.hotrod.config.ParameterTag;
import org.hotrod.database.PropertyType;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrod.utils.identifiers.JavaIdentifier;

public class SelectParameterMetadata implements Serializable {

  private static final long serialVersionUID = 1L;

  private JavaIdentifier identifier;
  private ParameterTag p;

  public SelectParameterMetadata(final ParameterTag p) {
    this.identifier = new JavaIdentifier(p.getName(), new PropertyType(p.getJavaType(), JDBCType.VARCHAR, false));
    this.p = p;
  }

  // Getters

  public JavaIdentifier getIdentifier() {
    return identifier;
  }

  public ParameterTag getParameter() {
    return p;
  }

}
