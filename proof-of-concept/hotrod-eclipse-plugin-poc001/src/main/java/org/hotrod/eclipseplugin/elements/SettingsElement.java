package org.hotrod.eclipseplugin.elements;

public class SettingsElement extends TreeLeafElement {

  public SettingsElement(final boolean modified) {
    super("", modified);
  }

  public SettingsElement() {
    super("", false);
  }

  @Override
  public String getIconPath() {
    return "icons/settings6-16.png";
  }

  @Override
  public String getLabel() {
    return "Settings";
  }

  @Override
  public String getTooltip() {
    return "Settings";
  }

}
