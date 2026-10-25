package org.hotrod.torcs.plan;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

class PlanRetrieverFactoryTest {

  @Test
  void selectsSupportedDatabaseRetrievers() throws Exception {
    assertInstanceOf(PostgreSQLPlanRetriever.class, factoryFor("PostgreSQL", "16.0"));
    assertInstanceOf(SQLServerPlanRetriever.class, factoryFor("Microsoft SQL Server", "16.0"));
    assertInstanceOf(MariaDBPlanRetriever.class, factoryFor("MySQL", "MariaDB 10.11"));
  }

  @Test
  void nullProductVersionDoesNotCauseNullPointerException() throws Exception {
    assertInstanceOf(MySQLPlanRetriever.class, factoryFor("MySQL", null));
  }

  @Test
  void rejectsUnsupportedDatabaseProducts() {
    assertThrows(PlanRetrieverFactory.UnsupportedTorcsDatabaseException.class,
        () -> factoryFor("Unknown Database", "1.0"));
  }

  private PlanRetriever factoryFor(String productName, String productVersion)
      throws SQLException, PlanRetrieverFactory.UnsupportedTorcsDatabaseException {
    DatabaseMetaData metadata = proxy(DatabaseMetaData.class, (proxy, method, args) -> {
      switch (method.getName()) {
      case "getDatabaseProductName":
        return productName;
      case "getDatabaseProductVersion":
        return productVersion;
      case "getDatabaseMajorVersion":
        return 1;
      case "getDatabaseMinorVersion":
        return 0;
      default:
        return defaultValue(method.getReturnType());
      }
    });
    Connection connection = proxy(Connection.class, (proxy, method, args) -> {
      if (method.getName().equals("getMetaData")) {
        return metadata;
      }
      return defaultValue(method.getReturnType());
    });
    DataSource dataSource = proxy(DataSource.class, (proxy, method, args) -> {
      if (method.getName().equals("getConnection")) {
        return connection;
      }
      return defaultValue(method.getReturnType());
    });
    return PlanRetrieverFactory.getTorcsPlanRetriever(dataSource);
  }

  @SuppressWarnings("unchecked")
  private static <T> T proxy(Class<T> type, java.lang.reflect.InvocationHandler handler) {
    return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, handler);
  }

  private static Object defaultValue(Class<?> type) {
    if (!type.isPrimitive()) {
      return null;
    }
    if (type == boolean.class) {
      return false;
    }
    if (type == int.class) {
      return 0;
    }
    if (type == long.class) {
      return 0L;
    }
    if (type == float.class) {
      return 0F;
    }
    if (type == double.class) {
      return 0D;
    }
    if (type == byte.class) {
      return (byte) 0;
    }
    if (type == short.class) {
      return (short) 0;
    }
    if (type == char.class) {
      return '\0';
    }
    return null;
  }
}
