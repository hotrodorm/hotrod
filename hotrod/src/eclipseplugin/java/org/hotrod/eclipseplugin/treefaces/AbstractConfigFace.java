package org.hotrod.eclipseplugin.treefaces;

import java.io.File;

import org.apache.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.DaosTag;
import org.hotrod.config.FragmentTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.eclipseplugin.ErrorMessage;
import org.hotrod.eclipseplugin.utils.FUtil;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.utils.FileRegistry;

public abstract class AbstractConfigFace extends AbstractFace {

  private static final Logger log = Logger.getLogger(AbstractConfigFace.class);

  private boolean valid;

  // Constructors

  public AbstractConfigFace(final String name, final AbstractConfigurationTag tag, final boolean valid) {
    super(name, tag);
    this.valid = valid;
  }

  public AbstractConfigFace(final String name, final AbstractConfigurationTag tag, final ErrorMessage errorMessage,
      final boolean valid) {
    super(name, tag, errorMessage);
    this.valid = valid;
  }

  // Validity

  public boolean isValid() {
    return valid;
  }

  public void setInvalid(final ErrorMessage errorMessage) {
    if (errorMessage == null) {
      throw new IllegalArgumentException("Cannot set null error message.");
    }
    this.valid = false;
    super.setErrorMessage(errorMessage);
  }

  public void setValid() {
    this.valid = true;
    super.setErrorMessage(null);
  }

  // Children

  @Override
  public AbstractFace[] getChildren() {
    log.debug("this.valid=" + this.valid);
    if (this.valid) {
      AbstractFace[] children = super.getChildren();
      log.debug("children[" + children.length + "]");
      return children;
    } else {
      return new AbstractFace[0];
    }
  }

  @Override
  public boolean hasChildren() {
    log.debug("this.valid=" + this.valid);
    if (this.valid) {
      return super.hasChildren();
    } else {
      return false;
    }
  }

  @Override
  public boolean hasBranchChanges() {
    log.debug("this.valid=" + this.valid);
    if (this.valid) {
      return super.hasBranchChanges();
    } else {
      return false;
    }
  }

  // Processing file system changes

  public final boolean informFileAdded(final File f, final HotRodConfigTag primaryConfig,
      final FileRegistry fileRegistry, final DaosTag daosTag) throws UncontrolledException, ControlledException {

    boolean changesDetected = false;
    for (FragmentConfigFace fragment : super.getFragments()) {
      if (FUtil.equals(f, fragment.getFragmentTag().getFile())) {
        changesDetected |= fragment.loadAndApplyChanges(primaryConfig, fileRegistry, daosTag);
      } else {
        changesDetected |= fragment.informFileAdded(f, primaryConfig, fileRegistry, daosTag);
      }
    }
    return changesDetected;
  }

  public final boolean informFileChanged(final File f, final HotRodConfigTag primaryConfig,
      final FileRegistry fileRegistry, final DaosTag daosTag) throws UncontrolledException, ControlledException {
    boolean changesDetected = false;
    for (FragmentConfigFace fragment : super.getFragments()) {
      FragmentTag tag = fragment.getFragmentTag();
      log.debug("fragment file: " + tag.getFile());
      if (FUtil.equals(f, tag.getFile())) {
        log.debug("inform - fragment is file");
        changesDetected |= fragment.loadAndApplyChanges(primaryConfig, fileRegistry, daosTag);
      } else {
        log.debug("inform - fragment is NOT file");
        changesDetected |= fragment.informFileChanged(f, primaryConfig, fileRegistry, daosTag);
      }
    }
    log.debug("inform complete: changesDetected=" + changesDetected);
    return changesDetected;
  }

  public final boolean informFileRemoved(final File f) throws UncontrolledException, ControlledException {
    boolean changesDetected = false;
    for (FragmentConfigFace fragment : super.getFragments()) {
      FragmentTag tag = fragment.getFragmentTag();
      if (FUtil.equals(f, tag.getFile())) {
        throw new ControlledException(tag.getSourceLocation(), "Fragment '" + tag.getFile().getPath() + "' not found.",
            "Fragment '" + tag.getFile().getPath() + "' not found.");
      } else {
        changesDetected |= fragment.informFileRemoved(f);
      }
    }
    return changesDetected;
  }

}
