package org.hotrod.generator.springjdbc;

import org.hotrod.config.SQLParameter;
import org.hotrod.generator.ParameterRenderer;

public class SpringParameterRenderer implements ParameterRenderer {

  @Override
  public String render(final SQLParameter parameter) {

    // The parameter occurrence is a normal SQL parameter
    return ":" + parameter.getId().getJavaMemberName();

  }

}
