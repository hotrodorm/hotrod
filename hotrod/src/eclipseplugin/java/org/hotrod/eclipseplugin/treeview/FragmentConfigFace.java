package org.hotrod.eclipseplugin.treeview;

import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.FragmentTag;
import org.hotrod.eclipseplugin.treeview.FaceFactory.InvalidConfigurationItemException;

public class FragmentConfigFace extends AbstractFace {

  private String relativePath;

  public FragmentConfigFace(final FragmentTag fragment) throws InvalidConfigurationItemException {
    super(fragment.getFilename(), fragment);

    // TODO: fix this: the relative path must be based on the project, not the
    // parent file.
    // this.relativePath = fragment.getRelativeProjectPath().getRelativePath();
    this.relativePath = fragment.getFilename();

    for (AbstractConfigurationTag item : fragment.getSubTags()) {
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
    return "eclipse-plugin/icons/fragment8-16.png";
    // return this.valid ? "icons/fragment9-16.png" :
    // "icons/fragment9-bad-16.png";
  }

  @Override
  public String getTooltip() {
    return "HotRod fragment file " + super.getName();
  }

}
