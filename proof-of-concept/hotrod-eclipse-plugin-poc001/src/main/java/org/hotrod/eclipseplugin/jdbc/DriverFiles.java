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

public class DriverFiles {

  private static Set<DriverFileLocation> driverFiles = new HashSet<DriverFileLocation>();

  public static synchronized DriverFileLocation load(final IProject project, final String driverClassPath)
      throws SQLException {
    DriverFileLocation df = new DriverFileLocation(project, driverClassPath);

    // Return the existing one if already loaded

    for (DriverFileLocation existing : driverFiles) {
      if (existing.equals(df)) {
        return existing;
      }
    }

    // Otherwise, load the new driver

    IFile ifile = project.getFile(driverClassPath);
    File file = ifile.getLocation().toFile();
    if (!file.exists()) {
      throw new SQLException("Could not load JDBC driver. File not found at " + file.getPath());
    }

    final URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    df.classLoader = loader;

    try {
      Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
      method.setAccessible(true);
      method.invoke(loader, file.toURI().toURL());
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
    return df;
  }

  public static class DriverFileLocation {

    private IProject project;
    private String driverClassPath;
    private URLClassLoader classLoader;

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

    public URLClassLoader getClassLoader() {
      return classLoader;
    }

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

}
