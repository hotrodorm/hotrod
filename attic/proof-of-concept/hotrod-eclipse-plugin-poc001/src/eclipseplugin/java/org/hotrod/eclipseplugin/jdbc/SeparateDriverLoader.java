package org.hotrod.eclipseplugin.jdbc;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.hotrod.eclipseplugin.jdbc.DriverFiles.DriverFileLocation;
import org.hotrod.eclipseplugin.utils.FUtil;

public class SeparateDriverLoader {

  private Set<DriverFileLocation> driverFiles = new HashSet<DriverFileLocation>();
  private URLClassLoader loader = null;

  public SeparateDriverLoader(final IProject project, final List<String> driverClassPathEntries) throws SQLException {

    log("[DriverFiles] starting...");
    for (String entry : driverClassPathEntries) {
      DriverFileLocation df = new DriverFileLocation(project, entry);

      // Return the existing one if already loaded

      for (DriverFileLocation existing : this.driverFiles) {
        if (existing.equals(df)) {
          log("[DriverFiles] already exists.");
          return;
        }
      }

      // Otherwise, load the new driver

      File file = new File(entry);
      if (!FUtil.isAbsolute(file)) {
        IFile ifile = project.getFile(entry);
        file = ifile.getLocation().toFile();
      }

      log("[DriverFiles] file=" + file.getPath());
      if (!file.exists()) {
        throw new SQLException("Could not load JDBC driver. File not found at " + file.getPath());
      }

      URL jarFile;
      try {
        jarFile = new URL("jar", "", "file:" + entry + "!/");
      } catch (MalformedURLException e1) {
        throw new SQLException("Could not load JDBC driver: invalid jar file " + entry);
      }

      this.loader = URLClassLoader.newInstance(new URL[] { jarFile }, getClass().getClassLoader());

      try {
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(this.loader, file.toURI().toURL());
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

      this.driverFiles.add(df);

    }

  }

  public URLClassLoader getLoader() {
    return this.loader;
  }

  public static void log(final String txt) {
    // System.out.println(txt);
  }

}
