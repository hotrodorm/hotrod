package automatedtests.identifiers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.utils.identifiers.Id;

import automatedtests.identifiers.TestDatabaseAdapter.CaseSensitiveness;
import automatedtests.identifiers.TestDatabaseAdapter.CatalogSchemaSupport;
import junit.framework.TestCase;

public class IdSpecialCharactersTests extends TestCase {

  public IdSpecialCharactersTests(final String txt) {
    super(txt);
  }

  public void testInitial() throws SQLException, InvalidIdentifierException {
    DatabaseAdapter lAdapter = new TestDatabaseAdapter(getDatabaseMetaData(), CaseSensitiveness.LOWERCASE,
        CatalogSchemaSupport.SCHEMA_ONLY);
    Id id = Id.fromTypedSQL("'%discount'", lAdapter);

    assertEquals("%discount", id.getCanonicalSQLName());
    assertEquals("_discount", id.getJavaClassName());
    assertEquals("_discount", id.getJavaMemberName());
  }

  public void testMiddle() throws SQLException, InvalidIdentifierException {
    DatabaseAdapter lAdapter = new TestDatabaseAdapter(getDatabaseMetaData(), CaseSensitiveness.LOWERCASE,
        CatalogSchemaSupport.SCHEMA_ONLY);
    Id id = Id.fromTypedSQL("'car#part$Price'", lAdapter);

    assertEquals("car#part$Price", id.getCanonicalSQLName());
    assertEquals("Car_part_price", id.getJavaClassName());
    assertEquals("car_part_price", id.getJavaMemberName());
  }

  public void testEnd() throws SQLException, InvalidIdentifierException {
    DatabaseAdapter lAdapter = new TestDatabaseAdapter(getDatabaseMetaData(), CaseSensitiveness.LOWERCASE,
        CatalogSchemaSupport.SCHEMA_ONLY);
    Id id = Id.fromTypedSQL("'part#'", lAdapter);

    assertEquals("part#", id.getCanonicalSQLName());
    assertEquals("Part_", id.getJavaClassName());
    assertEquals("part_", id.getJavaMemberName());
  }

  private ResultSet getResultSet() {
    InvocationHandler h = new InvocationHandler() {
      @Override
      public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if ("next".equals(method.getName()) && (args == null || args.length == 0)) {
          return false;
        }
        return null;
      }
    };
    ResultSet rs = (ResultSet) Proxy.newProxyInstance(IdFromSQLTests.class.getClassLoader(),
        new Class<?>[] { ResultSet.class }, h);
    return rs;
  }

  private DatabaseMetaData getDatabaseMetaData() {
    final ResultSet rs = getResultSet();
    InvocationHandler h = new InvocationHandler() {
      @Override
      public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        // log("$$$ method=" + method + " args=" + args);
        if ("getIdentifierQuoteString".equals(method.getName()) && (args == null || args.length == 0)) {
          return "\"";
        }
        if ("getTypeInfo".equals(method.getName()) && (args == null || args.length == 0)) {
          return rs;
        }
        return null;
      }
    };
    DatabaseMetaData dm = (DatabaseMetaData) Proxy.newProxyInstance(IdFromSQLTests.class.getClassLoader(),
        new Class<?>[] { DatabaseMetaData.class }, h);
    return dm;
  }

}
