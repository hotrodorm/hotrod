package org.hotrod.eclipseplugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag.TagStatus;
import org.hotrod.config.ConfigurationLoader;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.eclipseplugin.FileProperties.CouldNotSaveFilePropertiesException;
import org.hotrod.eclipseplugin.FileSystemChangesListener.FileChangeListener;
import org.hotrod.eclipseplugin.ProjectProperties.CouldNotSaveProjectPropertiesException;
import org.hotrod.eclipseplugin.treefaces.MainConfigFace;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.utils.EUtils;

public class LoadedConfigurationFiles implements FileChangeListener {

  private static final Logger log = Logger.getLogger(LoadedConfigurationFiles.class);

  private static final String VALID_HOTROD_EXTENSION = ".xml";

  private HotRodViewContentProvider provider;
  private HotRodView viewPart;

  private Map<String, MainConfigFace> loadedFiles = new TreeMap<String, MainConfigFace>();

  // Constructor

  public LoadedConfigurationFiles(final HotRodViewContentProvider provider, final HotRodView viewPart) {
    super();
    this.provider = provider;
    this.viewPart = viewPart;
  }

  // File is being dropped in (addeed to) the view

  public void addFile(final File f) {
    log.info("ADD f=" + f);

    try {

      if (f != null && f.getName().endsWith(VALID_HOTROD_EXTENSION) && f.isFile()) {
        log.info("ADD [2] f=" + f);
        String absolutePath = f.getAbsolutePath();
        boolean filesLoaded = false;
        if (!this.loadedFiles.containsKey(absolutePath)) {
          filesLoaded = true;

          RelativeProjectPath path = RelativeProjectPath.findProjectPath(f);
          if (path != null) {

            // 1. Load the cached config

            log.info("will load project properties.");
            ProjectProperties projectProperties = WorkspaceProperties.getInstance()
                .getProjectProperties(path.getProject());
            log.info("will load file properties.");
            FileProperties fileProperties = projectProperties.getFileProperties(path.getRelativeFileName());
            log.info("fileProperties=" + fileProperties);

            // 2. Retrieve cached config (current), if available

            HotRodConfigTag currentConfig = fileProperties == null ? null
                : fileProperties.getCachedMetadata().getConfig();
            MainConfigFace currentFace = currentConfig == null ? null
                : new MainConfigFace(f, path, this.provider, currentConfig);
            log.debug("cachedConfig=" + currentConfig);

            // 3. Load new face (fresh)

            log.debug("File loading...");
            MainConfigFace freshFace = loadFile(f);
            log.debug("Loaded - freshFace=" + freshFace);

            if (currentConfig == null) {

              // 4.a Display loaded file as-is when no cached config is present.

              log.debug("No cached config present.");
              this.loadedFiles.put(absolutePath, freshFace);
              if (freshFace.getConfig() != null) {
                freshFace.getConfig().setTreeStatus(TagStatus.ADDED);
              }
              this.viewPart.informFileAdded(freshFace, true);

            } else {

              // 4.b Display combined loaded file with cached config.

              log.debug("Cached config present.");
              this.loadedFiles.put(absolutePath, currentFace);
              boolean changesDetected = applyFreshVersion(currentFace, freshFace);
              log.debug("changesDetected=" + changesDetected);
              this.viewPart.informFileAdded(currentFace, changesDetected);

            }

            try {
              fileProperties.save();
            } catch (CouldNotSaveFilePropertiesException e) {
              log.error("Could not save file properties: " + e.getMessage());
            }

            try {
              projectProperties.save();
            } catch (CouldNotSaveProjectPropertiesException e) {
              log.error("Could not save project properties: " + e.getMessage());
            }

          }

        }

        if (filesLoaded) {
          this.viewPart.refreshToolbar();
        }

      }

    } catch (Throwable t) {
      log.error("Throwable caught.", t);
    }

  }

  private void reload(final MainConfigFace currentFace) {
    log.debug("reload");
    String absPath = currentFace.getAbsolutePath();
    File f = new File(absPath);
    MainConfigFace freshFace = loadFile(f);

    boolean changesDetected = applyFreshVersion(currentFace, freshFace);
    log.debug("changesDetected=" + changesDetected);

    if (changesDetected) {
      this.viewPart.informFileChangesDetected(currentFace);
    }

  }

  private boolean applyFreshVersion(final MainConfigFace currentFace, final MainConfigFace freshFace) {
    if (currentFace.isValid()) {
      if (freshFace.isValid()) { // 1. Stays valid
        log.debug("1. Stays valid");

        HotRodConfigTag bl1 = currentFace.getConfig();
        bl1.logGenerateMark("Generate Marks (PRE) - " + System.identityHashCode(bl1), '-');

        // currentFace.display();
        boolean changesDetected = currentFace.applyChangesFrom(freshFace);
        // currentFace.display();

        HotRodConfigTag bl2 = currentFace.getConfig();
        bl2.logGenerateMark("Generate Marks (POST) - " + System.identityHashCode(bl2), '=');

        return changesDetected;

      } else { // 2. Becoming invalid
        log.debug("2. Becoming invalid");
        currentFace.setInvalid(freshFace.getErrorMessage());
        return true;
      }
    } else {
      if (freshFace.isValid()) { // 3. Becoming valid
        log.debug("3. Becoming valid");
        currentFace.setValid();
        if (currentFace.getConfig() == null) {
          log.debug("3.1 Set first config since it was null so far.");
          currentFace.initializeConfig(freshFace.getConfig());
        } else {
          log.debug("3.2 Apply changes.");
          currentFace.applyChangesFrom(freshFace);
        }
        return true;
      } else { // 4. Stays invalid
        log.debug("4. Stays invalid");
        currentFace.setInvalid(freshFace.getErrorMessage());
        return true;
      }
    }
  }

