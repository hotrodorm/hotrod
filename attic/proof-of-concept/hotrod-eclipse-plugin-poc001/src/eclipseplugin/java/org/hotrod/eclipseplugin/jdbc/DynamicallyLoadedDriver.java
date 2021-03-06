package org.hotrod.eclipseplugin.jdbc;

import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.eclipse.core.resources.IProject;

public class DynamicallyLoadedDriver {

  private IProject project;
  private List<String> driverClassPathEntries;
  private String driverClassName;

  private static final boolean USE_SEPARATE_CLASS_LOADER = true;

  private URLClassLoader classLoader;
  private DriverProxy driverProxy;

  public DynamicallyLoadedDriver(final IProject project, final List<String> driverClassPathEntries,
      final String driverClassName) throws SQLException {
    log("[DLD] starting...");
    this.project = project;
    this.driverClassPathEntries = driverClassPathEntries;
    this.driverClassName = driverClassName;

    // Load/retrieve driver file location

    if (USE_SEPARATE_CLASS_LOADER) {
      SeparateDriverLoader dl = new SeparateDriverLoader(this.project, this.driverClassPathEntries);
      this.classLoader = dl.getLoader();
    } else {
      for (String classPathEntry : this.driverClassPathEntries) {
        log("[DLD] classPathEntry=" + classPathEntry);
        DriverFiles.load(this.project, classPathEntry);
      }
      this.classLoader = DriverFiles.getClassLoader();
    }

    try {
      log("[DLD] will load class: " + this.driverClassName);
      Class<?> classToLoad = Class.forName(this.driverClassName, true, classLoader);
      Driver driver = (Driver) classToLoad.newInstance();
      this.driverProxy = new DriverProxy(driver);
      DriverManager.registerDriver(this.driverProxy);
      log("[DLD] loaded.");
    } catch (ClassNotFoundException e) {
      throw new SQLException("Could not load JDBC driver. Class " + this.driverClassName + " not found");
    } catch (InstantiationException e) {
      throw new SQLException("Could not load JDBC driver.", e);
    } catch (IllegalAccessException e) {
      throw new SQLException("Could not load JDBC driver.", e);
    }

  }

  public Connection getConnection(final String url, final String username, final String password) throws SQLException {
    return DriverManager.getConnection(url, username, password);
  }

  public void close() throws SQLException {
    DriverManager.deregisterDriver(this.driverProxy);
  }

  // Utility classes

  private static class DriverProxy implements Driver {

    private Driver driver;

    private DriverProxy(final Driver driver) {
      this.driver = driver;
    }

    @Override
    public boolean acceptsURL(final String url) throws SQLException {
      return this.driver.acceptsURL(url);
    }

    @Override
    public Connection connect(final String url, final Properties properties) throws SQLException {
      return this.driver.connect(url, properties);
    }

    @Override
    public int getMajorVersion() {
      return this.driver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
      return this.driver.getMinorVersion();
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties properties) throws SQLException {
      return this.driver.getPropertyInfo(url, properties);
    }

    @Override
    public boolean jdbcCompliant() {
      return this.driver.jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
      return this.driver.getParentLogger();
    }

  }

  public static void log(final String txt) {
    // System.out.println(txt);
  }

}
