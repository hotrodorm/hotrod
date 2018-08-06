package automatedtests.identifiers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.utils.identifiers2.Id;
import org.hotrod.utils.identifiers2.Id.InvalidIdentifierException;

import automatedtests.identifiers.TestDatabaseAdapter.CaseSensitiveness;
import junit.framework.TestCase;

public class IdFromSQLWithJavaTests extends TestCase {

  public IdFromSQLWithJavaTests(final String txt) {
    super(txt);
  }

  public void testFromSQLWithJavaClass() throws SQLException, InvalidIdentifierException {

    DatabaseAdapter uAdapter = new TestDatabaseAdapter(null, getDatabaseMetaData(), CaseSensitiveness.UPPERCASE);

    matchesSQL(Id.fromSQLAndJavaClass("sql", uAdapter, "A"), "A", "a", "A", "a", "getA", "setA", "SQL", "sql", "sql");

    try {
      matchesSQL(Id.fromSQLAndJavaClass("sql", uAdapter, "a"), "A", "a", "A", "a", "getA", "setA", "SQL", "sql", "sql");
      fail("Java class cannot start with a lower case letter.");
    } catch (InvalidIdentifierException e) {
      // OK
    }

    matchesSQL(Id.fromSQLAndJavaClass("sql", uAdapter, "Abc123"), "Abc123", "abc123", "ABC123", "abc123", "getAbc123",
        "setAbc123", "SQL", "sql", "sql");

  }

  public void testFromSQLWithJavaMember() throws SQLException, InvalidIdentifierException {

    DatabaseAdapter uAdapter = new TestDatabaseAdapter(null, getDatabaseMetaData(), CaseSensitiveness.UPPERCASE);

    matchesSQL(Id.fromSQLAndJavaMember("sql", uAdapter, "a"), "A", "a", "A", "a", "getA", "setA", "SQL", "sql", "sql");

    try {
      matchesSQL(Id.fromSQLAndJavaMember("sql", uAdapter, "A"), "A", "a", "A", "a", "getA", "setA", "SQL", "sql",
          "sql");
      fail("Java class cannot start with an upper case letter.");
    } catch (InvalidIdentifierException e) {
      // OK
    }

    matchesSQL(Id.fromSQLAndJavaMember("sql", uAdapter, "abc123"), "Abc123", "abc123", "ABC123", "abc123", "getAbc123",
        "setAbc123", "SQL", "sql", "sql");

  }

  // Helpers

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
    ResultSet rs = (ResultSet) Proxy.newProxyInstance(IdFromSQLWithJavaTests.class.getClassLoader(),
        new Class<?>[] { ResultSet.class }, h);
    return rs;
  }

  private DatabaseMetaData getDatabaseMetaData() {
    final ResultSet rs = getResultSet();
    InvocationHandler h = new InvocationHandler() {
      @Override
      public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        log("$$$ method=" + method + " args=" + args);
        if ("getIdentifierQuoteString".equals(method.getName()) && (args == null || args.length == 0)) {
          return "\"";
        }
        if ("getTypeInfo".equals(method.getName()) && (args == null || args.length == 0)) {
          return rs;
        }
        return null;
      }
    };
    DatabaseMetaData dm = (DatabaseMetaData) Proxy.newProxyInstance(IdFromSQLWithJavaTests.class.getClassLoader(),
        new Class<?>[] { DatabaseMetaData.class }, h);
    return dm;
  }

  // Helpers

  private void matchesSQL(final Id id, final String javaClassName, final String javaMemberName,
      final String constantName, final String dashedName, final String getter, final String setter,
      final String canonicalSQLName, final String renderedSQLName, final String... tokens) {
    assertNotNull("id should no be null", id);
    assertEquals("Expected javaClassName '" + javaClassName + "' but found '" + id.getJavaClassName() + "'",
        javaClassName, id.getJavaClassName());
    assertEquals("Expected javaMemberName '" + javaMemberName + "' but found '" + id.getJavaMemberName() + "'",
        javaMemberName, id.getJavaMemberName());
    assertEquals("Expected constantName '" + constantName + "' but found '" + id.getJavaConstantName() + "'",
        constantName, id.getJavaConstantName());
    assertEquals("Expected dashedName '" + dashedName + "' but found '" + id.getDashedName() + "'", dashedName,
        id.getDashedName());

    assertEquals("Expected getter '" + getter + "' but found '" + id.getJavaGetter() + "'", getter, id.getJavaGetter());
    assertEquals("Expected setter '" + setter + "' but found '" + id.getJavaSetter() + "'", setter, id.getJavaSetter());

    assertEquals("Expected canonicalSQLName '" + canonicalSQLName + "' but found '" + id.getCanonicalSQLName() + "'",
        canonicalSQLName, id.getCanonicalSQLName());
    assertEquals("Expected renderedSQLName '" + renderedSQLName + "' but found '" + id.getRenderedSQLName() + "'",
        renderedSQLName, id.getRenderedSQLName());

    assertNotNull("tokens should not be null", tokens);
    assertNotNull("canonicalparts should not be null", id.getCanonicalParts());
    assertEquals(
        "canonicalparts[" + id.getCanonicalParts().size() + "] number does not match tokens[" + tokens.length + "]",
        id.getCanonicalParts().size(), tokens.length);
    for (int i = 0; i < tokens.length; i++) {
      assertEquals("canonical part[" + i + "] '" + id.getCanonicalParts().get(i) + "' <> token '" + tokens[i] + "'",
          id.getCanonicalParts().get(i).getToken(), tokens[i]);
    }
  }

  private static void log(final String txt) {
    // System.out.println("[LOG] " + txt);
  }
}
