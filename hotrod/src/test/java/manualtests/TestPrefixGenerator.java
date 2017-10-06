package manualtests;

import org.hotrod.metadata.VOMetadata.PrefixGenerator;

public class TestPrefixGenerator {

  public static void main(final String[] args) {
    PrefixGenerator g = new PrefixGenerator();
    for (int i = 0; i < 26 * 28; i++) {
      System.out.println("prefix: " + g.next());
    }
  }

}
