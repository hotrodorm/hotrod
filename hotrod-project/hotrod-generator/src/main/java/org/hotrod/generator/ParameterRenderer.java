package org.hotrod.generator;

import org.hotrod.config.JDBCParameterOccurrence;

public interface ParameterRenderer {

  String render(JDBCParameterOccurrence parameter);

}
