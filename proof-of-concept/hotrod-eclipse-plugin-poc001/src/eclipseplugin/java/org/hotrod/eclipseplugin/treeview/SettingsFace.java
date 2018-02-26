package org.hotrod.eclipseplugin.treeview;

import org.hotrod.domain.Settings;

public class SettingsFace extends AbstractFace {

  public SettingsFace(final Settings settings) {
    super(settings.getName(), settings);
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
