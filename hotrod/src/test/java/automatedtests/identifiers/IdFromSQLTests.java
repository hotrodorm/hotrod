package automatedtests.identifiers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.utils.identifiers.Id;
import org.hotrod.utils.identifiers.Id.InvalidIdentifierException;

import automatedtests.identifiers.TestDatabaseAdapter.CaseSensitiveness;
import junit.framework.TestCase;

public class IdFromSQLTests extends TestCase {

  public IdFromSQLTests(final String txt) {
    super(txt);
  }

  public void testFromSQLCommon() throws SQLException, InvalidIdentifierException {

    DatabaseAdapter uAdapter = new TestDatabaseAdapter(null, getDatabaseMetaData(), CaseSensitiveness.UPPERCASE);

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

    matchesSQL(Id.fromSQL("a", false, uAdapter), "A", "a", "A", "a", "getA", "setA", "A", "a", "a");

    matchesSQL(Id.fromSQL("A", false, uAdapter), "A", "a", "A", "a", "getA", "setA", "A", "a", "A");
    matchesSQL(Id.fromSQL("_", false, uAdapter), "_", "_", "_", "_", "get_", "set_", "_", "'_'", "_");
    matchesSQL(Id.fromSQL("__", false, uAdapter), "__", "__", "__", "__", "get__", "set__", "__", "'__'", "__");
    matchesSQL(Id.fromSQL("___", false, uAdapter), "___", "___", "___", "___", "get___", "set___", "___", "'___'",
        "___");

    matchesSQL(Id.fromSQL(" ", false, uAdapter), "_", "_", "_", "_", "get_", "set_", " ", "' '", "_");
    matchesSQL(Id.fromSQL("  ", false, uAdapter), "__", "__", "__", "__", "get__", "set__", "  ", "'  '", "__");
    matchesSQL(Id.fromSQL("   ", false, uAdapter), "___", "___", "___", "___", "get___", "set___", "   ", "'   '",
        "___");

    matchesSQL(Id.fromSQL(" _", false, uAdapter), "_", "_", "_", "_", "get_", "set_", " _", "' _'", "_");
    matchesSQL(Id.fromSQL("_ ", false, uAdapter), "_", "_", "_", "_", "get_", "set_", "_ ", "'_ '", "_");

    // ab
    // _ab
    // ab_
    // _ab_

    matchesSQL(Id.fromSQL("ab", false, uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", "AB", "ab", "ab");
    matchesSQL(Id.fromSQL("_ab", false, uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", "_AB", "'_AB'", "ab");
    matchesSQL(Id.fromSQL("ab_", false, uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", "AB_", "ab_", "ab");
    matchesSQL(Id.fromSQL("_ab_", false, uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", "_AB_", "'_AB_'", "ab");

    // __ab
    // ab__
    // __ab__

    matchesSQL(Id.fromSQL("__ab", false, uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", "__AB", "'__AB'", "ab");
    matchesSQL(Id.fromSQL("ab__", false, uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", "AB__", "ab__", "ab");
    matchesSQL(Id.fromSQL("__ab__", false, uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", "__AB__", "'__AB__'",
        "ab");

    // a_b
    // a__b

    matchesSQL(Id.fromSQL("a_b", false, uAdapter), "AB", "aB", "A_B", "a-b", "getAB", "setAB", "A_B", "a_b", "a", "b");
    matchesSQL(Id.fromSQL("a__b", false, uAdapter), "AB", "aB", "A_B", "a-b", "getAB", "setAB", "A__B", "a__b", "a",
        "b");

    // abc
    // abc123
    // abc_123
    // abc__123
    // abc_123_

    matchesSQL(Id.fromSQL("abc", false, uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "abc", "abc");
    matchesSQL(Id.fromSQL("abc123", false, uAdapter), "Abc123", "abc123", "ABC123", "abc123", "getAbc123", "setAbc123",
        "ABC123", "abc123", "abc123");
    matchesSQL(Id.fromSQL("abc_123", false, uAdapter), "Abc123", "abc123", "ABC_123", "abc-123", "getAbc123",
        "setAbc123", "ABC_123", "abc_123", "abc", "123");
    matchesSQL(Id.fromSQL("abc__123", false, uAdapter), "Abc123", "abc123", "ABC_123", "abc-123", "getAbc123",
        "setAbc123", "ABC__123", "abc__123", "abc", "123");
    matchesSQL(Id.fromSQL("abc_123_", false, uAdapter), "Abc123", "abc123", "ABC_123", "abc-123", "getAbc123",
        "setAbc123", "ABC_123_", "abc_123_", "abc", "123");

    // 123_abc
    // abc_def
    // abc_def1
    // 1a3bc3_4d5ef6

    matchesSQL(Id.fromSQL("123_abc", false, uAdapter), "_123Abc", "_123Abc", "_123_ABC", "123-abc", "get_123Abc",
        "set_123Abc", "123_ABC", "'123_ABC'", "123", "abc");
    matchesSQL(Id.fromSQL("abc_def", false, uAdapter), "AbcDef", "abcDef", "ABC_DEF", "abc-def", "getAbcDef",
        "setAbcDef", "ABC_DEF", "abc_def", "abc", "def");
    matchesSQL(Id.fromSQL("abc_def1", false, uAdapter), "AbcDef1", "abcDef1", "ABC_DEF1", "abc-def1", "getAbcDef1",
        "setAbcDef1", "ABC_DEF1", "abc_def1", "abc", "def1");
    matchesSQL(Id.fromSQL("1a3bc3_4d5ef6", false, uAdapter), "_1a3bc34d5ef6", "_1a3bc34d5ef6", "_1A3BC3_4D5EF6",
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

    matchesSQL(Id.fromSQL(" a", false, uAdapter), "A", "a", "A", "a", "getA", "setA", " A", "' A'", "a");
    matchesSQL(Id.fromSQL("a ", false, uAdapter), "A", "a", "A", "a", "getA", "setA", "A ", "'A '", "a");
    matchesSQL(Id.fromSQL(" a ", false, uAdapter), "A", "a", "A", "a", "getA", "setA", " A ", "' A '", "a");
    matchesSQL(Id.fromSQL(" ab ", false, uAdapter), "Ab", "ab", "AB", "ab", "getAb", "setAb", " AB ", "' AB '", "ab");
    matchesSQL(Id.fromSQL("ab cd", false, uAdapter), "AbCd", "abCd", "AB_CD", "ab-cd", "getAbCd", "setAbCd", "AB CD",
        "'AB CD'", "ab", "cd");
    matchesSQL(Id.fromSQL("ab cd ef", false, uAdapter), "AbCdEf", "abCdEf", "AB_CD_EF", "ab-cd-ef", "getAbCdEf",
        "setAbCdEf", "AB CD EF", "'AB CD EF'", "ab", "cd", "ef");
    matchesSQL(Id.fromSQL(" ab cd ef ", false, uAdapter), "AbCdEf", "abCdEf", "AB_CD_EF", "ab-cd-ef", "getAbCdEf",
        "setAbCdEf", " AB CD EF ", "' AB CD EF '", "ab", "cd", "ef");
    matchesSQL(Id.fromSQL("ab1 cd_ _ef", false, uAdapter), "Ab1Cd__ef", "ab1Cd__ef", "AB1_CD___EF", "ab1-cd_-_ef",
        "getAb1Cd__ef", "setAb1Cd__ef", "AB1 CD_ _EF", "'AB1 CD_ _EF'", "ab1", "cd_", "_ef");

    // Blanks-only identifiers

    matchesSQL(Id.fromSQL(" ", true, uAdapter), "_", "_", "_", "_", "get_", "set_", " ", "' '", "_");
    matchesSQL(Id.fromSQL("  ", true, uAdapter), "__", "__", "__", "__", "get__", "set__", "  ", "'  '", "__");
    matchesSQL(Id.fromSQL("   ", true, uAdapter), "___", "___", "___", "___", "get___", "set___", "   ", "'   '",
        "___");

  }

  public void testFromSQLUpperCaseDefaultAdapter() throws InvalidIdentifierException, SQLException {
    DatabaseAdapter uAdapter = new TestDatabaseAdapter(null, getDatabaseMetaData(), CaseSensitiveness.UPPERCASE);

    // Unquoted

    matchesSQL(Id.fromSQL("abc", false, uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "abc", "abc");
    matchesSQL(Id.fromSQL("ABC", false, uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "abc", "ABC");
    matchesSQL(Id.fromSQL("AbC", false, uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "abc", "AbC");
    matchesSQL(Id.fromSQL("aBc", false, uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "abc", "aBc");

    // Quoted

    matchesSQL(Id.fromSQL("abc", true, uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "'abc'",
        "abc");
    matchesSQL(Id.fromSQL("ABC", true, uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "abc", "ABC");
    matchesSQL(Id.fromSQL("AbC", true, uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "AbC", "'AbC'",
        "AbC");
    matchesSQL(Id.fromSQL("aBc", true, uAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "aBc", "'aBc'",
        "aBc");
  }

  public void testFromSQLLowerCaseDefaultAdapter() throws InvalidIdentifierException, SQLException {
    DatabaseAdapter lAdapter = new TestDatabaseAdapter(null, getDatabaseMetaData(), CaseSensitiveness.LOWERCASE);

    // Unquoted

    matchesSQL(Id.fromSQL("abc", false, lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "abc");
    matchesSQL(Id.fromSQL("ABC", false, lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "ABC");
    matchesSQL(Id.fromSQL("AbC", false, lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "AbC");
    matchesSQL(Id.fromSQL("aBc", false, lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "aBc");

    // Quoted

    matchesSQL(Id.fromSQL("abc", true, lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "abc");
    matchesSQL(Id.fromSQL("ABC", true, lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "'ABC'",
        "ABC");
    matchesSQL(Id.fromSQL("AbC", true, lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "AbC", "'AbC'",
        "AbC");
    matchesSQL(Id.fromSQL("aBc", true, lAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "aBc", "'aBc'",
        "aBc");
  }

  public void testFromSQLCaseSensitiveAdapter() throws InvalidIdentifierException, SQLException {
    DatabaseAdapter sAdapter = new TestDatabaseAdapter(null, getDatabaseMetaData(), CaseSensitiveness.SENSITIVE);

    // Unquoted

    matchesSQL(Id.fromSQL("abc", false, sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "abc");
    matchesSQL(Id.fromSQL("ABC", false, sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "ABC", "ABC");
    matchesSQL(Id.fromSQL("AbC", false, sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "AbC", "AbC", "AbC");
    matchesSQL(Id.fromSQL("aBc", false, sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "aBc", "aBc", "aBc");

    // Quoted

    matchesSQL(Id.fromSQL("abc", true, sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "abc");
    matchesSQL(Id.fromSQL("ABC", true, sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "ABC", "ABC");
    matchesSQL(Id.fromSQL("AbC", true, sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "AbC", "AbC", "AbC");
    matchesSQL(Id.fromSQL("aBc", true, sAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "aBc", "aBc", "aBc");
  }

  public void testFromSQLCaseInsensitiveAdapter() throws InvalidIdentifierException, SQLException {
    DatabaseAdapter iAdapter = new TestDatabaseAdapter(null, getDatabaseMetaData(), CaseSensitiveness.INSENSITIVE);

    // Unquoted

    matchesSQL(Id.fromSQL("abc", false, iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "abc");
    matchesSQL(Id.fromSQL("ABC", false, iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "ABC");
    matchesSQL(Id.fromSQL("AbC", false, iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "AbC");
    matchesSQL(Id.fromSQL("aBc", false, iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "aBc");

    // Quoted

    matchesSQL(Id.fromSQL("abc", true, iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "abc", "abc", "abc");
    matchesSQL(Id.fromSQL("ABC", true, iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "ABC", "abc", "ABC");
    matchesSQL(Id.fromSQL("AbC", true, iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "AbC", "abc", "AbC");
    matchesSQL(Id.fromSQL("aBc", true, iAdapter), "Abc", "abc", "ABC", "abc", "getAbc", "setAbc", "aBc", "abc", "aBc");
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
