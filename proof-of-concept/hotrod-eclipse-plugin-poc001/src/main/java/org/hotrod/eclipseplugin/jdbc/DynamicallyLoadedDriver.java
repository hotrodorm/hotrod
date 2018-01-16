package org.hotrod.eclipseplugin.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.hotrod.eclipseplugin.jdbc.DriverFiles.DriverFileLocation;

public class DynamicallyLoadedDriver {

  private IProject project;
  private String driverClassPath;
  private String driverClassName;

  public DynamicallyLoadedDriver(final IProject project, final String driverClassPath, final String driverClassName)
      throws SQLException {
    this.project = project;
    this.driverClassPath = driverClassPath;
    this.driverClassName = driverClassName;

    // Load/retrieve driver file location

    DriverFileLocation fl = DriverFiles.load(project, driverClassPath);

    try {
      Class<?> classToLoad = Class.forName(this.driverClassName, true, fl.getClassLoader());
      Driver driver = (Driver) classToLoad.newInstance();
      DriverManager.registerDriver(new DriverProxy(driver));
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

  }

}
