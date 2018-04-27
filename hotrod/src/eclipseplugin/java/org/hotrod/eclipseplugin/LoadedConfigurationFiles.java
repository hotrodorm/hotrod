package org.hotrod.eclipseplugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.AbstractConfigurationTag.TagStatus;
import org.hotrod.config.ConfigurationLoader;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.eclipseplugin.FileSystemChangesListener.FileChangeListener;
import org.hotrod.eclipseplugin.ProjectProperties.FileProperties;
import org.hotrod.eclipseplugin.treeview.HotRodViewContentProvider;
import org.hotrod.eclipseplugin.treeview.MainConfigFace;

public class LoadedConfigurationFiles implements FileChangeListener {

  private static final Logger log = Logger.getLogger(LoadedConfigurationFiles.class);

  private static final String VALID_HOTROD_EXTENSION = ".xml";

  private HotRodViewContentProvider provider;
  private Map<String, MainConfigFace> loadedFiles = new TreeMap<String, MainConfigFace>();

  // Constructor

  public LoadedConfigurationFiles(final HotRodViewContentProvider provider) {
    super();
    this.provider = provider;
  }

  // File is being dropped (addeed to) in the view

  public void addFile(final File f) {

    if (f != null && f.getName().endsWith(VALID_HOTROD_EXTENSION) && f.isFile()) {
      String absolutePath = f.getAbsolutePath();
      if (!this.loadedFiles.containsKey(absolutePath)) {

        RelativeProjectPath path = RelativeProjectPath.findProjectPath(f);
        if (path != null) {

          // 1. Load the cached config

          ProjectProperties projectProperties = ProjectPropertiesCache.getProjectProperties(path.getProject());
          FileProperties fileProperties = projectProperties.getFileProperties(path.getRelativeFileName());

          // 2. Retrieve cached config, if available

          HotRodConfigTag cachedConfig = fileProperties.getCachedMetadata().getConfig();
          MainConfigFace cachedFace = cachedConfig == null ? null
              : new MainConfigFace(f, path, this.provider, cachedConfig);

          // 3. Load file

          MainConfigFace freshFace = load(f);
          log.debug("File loading - freshFace=" + freshFace);

          log.info("cachedConfig=" + cachedConfig);

          if (cachedConfig == null) {

            // 4.a Display loaded file as-is when no cached config is present.

            log.debug("No cached config present.");
            this.loadedFiles.put(absolutePath, freshFace);
            if (freshFace.isValid()) {
              freshFace.getConfig().setTreeStatus(TagStatus.ADDED);
              freshFace.computeBranchChanges();
            }

          } else {

            // 4.b Display combined loaded file with cached config.

            if (freshFace.isValid()) {

              this.loadedFiles.put(absolutePath, freshFace);

            } else {
              log.info("Cached config present.");
              cachedConfig.logGenerateMark("Generate Marks (PRE) - " + System.identityHashCode(cachedConfig), '-');
              log.info("freshFace=" + freshFace + " freshFace.getTag()=" + freshFace.getTag());
              cachedFace.applyChangesFrom(freshFace);
              HotRodConfigTag ex2 = cachedFace.getConfig();
              ex2.logGenerateMark("Generate Marks (POST) - " + System.identityHashCode(ex2), '=');

              this.loadedFiles.put(absolutePath, cachedFace);
              cachedFace.computeBranchChanges();
            }

          }

          // 5. Refresh the plugin tree view with new file.

          this.provider.refresh();

        }

      }
    }

  }

  private MainConfigFace load(final File f) {

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
          new ErrorMessage(e.getLocation(), e.getMessage()));
      return face;
    } catch (UncontrolledException e) {
      MainConfigFace face = new MainConfigFace(f, path, this.provider, new ErrorMessage(null, e.getMessage()));
      return face;
    }

  }

  public void remove(final MainConfigFace face) {
    this.loadedFiles.remove(face.getAbsolutePath());
  }

  private void reload(final MainConfigFace currentFace) {
    log.info("reload");
    String absPath = currentFace.getAbsolutePath();
    File f = new File(absPath);
    MainConfigFace newFace = load(f);
    HotRodConfigTag currentConfig = currentFace.getConfig();

    if (currentFace.isValid()) {
      if (newFace.isValid()) { // 1. Stays valid
        log.info("1. Stays valid");
        currentFace.applyChangesFrom(newFace);
      } else { // 2. Becoming invalid
        log.info("2. Becoming invalid");
        currentFace.setInvalid(newFace.getErrorMessage());
      }
    } else {
      if (newFace.isValid()) { // 3. Becoming valid
        log.info("3. Becoming valid");
        currentFace.setValid();
        if (currentConfig == null) {
          log.info("3.1 Set config to new one.");
          currentFace.setConfig(newFace.getConfig());
        } else {
          log.info("3.2 Apply changes.");
          currentFace.applyChangesFrom(newFace);
        }
      } else { // 4. Stays invalid
        log.info("4. Stays invalid");
        currentFace.setInvalid(newFace.getErrorMessage());
      }
    }

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
    log.debug("  --> received file added: " + f.getAbsolutePath());
    // Ignore new file
    return false;
  }

  @Override
  public boolean informFileRemoved(final File f) {
    log.debug("  --> received file removed: " + f.getAbsolutePath());
    String fullPathName = f.getAbsolutePath();
    printLoadedFiles();
    if (this.loadedFiles.containsKey(fullPathName)) {
      log.debug("  >> Found to remove");
      this.remove(this.loadedFiles.get(fullPathName));
      return true;
    } else {
      log.debug("  >> NOT Found to remove");
      return false;
    }
  }

  @Override
  public boolean informFileChanged(final File f) {
    log.debug("  --> received file changed: " + f.getAbsolutePath());
    String fullPathName = f.getAbsolutePath();
    if (this.loadedFiles.containsKey(fullPathName)) {
      MainConfigFace currentPresentedFace = this.loadedFiles.get(fullPathName);
      this.reload(currentPresentedFace);
      currentPresentedFace.computeBranchChanges();
      return true;
    }
    return false;
  }

  private void printLoadedFiles() {
    for (String p : this.loadedFiles.keySet()) {
      log.debug("## currently loaded: " + p);
    }
  }

}
