package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.Settings;

public class SettingsFace extends AbstractLeafFace {

  private Settings settings;

  public SettingsFace(final Settings settings) {
    super(settings.getName(), false);
    this.settings = settings;
  }

  @Override
  public String getIconPath() {
    return "icons/settings6-16.png";
  }

  @Override
  public String getDecoration() {
    return "settings";
  }

  @Override
  public String getTooltip() {
    return "Settings";
  }

}
