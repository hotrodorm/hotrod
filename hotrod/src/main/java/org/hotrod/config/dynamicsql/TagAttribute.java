package org.hotrod.config.dynamicsql;

import org.hotrod.generator.ParameterRenderer;

public class TagAttribute {

  private String name;
  private LiteralTextPart lValue;
  private ParameterisableTextPart pValue;

  public TagAttribute(final String name, final ParameterisableTextPart value) {
    this.name = name;
    this.lValue = null;
    this.pValue = value;
  }

  public TagAttribute(final String name, final LiteralTextPart value) {
    this.name = name;
    this.lValue = value;
    this.pValue = null;
  }

  public String getName() {
    return this.name;
  }

  public String render(final ParameterRenderer parameterRenderer) {
    if (this.lValue != null) {
      return this.lValue.renderXML(parameterRenderer);
    } else {
      return this.pValue == null ? null : this.pValue.renderXML(parameterRenderer);
    }
  }

}
