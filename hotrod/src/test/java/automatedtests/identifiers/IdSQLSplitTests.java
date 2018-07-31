package automatedtests.identifiers;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.utils.identifiers.Id;
import org.hotrod.utils.identifiers.Id.InvalidIdentifierException;

import junit.framework.TestCase;

public class IdSQLSplitTests extends TestCase {

  public IdSQLSplitTests(final String txt) {
    super(txt);
  }

  private void matches(final List<String> parts, final String... tokens) {
    assertNotNull("parts should no be null", parts);
    assertNotNull("tokens should not be null", tokens);
    assertEquals("parts[" + parts.size() + "] number does not match expected tokens[" + tokens.length + "]",
        tokens.length, parts.size());
    for (int i = 0; i < tokens.length; i++) {
      assertEquals("part[" + i + "] '" + parts.get(i) + "' <> token '" + tokens[i] + "'", tokens[i], parts.get(i));
    }
  }

  public void testSplitSQL() throws SQLException, InvalidIdentifierException {

    List<String> pt;

    // a -- resolved
    // A -- resolved
    // _ -- resolved

    pt = Id.splitSQL("a");
    matches(pt, "a");

    pt = Id.splitSQL("A");
    matches(pt, "a");

    pt = Id.splitSQL("_");
    matches(pt, "_");

    // _B
    // _aB
    // a_aB
    // A_Bb

    pt = Id.splitSQL("_B");
    matches(pt, "_", "b");

    pt = Id.splitSQL("_aB");
    matches(pt, "_a", "b");

    pt = Id.splitSQL("a_aB");
    matches(pt, "a_a", "b");

    pt = Id.splitSQL("A_AB");
    matches(pt, "a_", "ab");

    // aBb
    // aaBb
    // AaBb

    pt = Id.splitSQL("aBb");
    matches(pt, "a", "bb");

    pt = Id.splitSQL("aaBb");
    matches(pt, "aa", "bb");

    pt = Id.splitSQL("AaBb");
    matches(pt, "aa", "bb");

    // aaaBb
    // AaaBb
    // AAABb

    pt = Id.splitSQL("aaaBb");
    matches(pt, "aaa", "bb");

    pt = Id.splitSQL("AaaBb");
    matches(pt, "aaa", "bb");

    pt = Id.splitSQL("AAABb");
    matches(pt, "aaa", "bb");

  }

}
