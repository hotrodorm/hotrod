package automatedtests.identifiers;

import java.sql.SQLException;

import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.utils.identifiers.SQLName;

import junit.framework.TestCase;

public class SQLNameTests extends TestCase {

  public SQLNameTests(final String txt) {
    super(txt);
  }

  public void testQuoting() throws SQLException, InvalidIdentifierException {

    matches(new SQLName(""), "", false);
    matches(new SQLName("a"), "a", false);
    matches(new SQLName("ab"), "ab", false);
    matches(new SQLName("abc"), "abc", false);

    matches(new SQLName("''"), "''", false);

    matches(new SQLName("'a'"), "a", true);
    matches(new SQLName("'ab'"), "ab", true);
    matches(new SQLName("'abc'"), "abc", true);

    matches(new SQLName("a'b"), "a'b", false);
    matches(new SQLName("a\"b"), "a\"b", false);
    matches(new SQLName("a`b"), "a`b", false);
    matches(new SQLName("a[b"), "a[b", false);
    matches(new SQLName("a]b"), "a]b", false);

    matches(new SQLName("'ab'"), "ab", true);
    matches(new SQLName("\"ab\""), "ab", true);
    matches(new SQLName("`ab`"), "ab", true);
    matches(new SQLName("[ab]"), "ab", true);

  }

  // Utilities

  private void matches(final SQLName name, final String expectedName, final boolean expectedQuoting) {
    assertNotNull("name should no be null", name);
    assertEquals("Name did not match: expected '" + expectedName + "' but found '" + name.getName() + "'", expectedName,
        name.getName());
    assertEquals("Quoting did not match: expected '" + expectedQuoting + "' but found '" + name.isQuoted() + "'",
        expectedQuoting, name.isQuoted());
  }

}
