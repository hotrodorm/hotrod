package org.hotrod.eclipseplugin.jdbc;

import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;

public class DynamicallyLoadedDriver {

  private static final Logger log = Logger.getLogger(DynamicallyLoadedDriver.class);

  private IProject project;
  private List<String> driverClassPathEntries;
  private String driverClassName;

  private static final boolean USE_SEPARATE_CLASS_LOADER = true;

  private URLClassLoader classLoader;
  private Driver driverProxy;

  public DynamicallyLoadedDriver(final IProject project, final List<String> driverClassPathEntries,
      final String driverClassName) throws SQLException {
    log.debug("starting...");
    this.project = project;
    this.driverClassPathEntries = driverClassPathEntries;
    this.driverClassName = driverClassName;

    // Load/retrieve driver file location

    if (USE_SEPARATE_CLASS_LOADER) {
      SeparateDriverLoader dl = new SeparateDriverLoader(this.project, this.driverClassPathEntries);
      this.classLoader = dl.getLoader();
    } else {
      for (String classPathEntry : this.driverClassPathEntries) {
        log.debug("classPathEntry=" + classPathEntry);
        DriverFiles.load(this.project, classPathEntry);
      }
      this.classLoader = DriverFiles.getClassLoader();
    }

    try {
      log.debug("will load class: " + this.driverClassName);
      Class<?> classToLoad = Class.forName(this.driverClassName, true, classLoader);
      Driver driver = (Driver) classToLoad.newInstance();
      this.driverProxy = this.getDriverProxy(driver);
      DriverManager.registerDriver(this.driverProxy);
      log.debug("loaded.");
    } catch (ClassNotFoundException e) {
      throw new SQLException("Could not load JDBC driver. Class " + this.driverClassName + " not found");
    } catch (InstantiationException e) {
      throw new SQLException("Could not load JDBC driver.", e);
    } catch (IllegalAccessException e) {
      throw new SQLException("Could not load JDBC driver.", e);
    }

  }

  public Driver getDriverProxy(final Driver driver) {
    Driver d = (Driver) java.lang.reflect.Proxy.newProxyInstance(Driver.class.getClassLoader(),
        new java.lang.Class<?>[] { Driver.class }, new java.lang.reflect.InvocationHandler() {
          @Override
          public Object invoke(final Object proxy, final java.lang.reflect.Method method, final Object[] args)
              throws java.lang.Throwable {
            return method.invoke(driver, args);
          }
        });
    return d;
  }

  public Connection getConnection(final String url, final String username, final String password) throws SQLException {
    return DriverManager.getConnection(url, username, password);
  }

  public void close() throws SQLException {
    DriverManager.deregisterDriver(this.driverProxy);
  }

}
