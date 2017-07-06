package org.hotrod.generator;

import org.hotrod.config.sql.SQLParameter;

public interface ParameterRenderer {

  String render(SQLParameter parameter);

}
