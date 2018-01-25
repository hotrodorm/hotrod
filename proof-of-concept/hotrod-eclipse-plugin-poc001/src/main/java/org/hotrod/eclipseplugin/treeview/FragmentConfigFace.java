package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.ConfigItem;
import org.hotrod.eclipseplugin.domain.FragmentConfigFile;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class FragmentConfigFace extends AbstractFace {

  private String relativePath;

  public FragmentConfigFace(final FragmentConfigFile fragment) throws InvalidConfigurationItemException {
    super(fragment.getShortName(), fragment);
    this.relativePath = fragment.getRelativeProjectPath().getRelativePath();
    for (ConfigItem item : fragment.getSubItems()) {
      AbstractFace element = FaceFactory.getFace(item);
      super.addChild(element);
    }
  }

  @Override
  public String getDecoration() {
    return "fragment";
  }

  public String getRelativePath() {
    return this.relativePath;
  }

  @Override
  public String getIconPath() {
    return "icons/fragment8-16.png";
    // return this.valid ? "icons/fragment9-16.png" :
    // "icons/fragment9-bad-16.png";
  }

  @Override
  public String getTooltip() {
    return "HotRod fragment file " + super.getName();
  }

}
