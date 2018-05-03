package org.hotrod.eclipseplugin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.hotrod.eclipseplugin.FileProperties.CouldNotLoadFilePropertiesException;

/**
 * <pre>
 *    File Properties Graph
 *    =====================
 *                                             (save)
 *   local config cache <--------------------------------------------------------+
 *          |                                                                    |
 *          V                                                                    |
 *    [DRAG & DROP LOAD]                                                         |
 *       |     |                               (update)                          |
 *       |     +-> "cached" config <------------------------------------------+  |
 *       |                \                                                   |  |
 *       |                 \                                                  |  |
 *       |                  *-> "correlated" config --> Eclipse Plugin --> [GENERATE]
 *       |                 /                   \
 *       |                /                     \
 *       +-----> "file" config                   *--> ...another client...
 *                    ^
 *                    |
 *           [FILE CHANGE DETECTED]
 *                    |
 *                config file
 *
 * </pre>
 */
public class ProjectProperties {

  private static final Logger log = Logger.getLogger(ProjectProperties.class);

  private static final String PROJECT_FILE_NAME = "project.properties";
  private static final String FILE_ATT_PREFIX = "file";

  private IPath projectDir;
  private Map<String, String> loadedFilesByCode = new TreeMap<String, String>();
  private Map<String, String> loadedFilesByFileName = new TreeMap<String, String>();

  // Constructor

  private ProjectProperties(final IPath projectDir) {
    log.debug("init");
    this.projectDir = projectDir;
  }

  // Persistence

  public static ProjectProperties load(final IPath projectDir) {
    IPath pp = projectDir.append(PROJECT_FILE_NAME);
    Properties prop = new Properties();
    ProjectProperties projectProperties = new ProjectProperties(projectDir);
    try {
      prop.load(new FileInputStream(pp.toFile()));
      for (String name : prop.stringPropertyNames()) {
        if (name.startsWith(FILE_ATT_PREFIX)) {
          String value = prop.getProperty(name);
          projectProperties.registerFile(name, value);
        }
      }
      return projectProperties;
    } catch (FileNotFoundException e) {
      return new ProjectProperties(projectDir);
    } catch (IOException e) {
      log.error("Could not load project properties for project '" + projectDir.toFile().getName() + "'.", e);
      return new ProjectProperties(projectDir);
    }
  }

  private void registerFile(final String code, final String value) {
    this.loadedFilesByCode.put(code, value);
    this.loadedFilesByFileName.put(value, code);
  }

  public Set<String> getRelativeFileNames() {
    return this.loadedFilesByFileName.keySet();
  }

  public void save() throws CouldNotSaveProjectPropertiesException {
    IPath pp = this.projectDir.append(PROJECT_FILE_NAME);
    Properties prop = new Properties();
    for (String code : this.loadedFilesByCode.keySet()) {
      String relativeFileName = this.loadedFilesByCode.get(code);
      prop.setProperty(code, relativeFileName);
    }
    try {
      prop.store(new FileOutputStream(pp.toFile()), "HotRod project properties\n");
    } catch (FileNotFoundException e) {
      throw new CouldNotSaveProjectPropertiesException(e.getMessage());
    } catch (IOException e) {
      throw new CouldNotSaveProjectPropertiesException(e.getMessage());
    }
  }

  public FileProperties getFileProperties(final String relativeFileName) {
    String code = this.loadedFilesByFileName.get(relativeFileName);
    if (code == null) {
      code = getNewCode(relativeFileName);
      return new FileProperties(this, code, relativeFileName);
    } else {
      try {
        return FileProperties.load(this, code, relativeFileName);
      } catch (CouldNotLoadFilePropertiesException e) {
        log.error("Could not load file properties for '" + relativeFileName + "' -- instantiating an empty one.");
        return new FileProperties(this, code, relativeFileName);
      }
    }
  }

  private String getNewCode(final String relativeFileName) {
    for (int i = 1; i < 1000000; i++) {
      String codeCandidate = "file" + i;
      if (!this.loadedFilesByCode.containsKey(codeCandidate)) {
        this.registerFile(codeCandidate, relativeFileName);
        return codeCandidate;
      }
    }
    return null;
  }

  public void removeFileProperties(final String relativeFileName) throws CouldNotSaveProjectPropertiesException {
    String code = this.loadedFilesByFileName.get(relativeFileName);
    if (code != null) {
      this.loadedFilesByFileName.remove(relativeFileName);
      this.loadedFilesByCode.remove(code);
      FileProperties.remove(this, code);
    }
    this.save();
  }

  // Getters

  public IPath getProjectDir() {
    return projectDir;
  }

  // Exceptions

  public static class CouldNotLoadProjectPropertiesException extends Exception {

    private static final long serialVersionUID = 1L;

    private CouldNotLoadProjectPropertiesException(final String message) {
      super(message);
    }

  }

  public static class CouldNotSaveProjectPropertiesException extends Exception {

    private static final long serialVersionUID = 1L;

    private CouldNotSaveProjectPropertiesException(final String message) {
      super(message);
    }

  }

}
