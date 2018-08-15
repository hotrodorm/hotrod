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
import org.hotrod.utils.identifiers.ObjectId;

import automatedtests.identifiers.TestDatabaseAdapter.CaseSensitiveness;
import automatedtests.identifiers.TestDatabaseAdapter.CatalogSchemaSupport;
import junit.framework.TestCase;

public class ObjectIdTests extends TestCase {

  public ObjectIdTests(final String txt) {
    super(txt);
  }

  public void testFromSQLCommon() throws SQLException, InvalidIdentifierException {

    DatabaseAdapter uAdapter = new TestDatabaseAdapter(getDatabaseMetaData(), CaseSensitiveness.UPPERCASE,
        CatalogSchemaSupport.SCHEMA_ONLY);

    try {
      @SuppressWarnings("unused")
      ObjectId id = new ObjectId(null, null, null, uAdapter);
      fail("identifier cannot have a null object.");
    } catch (InvalidIdentifierException e) {
      // OK
    }

    {
      ObjectId t = new ObjectId(null, null, Id.fromTypedSQL("abc", uAdapter), uAdapter);
      ObjectId te = new ObjectId(null, null, Id.fromTypedSQL("abc", uAdapter), uAdapter);
      ObjectId td = new ObjectId(null, null, Id.fromTypedSQL("def", uAdapter), uAdapter);
      assertTrue("t=te", t.equals(te));
      assertTrue("t!=td", !t.equals(td));
    }

    {
      ObjectId c = new ObjectId(Id.fromTypedSQL("catalog1", uAdapter), null, Id.fromTypedSQL("abc", uAdapter),
          uAdapter);
      ObjectId ce = new ObjectId(Id.fromTypedSQL("catalog1", uAdapter), null, Id.fromTypedSQL("abc", uAdapter),
          uAdapter);
      ObjectId cd1 = new ObjectId(Id.fromTypedSQL("catalog2", uAdapter), null, Id.fromTypedSQL("abc", uAdapter),
          uAdapter);
      ObjectId cd2 = new ObjectId(Id.fromTypedSQL("catalog1", uAdapter), null, Id.fromTypedSQL("def", uAdapter),
          uAdapter);
      assertTrue("c=ce", c.equals(ce));
      assertTrue("c!=cd1", !c.equals(cd1));
      assertTrue("c!=cd2", !c.equals(cd2));
    }

    {
      ObjectId s = new ObjectId(null, Id.fromTypedSQL("schema1", uAdapter), Id.fromTypedSQL("abc", uAdapter), uAdapter);
      ObjectId se = new ObjectId(null, Id.fromTypedSQL("schema1", uAdapter), Id.fromTypedSQL("abc", uAdapter),
          uAdapter);
      ObjectId sd1 = new ObjectId(null, Id.fromTypedSQL("schema2", uAdapter), Id.fromTypedSQL("abc", uAdapter),
          uAdapter);
      ObjectId sd2 = new ObjectId(null, Id.fromTypedSQL("schema1", uAdapter), Id.fromTypedSQL("def", uAdapter),
          uAdapter);
      assertTrue("s=se", s.equals(se));
      assertTrue("s!=sd1", !s.equals(sd1));
      assertTrue("s!=sd2", !s.equals(sd2));
    }

    {
      ObjectId cs = new ObjectId(Id.fromTypedSQL("catalog1", uAdapter), Id.fromTypedSQL("schema1", uAdapter),
          Id.fromTypedSQL("abc", uAdapter), uAdapter);
      ObjectId cse = new ObjectId(Id.fromTypedSQL("catalog1", uAdapter), Id.fromTypedSQL("schema1", uAdapter),
          Id.fromTypedSQL("abc", uAdapter), uAdapter);
      ObjectId csd1 = new ObjectId(Id.fromTypedSQL("catalog2", uAdapter), Id.fromTypedSQL("schema1", uAdapter),
          Id.fromTypedSQL("abc", uAdapter), uAdapter);
      ObjectId csd2 = new ObjectId(Id.fromTypedSQL("catalog1", uAdapter), Id.fromTypedSQL("schema2", uAdapter),
          Id.fromTypedSQL("abc", uAdapter), uAdapter);
      ObjectId csd3 = new ObjectId(Id.fromTypedSQL("catalog2", uAdapter), Id.fromTypedSQL("schema2", uAdapter),
          Id.fromTypedSQL("abc", uAdapter), uAdapter);
      assertTrue("cs=cse", cs.equals(cse));
      assertTrue("cs!=csd1", !cs.equals(csd1));
      assertTrue("cs!=csd2", !cs.equals(csd2));
      assertTrue("cs!=csd3", !cs.equals(csd3));
    }

    // No/default catalog, no/default schema

    {
      ObjectId s = new ObjectId(null, null, Id.fromTypedSQL("abc", uAdapter), uAdapter);
      String expected = "abc";
      String actual = s.getRenderedSQLName();
      assertEquals("Invalid rendered name: expected '" + expected + "' and found '" + actual + "'.", expected, actual);
    }

    // No/default catalog, specified schema (Oracle, DB2, PostgreSQL, H2, Derby,
    // HyperSQL)

    {
      ObjectId s = new ObjectId(null, Id.fromTypedSQL("schema1", uAdapter), Id.fromTypedSQL("abc", uAdapter), uAdapter);
      String expected = "schema1.abc";
      String actual = s.getRenderedSQLName();
      assertEquals("Invalid rendered name: expected '" + expected + "' and found '" + actual + "'.", expected, actual);
    }

    // Specified catalog, default schema (SQL Server, Sybase ASE)

    {
      DatabaseAdapter csAdapter = new TestDatabaseAdapter(getDatabaseMetaData(), CaseSensitiveness.INSENSITIVE,
          CatalogSchemaSupport.CATALOG_AND_SCHEMA);
      ObjectId s = new ObjectId(Id.fromTypedSQL("db1", csAdapter), null, Id.fromTypedSQL("abc", csAdapter), csAdapter);
      String expected = "db1..abc";
      String actual = s.getRenderedSQLName();
      assertEquals("Invalid rendered name: expected '" + expected + "' and found '" + actual + "'.", expected, actual);
    }

    // Specified catalog, schema not supported (MySQL, MariaDB)

    {
      DatabaseAdapter cAdapter = new TestDatabaseAdapter(getDatabaseMetaData(), CaseSensitiveness.SENSITIVE,
          CatalogSchemaSupport.CATALOG_ONLY);
      ObjectId s = new ObjectId(Id.fromTypedSQL("db1", cAdapter), null, Id.fromTypedSQL("abc", cAdapter), cAdapter);
      String expected = "db1.abc";
      String actual = s.getRenderedSQLName();
      assertEquals("Invalid rendered name: expected '" + expected + "' and found '" + actual + "'.", expected, actual);
    }

    // Specified catalog, specified schema (SQL Server, Sybase ASE)

    {
      ObjectId s = new ObjectId(Id.fromTypedSQL("db1", uAdapter), Id.fromTypedSQL("schema1", uAdapter),
          Id.fromTypedSQL("abc", uAdapter), uAdapter);
      String expected = "db1.schema1.abc";
      String actual = s.getRenderedSQLName();
      assertEquals("Invalid rendered name: expected '" + expected + "' and found '" + actual + "'.", expected, actual);
    }

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
    ResultSet rs = (ResultSet) Proxy.newProxyInstance(ObjectIdTests.class.getClassLoader(),
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
    DatabaseMetaData dm = (DatabaseMetaData) Proxy.newProxyInstance(ObjectIdTests.class.getClassLoader(),
        new Class<?>[] { DatabaseMetaData.class }, h);
    return dm;
  }

  // Helpers

  @SuppressWarnings("unused")
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
      assertEquals("name part[" + i + "] '" + id.getCanonicalParts().get(i) + "' <> token '" + tokens[i] + "'",
          id.getCanonicalParts().get(i).getToken(), tokens[i]);
    }
  }

  private static void log(final String txt) {
    // System.out.println("[LOG] " + txt);
  }
}
