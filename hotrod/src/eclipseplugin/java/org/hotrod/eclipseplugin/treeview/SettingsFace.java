package org.hotrod.eclipseplugin.treeview;

import org.hotrod.config.GeneratorsTag;

public class SettingsFace extends AbstractFace {

  public SettingsFace(final GeneratorsTag settings) {
    super("Settings", settings);
  }

  @Override
  public String getIconPath() {
    return "eclipse-plugin/icons/settings6-16.png";
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
