package org.hotrod.eclipseplugin.treeview;

import org.hotrod.eclipseplugin.domain.ConfigItem;
import org.hotrod.eclipseplugin.domain.FragmentConfigFile;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class FragmentConfigFace extends AbstractContainerFace {

  private FragmentConfigFile fragment;

  public FragmentConfigFace(final FragmentConfigFile fragment) throws InvalidConfigurationItemException {
    super(fragment.getShortName(), false);
    this.fragment = fragment;
    for (ConfigItem item : this.fragment.getConfigItems()) {
      AbstractFace element = FaceFactory.getFace(item);
      super.addChild(element);
    }
  }

  @Override
  public String getDecoration() {
    return "fragment";
  }

  @Override
  public String getIconPath() {
    return "icons/fragment8-16.png";
    // return this.valid ? "icons/fragment9-16.png" :
    // "icons/fragment9-bad-16.png";
  }

  @Override
  public String getTooltip() {
    return "HotRod fragment file " + super.getLabel();
  }

}