  private MainConfigFace loadFile(final File f) {

    RelativeProjectPath path = RelativeProjectPath.findProjectPath(f);
    if (path == null) {
      return null;
    }

    // Load the file

    try {

      // TODO: fix generator value. Should be configurable
      HotRodConfigTag config = ConfigurationLoader.loadPrimary(path.getProject().getLocation().toFile(), f, "MyBatis");
      MainConfigFace face = new MainConfigFace(f, path, this.provider, config);
      return face;

    } catch (ControlledException e) {
      log.info("Error Message=" + e.getMessage() + " loc=" + e.getLocation());
      MainConfigFace face = new MainConfigFace(f, path, this.provider,
          new ErrorMessage(e.getLocation(), e.getInteractiveMessage()));
      return face;
    } catch (UncontrolledException e) {
      log.error("Failed to load configuration file.", e);
      MainConfigFace face = new MainConfigFace(f, path, this.provider,
          new ErrorMessage(null, EUtils.renderMessages(e)));
      return face;
    }

  }

  public void remove(final MainConfigFace face) {
    this.loadedFiles.remove(face.getAbsolutePath());
  }

  public void removeAll() {
    this.loadedFiles.clear();
  }

  // Getters

  public List<MainConfigFace> getLoadedFiles() {
    ArrayList<MainConfigFace> list = new ArrayList<MainConfigFace>(loadedFiles.values());
    Collections.sort(list);
    return list;
  }

  // FileChangesListener

  @Override
  public boolean informFileAdded(final File f) {
    log.info("\n\n[EVENT] file added: " + f.getAbsolutePath());
    boolean changesDetected = false;
    for (MainConfigFace face : this.loadedFiles.values()) {
      try {
        boolean triggerChanges = face.triggerFileAdded(f);
        log.info("triggerChanges="+triggerChanges);
        changesDetected |= triggerChanges;
        face.setValid();
      } catch (ControlledException e) {
        changesDetected = true;
        log.info(":::: exception in tag location: " + e.getLocation());
        face.setInvalid(new ErrorMessage(e.getLocation(), e.getInteractiveMessage()));
      } catch (UncontrolledException e) {
        changesDetected = true;
        face.setInvalid(new ErrorMessage(null, EUtils.renderMessages(e)));
      }
    }
    return changesDetected;
  }

  @Override
  public boolean informFileChanged(final File f) {
    log.info("\n\n[EVENT] file changed: " + f.getAbsolutePath());

    // Check if it's a main face

    String fullPathName = f.getAbsolutePath();
    if (this.loadedFiles.containsKey(fullPathName)) {
      MainConfigFace currentPresentedFace = this.loadedFiles.get(fullPathName);
      this.reload(currentPresentedFace);
      currentPresentedFace.computeBranchMarkers();
      return true;
    }

    log.info("[EVENT] file changed 2");

    // Otherwise, check if it's a fragment

    boolean refreshNeeded = false;
    for (MainConfigFace face : this.loadedFiles.values()) {
      try {
        boolean faceChanged = face.triggerFileChanged(f);
        face.setValid();
        log.info("### faceChanged=" + faceChanged);
        refreshNeeded |= faceChanged;

      } catch (ControlledException e) {
        log.info("[ControlledException] " + e.getInteractiveMessage());
        refreshNeeded = true;
        face.setInvalid(new ErrorMessage(e.getLocation(), e.getInteractiveMessage()));
      } catch (UncontrolledException e) {
        log.info("[UncontrolledException]", e);
        refreshNeeded = true;
        face.setInvalid(new ErrorMessage(null, EUtils.renderMessages(e)));
      }
    }
    return refreshNeeded;

  }

  @Override
  public boolean informFileRemoved(final File f) {
    log.info("\n\n[EVENT] file removed: " + f.getAbsolutePath());

    // Check if it's a main face

    String fullPathName = f.getAbsolutePath();
    if (this.loadedFiles.containsKey(fullPathName)) {
      log.debug("  >> Found to remove");
      this.remove(this.loadedFiles.get(fullPathName));
      return true;
    }

    // Otherwise, check if it's a fragment

    boolean changesDetected = false;
    for (MainConfigFace face : this.loadedFiles.values()) {
      try {
        changesDetected |= face.triggerFileRemoved(f);
//        face.setValid();
      } catch (ControlledException e) {
        changesDetected = true;
        face.setInvalid(new ErrorMessage(e.getLocation(), e.getInteractiveMessage()));
      } catch (UncontrolledException e) {
        changesDetected = true;
        face.setInvalid(new ErrorMessage(null, EUtils.renderMessages(e)));
      }
    }
    return changesDetected;

  }

}
