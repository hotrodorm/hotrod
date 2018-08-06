package automatedtests.identifiers;

import java.sql.SQLException;

import org.hotrod.utils.identifiers2.Id;
import org.hotrod.utils.identifiers2.Id.InvalidIdentifierException;

import junit.framework.TestCase;

public class IdFromJavaTests extends TestCase {

  public IdFromJavaTests(final String txt) {
    super(txt);
  }

  public void testFromJavaClass() throws SQLException, InvalidIdentifierException {

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

    try {
      matches(Id.fromJavaMember(" A"), "C", "m", "X", "-", "get", "set", "_");
      fail("A Java class must start with a lower case letter.");
    } catch (InvalidIdentifierException e) {
      // OK
    }
    try {
      matches(Id.fromJavaMember("A "), "C", "m", "X", "-", "get", "set", "_");
      fail("A Java class cannot include other non-alphaumeric characters.");
    } catch (InvalidIdentifierException e) {
      // OK
    }
    try {
      matches(Id.fromJavaMember("A B"), "C", "m", "X", "-", "get", "set", "_");
      fail("A Java class cannot include other non-alphaumeric characters.");
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

  public void testFromJavaMember() throws SQLException, InvalidIdentifierException {

    // --------------------------- Cl - m -- Co - d -- get --- set --- tok

    try {
      matches(Id.fromJavaMember(" a"), "C", "m", "X", "-", "get", "set", "_");
      fail("A Java member must start with a lower case letter.");
    } catch (InvalidIdentifierException e) {
      // OK
    }
    try {
      matches(Id.fromJavaMember("a "), "C", "m", "X", "-", "get", "set", "_");
      fail("A Java member cannot include other non-alphaumeric characters.");
    } catch (InvalidIdentifierException e) {
      // OK
    }
    try {
      matches(Id.fromJavaMember("a b"), "C", "m", "X", "-", "get", "set", "_");
      fail("A Java member cannot include other non-alphaumeric characters.");
    } catch (InvalidIdentifierException e) {
      // OK
    }

    // a
    // A
    // _
    // __
    // ___

    matches(Id.fromJavaMember("a"), "A", "a", "A", "a", "getA", "setA", "a");
    try {
      matches(Id.fromJavaMember("A"), "C", "m", "X", "-", "get", "set", "A");
      fail("A Java member cannot start with an upper case letter.");
    } catch (InvalidIdentifierException e) {
      // OK
    }
    matches(Id.fromJavaMember("_"), "_", "_", "_", "_", "get_", "set_", "_");
    matches(Id.fromJavaMember("__"), "__", "__", "__", "__", "get__", "set__", "__");
    matches(Id.fromJavaMember("___"), "___", "___", "___", "___", "get___", "set___", "___");

    // _B
    // _aB
    // a_aB

    matches(Id.fromJavaMember("_B"), "_B", "_B", "__B", "_-b", "get_B", "set_B", "_", "b");
    matches(Id.fromJavaMember("_aB"), "_aB", "_aB", "_A_B", "_a-b", "get_aB", "set_aB", "_a", "b");
    matches(Id.fromJavaMember("a_aB"), "A_aB", "a_aB", "A_A_B", "a_a-b", "getA_aB", "setA_aB", "a_a", "b");

    // aBb
    // aBB
    // aaBb
    // aaBB
    // a1Bb
    // a1B2
    // a1aB2b

    matches(Id.fromJavaMember("aBb"), "ABb", "aBb", "A_BB", "a-bb", "getABb", "setABb", "a", "bb");
    matches(Id.fromJavaMember("aBB"), "ABB", "aBB", "A_BB", "a-bb", "getABB", "setABB", "a", "BB");
    matches(Id.fromJavaMember("aaBb"), "AaBb", "aaBb", "AA_BB", "aa-bb", "getAaBb", "setAaBb", "aa", "bb");
    matches(Id.fromJavaMember("aaBB"), "AaBB", "aaBB", "AA_BB", "aa-bb", "getAaBB", "setAaBB", "aa", "BB");
    matches(Id.fromJavaMember("a1Bb"), "A1Bb", "a1Bb", "A1_BB", "a1-bb", "getA1Bb", "setA1Bb", "a1", "bb");
    matches(Id.fromJavaMember("a1B2"), "A1B2", "a1B2", "A1_B2", "a1-b2", "getA1B2", "setA1B2", "a1", "b2");
    matches(Id.fromJavaMember("a1aB2b"), "A1aB2b", "a1aB2b", "A1A_B2B", "a1a-b2b", "getA1aB2b", "setA1aB2b", "a1a",
        "b2b");

    // a_aB_b
    // _aB_
    // _aB_b
    // a_B_
    // a_B_b

    matches(Id.fromJavaMember("a_aB_b"), "A_aB_b", "a_aB_b", "A_A_B_B", "a_a-b_b", "getA_aB_b", "setA_aB_b", "a_a",
        "b_b");
    matches(Id.fromJavaMember("_aB_"), "_aB_", "_aB_", "_A_B_", "_a-b_", "get_aB_", "set_aB_", "_a", "b_");
    matches(Id.fromJavaMember("_aB_b"), "_aB_b", "_aB_b", "_A_B_B", "_a-b_b", "get_aB_b", "set_aB_b", "_a", "b_b");
    matches(Id.fromJavaMember("a_B_"), "A_B_", "a_B_", "A__B_", "a_-b_", "getA_B_", "setA_B_", "a_", "b_");
    matches(Id.fromJavaMember("a_B_b"), "A_B_b", "a_B_b", "A__B_B", "a_-b_b", "getA_B_b", "setA_B_b", "a_", "b_b");

    // a123a456B789

    matches(Id.fromJavaMember("a123a456B789"), "A123a456B789", "a123a456B789", "A123A456_B789", "a123a456-b789",
        "getA123a456B789", "setA123a456B789", "a123a456", "b789");

    // aaaBb

    matches(Id.fromJavaMember("aaaBb"), "AaaBb", "aaaBb", "AAA_BB", "aaa-bb", "getAaaBb", "setAaaBb", "aaa", "bb");

    // aaaBBB

    matches(Id.fromJavaMember("aaaBBB"), "AaaBBB", "aaaBBB", "AAA_BBB", "aaa-bbb", "getAaaBBB", "setAaaBBB", "aaa",
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
