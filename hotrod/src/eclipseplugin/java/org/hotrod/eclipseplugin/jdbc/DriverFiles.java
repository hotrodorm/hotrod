package org.hotrod.eclipseplugin.jdbc;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.hotrod.eclipseplugin.utils.FUtil;

public class DriverFiles {

  private static Set<DriverFileLocation> driverFiles = new HashSet<DriverFileLocation>();
  private static URLClassLoader loader = null;

  public static synchronized void load(final IProject project, final String driverClassPathEntry) throws SQLException {
    log("[DriverFiles] starting...");
    DriverFileLocation df = new DriverFileLocation(project, driverClassPathEntry);

    // Return the existing one if already loaded

    for (DriverFileLocation existing : driverFiles) {
      if (existing.equals(df)) {
        log("[DriverFiles] already exists.");
        return;
      }
    }

    // Otherwise, load the new driver

    File file = new File(driverClassPathEntry);
    if (!FUtil.isAbsolute(file)) {
      IFile ifile = project.getFile(driverClassPathEntry);
      file = ifile.getLocation().toFile();
    }

    log("[DriverFiles] file=" + file.getPath());
    if (!file.exists()) {
      throw new SQLException("Could not load JDBC driver. File not found at " + file.getPath());
    }

    if (loader == null) {
      loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    }

    try {
      Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
      method.setAccessible(true);
      method.invoke(loader, file.toURI().toURL());
      log("[DriverFiles] added!");

    } catch (SecurityException e) {
      throw new SQLException("Could not load JDBC driver " + file.getPath(), e);
    } catch (IllegalArgumentException e) {
      throw new SQLException("Could not load JDBC driver " + file.getPath(), e);
    } catch (MalformedURLException e) {
      throw new SQLException("Could not load JDBC driver " + file.getPath(), e);
    } catch (NoSuchMethodException e) {
      throw new SQLException("Could not load JDBC driver " + file.getPath(), e);
    } catch (IllegalAccessException e) {
      throw new SQLException("Could not load JDBC driver " + file.getPath(), e);
    } catch (InvocationTargetException e) {
      throw new SQLException("Could not load JDBC driver " + file.getPath(), e);
    }

    driverFiles.add(df);
  }

  public static synchronized URLClassLoader getClassLoader() {
    return loader;
  }

  public static class DriverFileLocation {

    private IProject project;
    private String driverClassPath;

    public DriverFileLocation(final IProject project, final String driverClassPath) {
      this.project = project;
      this.driverClassPath = driverClassPath;
    }

    public IProject getProject() {
      return project;
    }

    public String getDriverClassPath() {
      return driverClassPath;
    }

    // Indexable

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((driverClassPath == null) ? 0 : driverClassPath.hashCode());
      result = prime * result + ((project == null) ? 0 : project.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      DriverFileLocation other = (DriverFileLocation) obj;
      if (driverClassPath == null) {
        if (other.driverClassPath != null)
          return false;
      } else if (!driverClassPath.equals(other.driverClassPath))
        return false;
      if (project == null) {
        if (other.project != null)
          return false;
      } else if (!project.getName().equals(other.project.getName()))
        return false;
      return true;
    }

  }

  private static void log(final String txt) {
    System.out.println("[" + new Object() {
    }.getClass().getEnclosingClass().getName() + "] " + txt);
  }

}
