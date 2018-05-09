package org.hotrod.eclipseplugin.treeview;

import org.hotrod.config.GeneratorsTag;
import org.hotrod.eclipseplugin.HotRodView;

public class SettingsFace extends AbstractFace {

  public SettingsFace(final GeneratorsTag settings) {
    super("Settings", settings);
  }

  @Override
  public String getIconPath() {
    return HotRodView.ICONS_DIR + "settings.png";
  }

  @Override
  public String getErrorIconPath() {
    return HotRodView.ICONS_DIR + "settings-error.png";
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
