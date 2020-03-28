package automatedtests.identifiers;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.utils.identifiers.Id;
import org.hotrod.utils.identifiers.Id.NamePart;

import junit.framework.TestCase;

public class IdSplitTests extends TestCase {

  public IdSplitTests(final String txt) {
    super(txt);
  }

  public void testSplitSQL() throws SQLException, InvalidIdentifierException {

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

    matchesSQL(Id.splitSQL("a"), "a");
    matchesSQL(Id.splitSQL("A"), "A");
    matchesSQL(Id.splitSQL("_"), "_");
    matchesSQL(Id.splitSQL("__"), "__");
    matchesSQL(Id.splitSQL("___"), "___");
    matchesSQL(Id.splitSQL(" "), "_");
    matchesSQL(Id.splitSQL("  "), "__");
    matchesSQL(Id.splitSQL("   "), "___");
    matchesSQL(Id.splitSQL(" _"), "_");
    matchesSQL(Id.splitSQL("_ "), "_");

    // ab
    // _ab
    // ab_
    // _ab_

    matchesSQL(Id.splitSQL("ab"), "ab");
    matchesSQL(Id.splitSQL("_ab"), "ab");
    matchesSQL(Id.splitSQL("ab_"), "ab");
    matchesSQL(Id.splitSQL("_ab_"), "ab");

    // __ab
    // ab__
    // __ab__

    matchesSQL(Id.splitSQL("__ab"), "ab");
    matchesSQL(Id.splitSQL("ab__"), "ab");
    matchesSQL(Id.splitSQL("__ab__"), "ab");

    // a_b
    // a__b

    matchesSQL(Id.splitSQL("a_b"), "a", "b");
    matchesSQL(Id.splitSQL("a__b"), "a", "b");

    // abc
    // abc123
    // abc_123
    // abc__123
    // abc_123_

    matchesSQL(Id.splitSQL("abc"), "abc");
    matchesSQL(Id.splitSQL("abc123"), "abc123");
    matchesSQL(Id.splitSQL("abc_123"), "abc", "123");
    matchesSQL(Id.splitSQL("abc__123"), "abc", "123");
    matchesSQL(Id.splitSQL("abc_123_"), "abc", "123");

    // 123_abc
    // abc_def
    // abc_def1
    // 1a3bc3_4d5ef6

    matchesSQL(Id.splitSQL("123_abc"), "123", "abc");
    matchesSQL(Id.splitSQL("abc_def"), "abc", "def");
    matchesSQL(Id.splitSQL("1a3bc3_4d5ef6"), "1a3bc3", "4d5ef6");

    // " a"
    // "a "
    // " a "
    // " ab "
    // "ab cd"
    // "ab cd ef"
    // " ab cd ef "
    // "ab1 cd_ _ef"

    matchesSQL(Id.splitSQL(" a"), "a");
    matchesSQL(Id.splitSQL("a "), "a");
    matchesSQL(Id.splitSQL(" a "), "a");
    matchesSQL(Id.splitSQL(" ab "), "ab");
    matchesSQL(Id.splitSQL("ab cd"), "ab", "cd");
    matchesSQL(Id.splitSQL("ab cd ef"), "ab", "cd", "ef");
    matchesSQL(Id.splitSQL(" ab cd ef "), "ab", "cd", "ef");
    matchesSQL(Id.splitSQL("ab1 cd_ _ef"), "ab1", "cd_", "_ef");

    // abc
    // ABC
    // AbC
    // aBc

    matchesSQL(Id.splitSQL("abc"), "abc");
    matchesSQL(Id.splitSQL("ABC"), "ABC");
    matchesSQL(Id.splitSQL("AbC"), "AbC");
    matchesSQL(Id.splitSQL("aBc"), "aBc");

  }

