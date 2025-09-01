package org.hotrod.eclipseplugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;

/**
 * <pre>
 * 
 * <plugin-state> / state.properties 
 *                     autogenerate_active=1
 *                / project1 / project.properties 
 *                                file1=config/hotrod-small1.xml
 *                                file2=config/hotrod2.xml
 *                             file1.properties
 *                             file1.cache
 * 
 * </pre>
 *
 * @author valarcon
 *
 */

public class WorkspaceProperties {

  private static final Logger log = Logger.getLogger(WorkspaceProperties.class);

  private static WorkspaceProperties instance = null;

  private static final String STATE_FILE_NAME = "workspace.properties";

  private static final String AUTOGENERATE_ATT = "autogenerate_active";
  private static final String AUTOGENERATE_VALUE_ON = "1";
  private static final String AUTOGENERATE_VALUE_OFF = "0";

  private boolean autogenerateOnChanges;

  // Singleton retriever

  public static void initializeInstance() throws CouldNotLoadWorkspacePropertiesException {
    if (instance == null) {
      synchronized (WorkspaceProperties.class) {
        if (instance == null) {
          instance = new WorkspaceProperties();
        }
      }
    }
  }

  public static WorkspaceProperties getInstance() {
    if (WorkspaceProperties.instance == null) {
      throw new IllegalArgumentException("The workspace properties have not been initialized.");
    }
    return WorkspaceProperties.instance;
  }

  // Constructor

  private WorkspaceProperties() throws CouldNotLoadWorkspacePropertiesException {
    Properties prop = new Properties();
    try {
      File workspaceFile = getPropertiesFile();
      log.debug("workspaceFile=" + workspaceFile);
      prop.load(new FileInputStream(workspaceFile));
      this.autogenerateOnChanges = AUTOGENERATE_VALUE_ON.equals(prop.getProperty(AUTOGENERATE_ATT));
    } catch (FileNotFoundException e) {
      this.autogenerateOnChanges = false;
    } catch (IOException e) {
      throw new CouldNotLoadWorkspacePropertiesException(e.getMessage());
    }
  }

  // Behavior

  public static WorkspaceProperties load() throws CouldNotLoadWorkspacePropertiesException {
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream(getPropertiesFile()));
      WorkspaceProperties wp = new WorkspaceProperties();
      wp.autogenerateOnChanges = AUTOGENERATE_VALUE_ON.equals(prop.getProperty(AUTOGENERATE_ATT));
      return wp;
    } catch (FileNotFoundException e) {
      return new WorkspaceProperties();
    } catch (IOException e) {
      throw new CouldNotLoadWorkspacePropertiesException(e.getMessage());
    }
  }

  public void save() throws CouldNotSaveWorkspacePropertiesException {
    Properties prop = new Properties();
    prop.setProperty(AUTOGENERATE_ATT, this.autogenerateOnChanges ? AUTOGENERATE_VALUE_ON : AUTOGENERATE_VALUE_OFF);
    try {
      prop.store(new FileOutputStream(getPropertiesFile()), "HotRod workspace properties\n");
    } catch (FileNotFoundException e) {
      throw new CouldNotSaveWorkspacePropertiesException(e.getMessage());
    } catch (IOException e) {
      throw new CouldNotSaveWorkspacePropertiesException(e.getMessage());
    }
  }

  private static File getPropertiesFile() {
    IPath wp = getBaseDir().append(STATE_FILE_NAME);
    return wp.toFile();
  }

  private static IPath getBaseDir() {
    return Platform.getStateLocation(Activator.getPluginBundle());
  }

  public ProjectProperties getProjectProperties(final IProject project) {
    IPath projectDir = getBaseDir().append(project.getName());
    if (!projectDir.toFile().exists()) {
      projectDir.toFile().mkdir();
    } else if (!projectDir.toFile().isDirectory()) {
      projectDir.toFile().delete();
      projectDir.toFile().mkdir();
    }
    return ProjectProperties.load(projectDir);
  }

  // Getters & Setters

  public boolean isAutogenerateOnChanges() {
    return autogenerateOnChanges;
  }

  public void flipAutogenerateOnChanges() {
    this.autogenerateOnChanges = !this.autogenerateOnChanges;
  }

  // Exceptions

  public static class CouldNotSaveWorkspacePropertiesException extends Exception {

    private static final long serialVersionUID = 1L;

    private CouldNotSaveWorkspacePropertiesException(final String message) {
      super(message);
    }

  }

  public static class CouldNotLoadWorkspacePropertiesException extends Exception {

    private static final long serialVersionUID = 1L;

    private CouldNotLoadWorkspacePropertiesException(final String message) {
      super(message);
    }

  }

}
