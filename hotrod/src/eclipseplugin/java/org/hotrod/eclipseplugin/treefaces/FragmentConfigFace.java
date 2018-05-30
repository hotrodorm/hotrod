package org.hotrod.eclipseplugin.treefaces;

import org.apache.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.ConfigurationLoader;
import org.hotrod.config.DaosTag;
import org.hotrod.config.FragmentTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.eclipseplugin.ErrorMessage;
import org.hotrod.eclipseplugin.HotRodView;
import org.hotrod.eclipseplugin.treefaces.FaceFactory.InvalidConfigurationItemException;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.utils.FileRegistry;

public class FragmentConfigFace extends AbstractConfigFace {

  private static final Logger log = Logger.getLogger(FragmentConfigFace.class);

  private String relativePath;
  private FragmentTag fragmentTag;
  private HotRodFragmentConfigTag fragmentConfig;

  public FragmentConfigFace(final FragmentTag fragmentTag) throws InvalidConfigurationItemException {
    super(fragmentTag.getFilename(), fragmentTag, false);
    this.fragmentTag = fragmentTag;

    // TODO: fix this: the relative path must be based on the project, not the
    // parent file.
    // this.relativePath = fragment.getRelativeProjectPath().getRelativePath();
    this.relativePath = fragmentTag.getFilename();

    for (AbstractConfigurationTag item : fragmentTag.getSubTags()) {
      AbstractFace element = FaceFactory.getFace(item);
      super.addChild(element);
    }
  }

  private FragmentConfigFace(final FragmentTag fragmentTag, final HotRodFragmentConfigTag fragmentConfig)
      throws InvalidConfigurationItemException {
    super(fragmentTag.getFilename(), fragmentTag, true);
    this.fragmentTag = fragmentTag;

    // TODO: fix this: the relative path must be based on the project, not the
    // parent file.
    // this.relativePath = fragment.getRelativeProjectPath().getRelativePath();
    this.relativePath = fragmentTag.getFilename();

    for (AbstractConfigurationTag item : fragmentConfig.getSubTags()) {
      AbstractFace element = FaceFactory.getFace(item);
      super.addChild(element);
    }
  }

  // Processing file system changes

  public boolean loadAndApplyChanges(final HotRodConfigTag primaryConfig, final FileRegistry fileRegistry,
      final DaosTag daosTag) throws UncontrolledException, ControlledException {

    HotRodFragmentConfigTag fragmentConfig;
    try {
      fragmentConfig = ConfigurationLoader.loadFragment(primaryConfig, this.fragmentTag.getFile(), fileRegistry,
          daosTag, this.getFragmentTag());
    } catch (ControlledException e) {
      this.setInvalid(new ErrorMessage(this.fragmentTag.getSourceLocation(), e.getMessage()));
      throw e;
    } catch (UncontrolledException e) {
      this.setInvalid(new ErrorMessage(this.fragmentTag.getSourceLocation(), e.getMessage()));
      throw e;
    }

    FragmentConfigFace freshFace = null;
    try {
      freshFace = new FragmentConfigFace(this.fragmentTag, fragmentConfig);
    } catch (InvalidConfigurationItemException e) {
      throw new UncontrolledException("Could not load fragment '" + this.fragmentTag.getFile().getPath() + "'.", e);
    }

    boolean changesDetected = applyFreshVersion(freshFace);

    log.info("changesDetected=" + changesDetected);
    return changesDetected;

  }

  private boolean applyFreshVersion(final FragmentConfigFace freshFace) {
    if (this.isValid()) {
      if (freshFace.isValid()) { // 1. Stays valid
        log.info("1. Stays valid");

        this.fragmentConfig.logGenerateMark("Generate Marks (PRE) - " + System.identityHashCode(this.fragmentConfig),
            '-');
        // currentFace.display();
        boolean changesDetected = this.applyChangesFrom(freshFace);
        // currentFace.display();
        this.fragmentConfig.logGenerateMark("Generate Marks (POST) - " + System.identityHashCode(this.fragmentConfig),
            '=');

        return changesDetected;

      } else { // 2. Becoming invalid
        log.info("2. Becoming invalid");
        this.setInvalid(freshFace.getErrorMessage());
        return true;
      }
    } else {
      if (freshFace.isValid()) { // 3. Becoming valid
        log.info("3. Becoming valid");
        this.setValid();
        if (this.fragmentConfig == null) {
          log.info("3.1 Set first config since it was null so far.");
          this.fragmentConfig = freshFace.getFragmentTag().getFragmentConfig();
          super.removeAllChildren();
          addSubFaces(this.fragmentConfig);

        } else {
          log.info("3.2 Apply changes.");
          this.applyChangesFrom(freshFace);
        }
        return true;
      } else { // 4. Stays invalid
        log.info("4. Stays invalid");
        this.setInvalid(freshFace.getErrorMessage());
        return true;
      }
    }
  }

  private void addSubFaces(final HotRodFragmentConfigTag config) {
    for (AbstractConfigurationTag subTag : config.getSubTags()) {
      try {
        AbstractFace face = FaceFactory.getFace(subTag);
        this.addChild(face);
      } catch (InvalidConfigurationItemException e) {
        this.fragmentConfig = null;
        this.removeAllChildren();
        return;
      }
    }
  }

  // Getters

  @Override
  public String getDecoration() {
    return "fragment";
  }

  public String getRelativePath() {
    return this.relativePath;
  }

  public FragmentTag getFragmentTag() {
    return fragmentTag;
  }

  @Override
  public String getIconPath() {
    return HotRodView.ICONS_DIR + "fragment.png";
  }

  @Override
  public String getErrorIconPath() {
    return HotRodView.ICONS_DIR + "fragment-error.png";
  }

  @Override
  public String getTooltip() {
    return "HotRod fragment file " + super.getName();
  }

}
