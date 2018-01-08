package org.hotrod.eclipseplugin.elements;

import org.hotrod.eclipseplugin.domain.ConfigItem;
import org.hotrod.eclipseplugin.domain.FragmentConfigFile;
import org.hotrod.eclipseplugin.elements.ElementFactory.InvalidConfigurationItemException;

public class FragmentConfigElement extends TreeContainerElement {

  private FragmentConfigFile fragment;

  public FragmentConfigElement(final FragmentConfigFile fragment) throws InvalidConfigurationItemException {
    super(fragment.getFileName(), false);
    this.fragment = fragment;
    for (ConfigItem item : this.fragment.getConfigItems()) {
      TreeElement element = ElementFactory.getElement(item);
      super.addChild(element);
    }
  }

  @Override
  public String getIconPath() {
    return "icons/fragment8-16.png";
  }

  @Override
  public String getTooltip() {
    return "HotRod fragment file " + super.getLabel();
  }

}
