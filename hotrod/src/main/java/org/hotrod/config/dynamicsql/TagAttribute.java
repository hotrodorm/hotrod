package org.hotrod.config.dynamicsql;

import org.hotrod.generator.ParameterRenderer;

public class TagAttribute {

  private String name;
  private ParameterisableTextPart value;

  public TagAttribute(final String name, final ParameterisableTextPart value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return this.name;
  }

  public String render(final ParameterRenderer parameterRenderer) {
    return this.value == null ? null : this.value.renderTag(parameterRenderer);
  }

}
