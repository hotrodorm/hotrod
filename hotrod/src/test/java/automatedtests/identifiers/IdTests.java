package automatedtests.identifiers;

import java.sql.SQLException;

import org.hotrod.database.DatabaseAdapter;
import org.hotrod.utils.identifiers.Id;
import org.hotrod.utils.identifiers.Id.InvalidIdentifierException;

import junit.framework.TestCase;

public class IdTests extends TestCase {

  public IdTests(final String txt) {
    super(txt);
  }

  private void matches(final Id id, final String... tokens) {
    assertNotNull("id should no be null", id);
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

  public void testFromSQL() throws SQLException, InvalidIdentifierException {

    DatabaseAdapter adapter = new MyDatabaseAdapter(null, new MyDatabaseMetaData());

    Id id;

    // a -- resolved
    // A -- resolved
    // _ -- resolved

    id = Id.fromSQL("a", false, adapter);
    matches(id, "a");

    id = Id.fromSQL("A", false, adapter);
    matches(id, "a");

    id = Id.fromSQL("_", false, adapter);
    matches(id, "_");

    // _B
    // _aB
    // a_aB
    // A_Bb

    id = Id.fromSQL("_B", false, adapter);
    matches(id, "_", "b");

    id = Id.fromSQL("_aB", false, adapter);
    matches(id, "_a", "b");

    id = Id.fromSQL("a_aB", false, adapter);
    matches(id, "a_a", "b");

    id = Id.fromSQL("A_AB", false, adapter);
    matches(id, "a_", "ab");

    // aBb
    // aaBb
    // AaBb

    id = Id.fromSQL("aBb", false, adapter);
    matches(id, "a", "bb");

    id = Id.fromSQL("aaBb", false, adapter);
    matches(id, "aa", "bb");

    id = Id.fromSQL("AaBb", false, adapter);
    matches(id, "aa", "bb");

    // aaaBb
    // AaaBb
    // AAABb

    id = Id.fromSQL("aaaBb", false, adapter);
    matches(id, "aaa", "bb");

    id = Id.fromSQL("AaaBb", false, adapter);
    matches(id, "aaa", "bb");

    id = Id.fromSQL("AAABb", false, adapter);
    matches(id, "aaa", "bb");

  }

}
