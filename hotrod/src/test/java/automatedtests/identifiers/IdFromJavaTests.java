package automatedtests.identifiers;

import java.sql.SQLException;

import org.hotrod.utils.identifiers.Id;
import org.hotrod.utils.identifiers.Id.InvalidIdentifierException;

import junit.framework.TestCase;

public class IdFromJavaTests extends TestCase {

  public IdFromJavaTests(final String txt) {
    super(txt);
  }

  public void testFromSQLCommon() throws SQLException, InvalidIdentifierException {

    // --------------------------- Cl - m -- Co - d -- get --- set --- tok

    // a
    // A
    // _
    // __
    // ___

    try {
      matches(Id.fromJavaClass("a"), "A", "m", "X", "-", "get", "set", "a");
      fail("A Java class should not start with a lower case letter.");
    } catch (InvalidIdentifierException e) {
      // OK
    }
    matches(Id.fromJavaClass("A"), "A", "a", "A", "a", "getA", "setA", "a");
    matches(Id.fromJavaClass("_"), "_", "_", "_", "_", "get_", "set_", "_");
    matches(Id.fromJavaClass("__"), "__", "__", "__", "__", "get__", "set__", "__");
    matches(Id.fromJavaClass("___"), "___", "___", "___", "___", "get___", "set___", "___");

    // _B
    // _aB
    // a_aB
    // A_Bb

    matches(Id.fromJavaClass("_B"), "_B", "_B", "__B", "_-b", "get_B", "set_B", "_", "b");
    matches(Id.fromJavaClass("_aB"), "_aB", "_aB", "_A_B", "_a-b", "get_aB", "set_aB", "_a", "b");
    try {
      matches(Id.fromJavaClass("a_aB"), "C", "m", "X", "-", "get", "set", "a_a", "b");
      fail("A Java class should not start with a lower case letter.");
    } catch (InvalidIdentifierException e) {
      // OK
    }
    matches(Id.fromJavaClass("A_Bb"), "A_Bb", "a_Bb", "A__BB", "a_-bb", "getA_Bb", "setA_Bb", "a_", "bb");

    // AaBb
    // AaBB

    matches(Id.fromJavaClass("AaBb"), "AaBb", "aaBb", "AA_BB", "aa-bb", "getAaBb", "setAaBb", "aa", "bb");
    matches(Id.fromJavaClass("AaBB"), "AaBB", "aaBB", "AA_BB", "aa-bb", "getAaBB", "setAaBB", "aa", "BB");

    // _aB_
    // _aB_b

    matches(Id.fromJavaClass("_aB_"), "_aB_", "_aB_", "_A_B_", "_a-b_", "get_aB_", "set_aB_", "_a", "b_");
    matches(Id.fromJavaClass("_aB_b"), "_aB_b", "_aB_b", "_A_B_B", "_a-b_b", "get_aB_b", "set_aB_b", "_a", "b_b");

    // A123a456B789

    matches(Id.fromJavaClass("A123a456B789"), "A123a456B789", "a123a456B789", "A123A456_B789", "a123a456-b789",
        "getA123a456B789", "setA123a456B789", "a123a456", "b789");

    // AaaBb

    matches(Id.fromJavaClass("AaaBb"), "AaaBb", "aaaBb", "AAA_BB", "aaa-bb", "getAaaBb", "setAaaBb", "aaa", "bb");

    // AAABbb
    // AaaBBB

    matches(Id.fromJavaClass("AAABbb"), "AAABbb", "aaaBbb", "AAA_BBB", "aaa-bbb", "getAAABbb", "setAAABbb", "AAA",
        "bbb");
    matches(Id.fromJavaClass("AaaBBB"), "AaaBBB", "aaaBBB", "AAA_BBB", "aaa-bbb", "getAaaBBB", "setAaaBBB", "aaa",
        "BBB");

  }

  // Helpers

  private void matches(final Id id, final String javaClassName, final String javaMemberName, final String constantName,
      final String dashedName, final String getter, final String setter, final String... tokens) {
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

    assertEquals("Expected null canonicalSQLName, but found '" + id.getCanonicalSQLName() + "'", null,
        id.getCanonicalSQLName());
    assertEquals("Expected null renderedSQLName, but found '" + id.getRenderedSQLName() + "'", null,
        id.getRenderedSQLName());

    assertNotNull("tokens should not be null", tokens);
    assertNotNull("canonicalparts should not be null", id.getCanonicalParts());
    assertEquals(
        "canonicalparts[" + id.getCanonicalParts().size() + "] number does not match tokens[" + tokens.length + "]",
        id.getCanonicalParts().size(), tokens.length);
    for (int i = 0; i < tokens.length; i++) {
      assertEquals("canonical part[" + i + "] '" + id.getCanonicalParts().get(i) + "' <> token '" + tokens[i] + "'",
          id.getCanonicalParts().get(i), tokens[i]);
    }
  }

  // private static void log(final String txt) {
  // System.out.println("[LOG] " + txt);
  // }

}
