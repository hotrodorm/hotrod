package org.hotrod.eclipseplugin.elements;

public class SequenceElement extends TreeLeafElement {

  public SequenceElement(final String name, final boolean modified) {
    super(name, modified);
  }

  public SequenceElement(final String name) {
    super(name, false);
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
