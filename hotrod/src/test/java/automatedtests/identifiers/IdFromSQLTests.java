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
import junit.framework.TestCase;

public class IdFromSQLTests extends TestCase {

  public IdFromSQLTests(final String txt) {
    super(txt);
  }

  public void testfromTypedSQLCommon() throws SQLException, InvalidIdentifierException {

    DatabaseAdapter uAdapter = new TestDatabaseAdapter(getDatabaseMetaData(), CaseSensitiveness.UPPERCASE);

    // a
    // A
    // _
    // __
    // ___
    // " "
    // " " (2 spaces)
    // " " (3 spaces)
    // " _"
    // "_ "

    matchesSQL(Id.fromTypedSQL("a", uAdapter), "A", "a", "A", "a", "getA", "setA", "A", "a", "a");

    matchesSQL(Id.fromTypedSQL("A", uAdapter), "A", "a", "A", "a", "getA", "setA", "A", "a", "A");
    matchesSQL(Id.fromTypedSQL("_", uAdapter), "_", "_", "_", "_", "get_", "set_", "_", "'_'", "_");
    matchesSQL(Id.fromTypedSQL("__", uAdapter), "__", "__", "__", "__", "get__", "set__", "__", "'__'", "__");
    matchesSQL(Id.fromTypedSQL("___", uAdapter), "___", "___", "___", "___", "get___", "set___", "___", "'___'", "___");

    matchesSQL(Id.fromTypedSQL(" ", uAdapter), "_", "_", "_", "_", "get_", "set_", " ", "' '", "_");
    matchesSQL(Id.fromTypedSQL("  ", uAdapter), "__", "__", "__", "__", "get__", "set__", "  ", "'  '", "__");
    matchesSQL(Id.fromTypedSQL("   ", uAdapter), "___", "___", "___", "___", "get___", "set___", "   ", "'   '", "___");

    matchesSQL(Id.fromTypedSQL(" _", uAdapter), "_", "_", "_", "_", "get_", "set_", " _", "' _'", "_");
    matchesSQL(Id.fromTypedSQL("_ ", uAdapter), "_", "_", "_", "_", "get_", "set_", "_ ", "'_ '", "_");

    // ab
    // _ab
    // ab_
    // _ab_

    matchesSQL(Id.fromTypedSQL("ab", uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", "AB", "ab", "ab");
    matchesSQL(Id.fromTypedSQL("_ab", uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", "_AB", "'_AB'", "ab");
    matchesSQL(Id.fromTypedSQL("ab_", uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", "AB_", "ab_", "ab");
    matchesSQL(Id.fromTypedSQL("_ab_", uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", "_AB_", "'_AB_'", "ab");

    // __ab
    // ab__
    // __ab__

    matchesSQL(Id.fromTypedSQL("__ab", uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", "__AB", "'__AB'", "ab");
    matchesSQL(Id.fromTypedSQL("ab__", uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", "AB__", "ab__", "ab");
    matchesSQL(Id.fromTypedSQL("__ab__", uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", "__AB__", "'__AB__'",
        "ab");

    // a_b
    // a__b

    matchesSQL(Id.fromTypedSQL("a_b", uAdapter), "AB", "aB", "A_B", "a-b", "getAB", "setAB", "A_B", "a_b", "a", "b");
    matchesSQL(Id.fromTypedSQL("a__b", uAdapter), "AB", "aB", "A_B", "a-b", "getAB", "setAB", "A__B", "a__b", "a", "b");

    // abc
    // abc123
    // abc_123
    // abc__123
    // abc_123_

    matchesSQL(Id.fromTypedSQL("abc", uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "abc", "abc");
    matchesSQL(Id.fromTypedSQL("abc123", uAdapter), "Abc123", "abc123", "ABC123", "abc123", "getAbc123", "setAbc123",
        "ABC123", "abc123", "abc123");
    matchesSQL(Id.fromTypedSQL("abc_123", uAdapter), "Abc123", "abc123", "ABC_123", "abc-123", "getAbc123", "setAbc123",
        "ABC_123", "abc_123", "abc", "123");
    matchesSQL(Id.fromTypedSQL("abc__123", uAdapter), "Abc123", "abc123", "ABC_123", "abc-123", "getAbc123",
        "setAbc123", "ABC__123", "abc__123", "abc", "123");
    matchesSQL(Id.fromTypedSQL("abc_123_", uAdapter), "Abc123", "abc123", "ABC_123", "abc-123", "getAbc123",
        "setAbc123", "ABC_123_", "abc_123_", "abc", "123");

    // 123_abc
    // abc_def
    // abc_def1
    // 1a3bc3_4d5ef6

    matchesSQL(Id.fromTypedSQL("123_abc", uAdapter), "_123Abc", "_123Abc", "_123_ABC", "123-abc", "get_123Abc",
        "set_123Abc", "123_ABC", "'123_ABC'", "123", "abc");
    matchesSQL(Id.fromTypedSQL("abc_def", uAdapter), "AbcDef", "abcDef", "ABC_DEF", "abc-def", "getAbcDef", "setAbcDef",
        "ABC_DEF", "abc_def", "abc", "def");
    matchesSQL(Id.fromTypedSQL("abc_def1", uAdapter), "AbcDef1", "abcDef1", "ABC_DEF1", "abc-def1", "getAbcDef1",
        "setAbcDef1", "ABC_DEF1", "abc_def1", "abc", "def1");
    matchesSQL(Id.fromTypedSQL("1a3bc3_4d5ef6", uAdapter), "_1a3bc34d5ef6", "_1a3bc34d5ef6", "_1A3BC3_4D5EF6",
        "1a3bc3-4d5ef6", "get_1a3bc34d5ef6", "set_1a3bc34d5ef6", "1A3BC3_4D5EF6", "'1A3BC3_4D5EF6'", "1a3bc3",
        "4d5ef6");

    // " a"
    // "a "
    // " a "
    // " ab "
    // "ab cd"
    // "ab cd ef"
    // " ab cd ef "
    // "ab1 cd_ _ef"

    matchesSQL(Id.fromTypedSQL(" a", uAdapter), "A", "a", "A", "a", "getA", "setA", " A", "' A'", "a");
    matchesSQL(Id.fromTypedSQL("a ", uAdapter), "A", "a", "A", "a", "getA", "setA", "A ", "'A '", "a");
    matchesSQL(Id.fromTypedSQL(" a ", uAdapter), "A", "a", "A", "a", "getA", "setA", " A ", "' A '", "a");
    matchesSQL(Id.fromTypedSQL(" ab ", uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", " AB ", "' AB '", "ab");
    matchesSQL(Id.fromTypedSQL("ab cd", uAdapter), "AbCd", "abCd", "AB_CD", "ab-cd", "getAbCd", "setAbCd", "AB CD",
        "'AB CD'", "ab", "cd");
    matchesSQL(Id.fromTypedSQL("ab cd ef", uAdapter), "AbCdEf", "abCdEf", "AB_CD_EF", "ab-cd-ef", "getAbCdEf",
        "setAbCdEf", "AB CD EF", "'AB CD EF'", "ab", "cd", "ef");
    matchesSQL(Id.fromTypedSQL(" ab cd ef ", uAdapter), "AbCdEf", "abCdEf", "AB_CD_EF", "ab-cd-ef", "getAbCdEf",
        "setAbCdEf", " AB CD EF ", "' AB CD EF '", "ab", "cd", "ef");
    matchesSQL(Id.fromTypedSQL("ab1 cd_ _ef", uAdapter), "Ab1Cd__ef", "ab1Cd__ef", "AB1_CD___EF", "ab1-cd_-_ef",
        "getAb1Cd__ef", "setAb1Cd__ef", "AB1 CD_ _EF", "'AB1 CD_ _EF'", "ab1", "cd_", "_ef");

    // Blanks-only identifiers

    matchesSQL(Id.fromTypedSQL("' '", uAdapter), "_", "_", "_", "_", "get_", "set_", " ", "' '", "_");
    matchesSQL(Id.fromTypedSQL("'  '", uAdapter), "__", "__", "__", "__", "get__", "set__", "  ", "'  '", "__");
    matchesSQL(Id.fromTypedSQL("'   '", uAdapter), "___", "___", "___", "___", "get___", "set___", "   ", "'   '",
        "___");

    // Quoting

    matchesSQL(Id.fromTypedSQL("'aBc'", uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "aBc", "'aBc'",
        "aBc");
    matchesSQL(Id.fromTypedSQL("\"aBc\"", uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "aBc", "'aBc'",
        "aBc");
    matchesSQL(Id.fromTypedSQL("`aBc`", uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "aBc", "'aBc'",
        "aBc");
    matchesSQL(Id.fromTypedSQL("[aBc]", uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "aBc", "'aBc'",
        "aBc");

  }

  public void testRealcases() throws SQLException, InvalidIdentifierException {

    DatabaseAdapter lAdapter = new TestDatabaseAdapter(getDatabaseMetaData(), CaseSensitiveness.LOWERCASE);

    String expected = "car#part$Price";

    Id id = Id.fromTypedSQL("'" + expected + "'", lAdapter);
    String actual = id.getCanonicalSQLName();

    assertEquals("Expected '" + expected + "' but found '" + actual + "'", expected, actual);

  }

  public void testfromTypedSQLUpperCaseDefaultAdapter() throws InvalidIdentifierException, SQLException {
    DatabaseAdapter uAdapter = new TestDatabaseAdapter(getDatabaseMetaData(), CaseSensitiveness.UPPERCASE);

    // Unquoted

    matchesSQL(Id.fromTypedSQL("abc", uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "abc", "abc");
    matchesSQL(Id.fromTypedSQL("ABC", uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "abc", "ABC");
    matchesSQL(Id.fromTypedSQL("AbC", uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "abc", "AbC");
    matchesSQL(Id.fromTypedSQL("aBc", uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "abc", "aBc");

    // Quoted

    matchesSQL(Id.fromTypedSQL("'abc'", uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "'abc'",
        "abc");
    matchesSQL(Id.fromTypedSQL("'ABC'", uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "abc", "ABC");
    matchesSQL(Id.fromTypedSQL("'AbC'", uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "AbC", "'AbC'",
        "AbC");
    matchesSQL(Id.fromTypedSQL("'aBc'", uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "aBc", "'aBc'",
        "aBc");
  }

  public void testfromTypedSQLLowerCaseDefaultAdapter() throws InvalidIdentifierException, SQLException {
    DatabaseAdapter lAdapter = new TestDatabaseAdapter(getDatabaseMetaData(), CaseSensitiveness.LOWERCASE);

    // Unquoted

    matchesSQL(Id.fromTypedSQL("abc", lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "abc");
    matchesSQL(Id.fromTypedSQL("ABC", lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "ABC");
    matchesSQL(Id.fromTypedSQL("AbC", lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "AbC");
    matchesSQL(Id.fromTypedSQL("aBc", lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "aBc");

    // Quoted

    matchesSQL(Id.fromTypedSQL("'abc'", lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "abc");
    matchesSQL(Id.fromTypedSQL("'ABC'", lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "'ABC'",
        "ABC");
    matchesSQL(Id.fromTypedSQL("'AbC'", lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "AbC", "'AbC'",
        "AbC");
    matchesSQL(Id.fromTypedSQL("'aBc'", lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "aBc", "'aBc'",
        "aBc");
  }

  public void testfromTypedSQLCaseSensitiveAdapter() throws InvalidIdentifierException, SQLException {
    DatabaseAdapter sAdapter = new TestDatabaseAdapter(getDatabaseMetaData(), CaseSensitiveness.SENSITIVE);

    // Unquoted

    matchesSQL(Id.fromTypedSQL("abc", sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "abc");
    matchesSQL(Id.fromTypedSQL("ABC", sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "ABC", "ABC");
    matchesSQL(Id.fromTypedSQL("AbC", sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "AbC", "AbC", "AbC");
    matchesSQL(Id.fromTypedSQL("aBc", sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "aBc", "aBc", "aBc");

    // Quoted

    matchesSQL(Id.fromTypedSQL("'abc'", sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "abc");
    matchesSQL(Id.fromTypedSQL("'ABC'", sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "ABC", "ABC");
    matchesSQL(Id.fromTypedSQL("'AbC'", sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "AbC", "AbC", "AbC");
    matchesSQL(Id.fromTypedSQL("'aBc'", sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "aBc", "aBc", "aBc");
  }

  public void testfromTypedSQLCaseInsensitiveAdapter() throws InvalidIdentifierException, SQLException {
    DatabaseAdapter iAdapter = new TestDatabaseAdapter(getDatabaseMetaData(), CaseSensitiveness.INSENSITIVE);

    // Unquoted

    matchesSQL(Id.fromTypedSQL("abc", iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "abc");
    matchesSQL(Id.fromTypedSQL("ABC", iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "ABC");
    matchesSQL(Id.fromTypedSQL("AbC", iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "AbC");
    matchesSQL(Id.fromTypedSQL("aBc", iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "aBc");

    // Quoted

    matchesSQL(Id.fromTypedSQL("'abc'", iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "abc");
    matchesSQL(Id.fromTypedSQL("'ABC'", iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "abc", "ABC");
    matchesSQL(Id.fromTypedSQL("'AbC'", iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "AbC", "abc", "AbC");
    matchesSQL(Id.fromTypedSQL("'aBc'", iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "aBc", "abc", "aBc");
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
    ResultSet rs = (ResultSet) Proxy.newProxyInstance(IdFromSQLTests.class.getClassLoader(),
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
    DatabaseMetaData dm = (DatabaseMetaData) Proxy.newProxyInstance(IdFromSQLTests.class.getClassLoader(),
        new Class<?>[] { DatabaseMetaData.class }, h);
    return dm;
  }

  // Helpers

  private void matchesSQL(final Id id, final String javaClassName, final String javaMemberName,
      final String constantName, final String dashedName, final String getter, final String setter,
      final String TypedSQLName, final String renderedSQLName, final String... tokens) {
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

    assertEquals("Expected TypedSQLName '" + TypedSQLName + "' but found '" + id.getCanonicalSQLName() + "'",
        TypedSQLName, id.getCanonicalSQLName());
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
