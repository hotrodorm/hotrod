package org.hotrod.eclipseplugin.elements;

import org.hotrod.eclipseplugin.domain.SequenceMethod;

public class SequenceElement extends TreeLeafElement {

  private SequenceMethod method;

  public SequenceElement(final SequenceMethod method) {
    super(method.getName(), false);
    this.method = method;
  }

  @Override
  public String getIconPath() {
    // return "icons/sql-sequence5-16.png";
    return "icons/sql-sequence7b-16.png";
  }

  @Override
  public String getLabel() {
    return super.getLabel() + "() -- retrieve sequence value";
  }

  @Override
  public String getTooltip() {
    return "Get SQL sequence " + super.getLabel();
  }

}