  public void testSplitJava() throws SQLException, InvalidIdentifierException {

    // a
    // A
    // _
    // __
    // ___

    matchesJava(Id.splitJava("a"), "a");
    matchesJava(Id.splitJava("A"), "a");
    matchesJava(Id.splitJava("_"), "_");
    matchesJava(Id.splitJava("__"), "__");
    matchesJava(Id.splitJava("___"), "___");

    // _B
    // _aB
    // a_aB
    // A_Bb

    matchesJava(Id.splitJava("_B"), "_", "b");
    matchesJava(Id.splitJava("_aB"), "_a", "b");
    matchesJava(Id.splitJava("a_aB"), "a_a", "b");
    matchesJava(Id.splitJava("A_Bb"), "a_", "bb");

    // aBb
    // aBB
    // aaBb
    // aaBB
    // AaBb
    // AaBB
    // a1Bb
    // a1B2
    // a1aB2b

    matchesJava(Id.splitJava("aBb"), "a", "bb");
    matchesJava(Id.splitJava("aBB"), "a", "BB");
    matchesJava(Id.splitJava("aaBb"), "aa", "bb");
    matchesJava(Id.splitJava("aaBB"), "aa", "BB");
    matchesJava(Id.splitJava("AaBb"), "aa", "bb");
    matchesJava(Id.splitJava("AaBB"), "aa", "BB");
    matchesJava(Id.splitJava("a1Bb"), "a1", "bb");
    matchesJava(Id.splitJava("a1B2"), "a1", "b2");
    matchesJava(Id.splitJava("a1aB2b"), "a1a", "b2b");

    // a_aB_b
    // _aB_
    // _aB_b
    // a_B_
    // a_B_b

    matchesJava(Id.splitJava("a_aB_b"), "a_a", "b_b");
    matchesJava(Id.splitJava("_aB_"), "_a", "b_");
    matchesJava(Id.splitJava("_aB_b"), "_a", "b_b");
    matchesJava(Id.splitJava("a_B_"), "a_", "b_");
    matchesJava(Id.splitJava("a_B_b"), "a_", "b_b");

    // a123a456B789

    matchesJava(Id.splitJava("a123a456B789"), "a123a456", "b789");

    // aaaBb
    // AaaBb

    matchesJava(Id.splitJava("aaaBb"), "aaa", "bb");
    matchesJava(Id.splitJava("AaaBb"), "aaa", "bb");

    // AAABbb
    // AaaBBB
    // aaaBBB

    matchesJava(Id.splitJava("AAABbb"), "AAA", "bbb");
    matchesJava(Id.splitJava("AaaBBB"), "aaa", "BBB");
    matchesJava(Id.splitJava("aaaBBB"), "aaa", "BBB");

  }

  // Utilities

  private void matchesSQL(final List<NamePart> actual, final String... expected) {
    // System.out.println("?");
    assertNotNull("actual should no be null", actual);
    assertNotNull("expected should not be null", expected);
    // System.out.println("actual.size()=" + actual.size());
    assertEquals("actual[" + actual.size() + "] number does not match expected[" + expected.length + "]",
        expected.length, actual.size());
    for (int i = 0; i < expected.length; i++) {
      String actToken = actual.get(i).getToken();
      assertEquals("actual[" + i + "] '" + actToken + "' <> expected[" + i + "] '" + expected[i] + "'", expected[i],
          actToken);
    }
  }

  private void matchesJava(final List<NamePart> actual, final String... expected) {
    assertNotNull("actual should no be null", actual);
    assertNotNull("expected should not be null", expected);
    assertEquals("actual[" + actual.size() + "] number does not match expected[" + expected.length + "]",
        expected.length, actual.size());
    for (int i = 0; i < expected.length; i++) {
      NamePart act = actual.get(i);
      String actToken = act.getToken();
      if (act.isAcronym()) {
        actToken = actToken.toUpperCase();
        assertEquals("actual[" + i + "] '" + actToken + "' <> expected[" + i + "] '" + expected[i] + "'", expected[i],
            actToken);
      } else {
        assertEquals("actual[" + i + "] '" + actToken + "' <> expected[" + i + "] '" + expected[i] + "'", expected[i],
            actToken);
      }
    }
  }

}
