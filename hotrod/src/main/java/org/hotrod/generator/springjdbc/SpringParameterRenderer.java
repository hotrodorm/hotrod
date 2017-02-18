package org.hotrod.generator.springjdbc;

import org.hotrod.config.SQLParameter;
import org.hotrod.generator.ParameterRenderer;
import org.hotrod.utils.identifiers.JavaIdentifier;

public class SpringParameterRenderer implements ParameterRenderer {

  @Override
  public String render(final SQLParameter parameter) {

    JavaIdentifier id = new JavaIdentifier(
        parameter.isDefinition() ? parameter.getName() : parameter.getDefinition().getName(), parameter.getJavaType());

    // The parameter occurrence is a normal SQL parameter
    return ":" + id.getJavaMemberIdentifier();

  }

}
