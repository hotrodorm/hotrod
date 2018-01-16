package org.hotrod.eclipseplugin.jdbc;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

// TODO: remove this class

public class SQLRunner {

  private IProject project;
  private String driverClassPath;
  private String driverClassName;
  private String url;
  private String username;
  private String password;

  public SQLRunner(final IProject project, final String driverClassPath, final String driverClassName, final String url,
      final String username, final String password) {
    this.project = project;
    this.driverClassPath = driverClassPath;
    this.driverClassName = driverClassName;
    this.url = url;
    this.username = username;
    this.password = password;

    IFile ifile = this.project.getFile(this.driverClassPath);
    File file = ifile.getLocation().toFile();
    System.out.println("file=" + file.getPath() + " fullpath=" + file.getAbsolutePath());
  }

  public void runSQL() {
    Connection conn = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    try {

      // conn = willNotWork();
      // conn = willWork();
      conn = loadDriver();

      st = conn.prepareStatement("select 6 * 7 as answer");
      rs = st.executeQuery();

      if (rs.next()) {
        int answer = rs.getInt(1);
        System.out.println("SQL: answer=" + answer);
      } else {
        System.out.println("SQL: did not receive answer.");
      }

    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      close(conn, st, rs);
    }

  }

  public static void run(final Connection conn) throws SQLException {
    PreparedStatement st = null;
    ResultSet rs = null;
    try {

      st = conn.prepareStatement("select 6 * 7 as answer");
      rs = st.executeQuery();

      if (rs.next()) {
        int answer = rs.getInt(1);
        System.out.println("SQL: answer=" + answer);
      } else {
        System.out.println("SQL: did not receive answer.");
      }

    } finally {
      close(st);
      close(rs);
    }

  }

  private static class DriverShim implements Driver {
    private Driver driver;

    DriverShim(Driver d) {
      this.driver = d;
    }

    public boolean acceptsURL(String u) throws SQLException {
      return this.driver.acceptsURL(u);
    }

    public Connection connect(String u, Properties p) throws SQLException {
      return this.driver.connect(u, p);
    }

    public int getMajorVersion() {
      return this.driver.getMajorVersion();
    }

    public int getMinorVersion() {
      return this.driver.getMinorVersion();
    }

    public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
      return this.driver.getPropertyInfo(u, p);
    }

    public boolean jdbcCompliant() {
      return this.driver.jdbcCompliant();
    }

  }

  private Connection loadDriver() throws Exception {

    IFile ifile = this.project.getFile(this.driverClassPath);
    File file = ifile.getLocation().toFile();
    if (!file.exists()) {
      throw new Exception("Could not find JDBC driver at: " + file.getPath());
    }

    final URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    System.out.println(this.getClass().getClassLoader().getClass() + " is the class loader");
    try {
      Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
      method.setAccessible(true);
      method.invoke(loader, file.toURI().toURL());

      Class<?> classToLoad = Class.forName(this.driverClassName, true, loader);
      Driver driver = (Driver) classToLoad.newInstance();
      DriverManager.registerDriver(new DriverShim(driver));
      Connection conn = DriverManager.getConnection(this.url, this.username, this.password);
      return conn;

    } catch (NoSuchMethodException e) {
      e.printStackTrace();
      throw new Exception(e);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new Exception(e);
    } catch (InstantiationException e) {
      e.printStackTrace();
      throw new Exception(e);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      throw new Exception(e);
    } catch (MalformedURLException e) {
      e.printStackTrace();
      throw new Exception(e);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new Exception(e);
    } catch (InvocationTargetException e) {
      e.printStackTrace();
      throw new Exception(e);
    }
  }

  // Utilities

  private void close(final Connection conn, final Statement st, final ResultSet rs) {
    try {
      close(rs);
    } finally {
      try {
        close(st);
      } finally {
        close(conn);
      }
    }
  }

  private static void close(final Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        // Ignore
      }
    }
  }

  private static void close(final Statement st) {
    if (st != null) {
      try {
        st.close();
      } catch (SQLException e) {
        // Ignore
      }
    }
  }

  private static void close(final ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        // Ignore
      }
    }
  }

}
