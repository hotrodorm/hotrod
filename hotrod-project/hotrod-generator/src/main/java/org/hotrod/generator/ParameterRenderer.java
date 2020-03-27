package org.hotrod.generator;

import org.hotrod.config.SQLParameter;

public interface ParameterRenderer {

  String render(SQLParameter parameter);

}
