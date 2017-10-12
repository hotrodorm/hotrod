package manualtests;

import org.hotrod.utils.ColumnsPrefixGenerator;

public class TestPrefixGenerator {

  public static void main(final String[] args) {
    ColumnsPrefixGenerator g = new ColumnsPrefixGenerator();
    for (int i = 0; i < 26 * 28; i++) {
      System.out.println("prefix: " + g.next());
    }
  }

}
