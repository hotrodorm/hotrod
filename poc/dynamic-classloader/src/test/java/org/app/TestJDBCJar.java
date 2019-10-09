package org.app;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import dynamic.classloader.ClassLoaderFactory;
import dynamic.classloader.ClassLoaderFactory.InvalidClassPathException;

public class TestJDBCJar {

  private static final String JDBC_DRIVER_CLASSPATH = "lib/postgresql-42.2.5.jre6.jar";
  private static final String JDBC_DRIVER_CLASS = "org.postgresql.Driver";
  private static final String JDBC_URL = "jdbc:postgresql://192.168.56.213:5432/postgres";
  private static final String JDBC_USERNAME = "postgres";
  private static final String JDBC_PASSWORD = "mypassword";

  public static void test() throws InvalidClassPathException, ClassNotFoundException, InstantiationException,
      IllegalAccessException, SQLException {

    // 1. Create a class loader

    List<File> entries = new ArrayList<File>();
    File dcp = new File(JDBC_DRIVER_CLASSPATH);
    entries.add(dcp);
    ClassLoader cl = ClassLoaderFactory.createClassLoader(entries);

    // 2. Load the driver

    Class<?> ldc = cl.loadClass(JDBC_DRIVER_CLASS);
    Driver ld = (Driver) ldc.newInstance();

    // 3. Get the connection

    Properties jdbcProps = new Properties();
    jdbcProps.put("user", JDBC_USERNAME);
    jdbcProps.put("password", JDBC_PASSWORD);
    Connection conn = ld.connect(JDBC_URL, jdbcProps);

    // 4. Run SQL statements

    PreparedStatement ps = conn.prepareStatement("select 3 * 5");

    ResultSet rs = ps.executeQuery();

    while (rs.next()) {
      int r = rs.getInt(1);
      System.out.println("r=" + r);
    }

    System.out.println("[ Query executed ]");

  }

}
