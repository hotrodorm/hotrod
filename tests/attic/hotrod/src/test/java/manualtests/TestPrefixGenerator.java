package manualtests;

import org.hotrod.database.DatabaseAdapter.UnescapedSQLCase;
import org.hotrod.utils.ColumnsPrefixGenerator;

public class TestPrefixGenerator {

  public static void main(final String[] args) {
    ColumnsPrefixGenerator g = new ColumnsPrefixGenerator(UnescapedSQLCase.LOWER_CASE);
    for (int i = 0; i < 26 * 28; i++) {
      System.out.println("prefix: " + g.next());
    }
  }

}
