package org.hotrod.eclipseplugin.elements;

import org.hotrod.eclipseplugin.domain.Settings;

public class SettingsElement extends TreeLeafElement {

  private Settings settings;

  public SettingsElement(final Settings settings) {
    super(settings.getName(), false);
    this.settings = settings;
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
