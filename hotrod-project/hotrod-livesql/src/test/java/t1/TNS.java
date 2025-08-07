package t1;

import org.hotrod.livesql.util.NSSequence;
import org.hotrod.livesql.util.NSUtil;

public class TNS {

  public static void main(String[] args) {
    NSSequence seq = NSUtil.sequence();
    for (int i = 0; i < 26 * 27 + 10; i++) {
      System.out.println("seq=" + seq.next());
    }

  }

}
