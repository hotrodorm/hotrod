package test;

import java.util.Random;

public class TestSeq {

  public static void main(final String[] args) {
    Random r = new Random();
    r.nextInt(64);
    int x = 1;
    for (int i = 0; i < 100; i++) {
      System.out.printf("%d: %x\n", i, x);
      x = 75 * (x + 1) - 1;
      x = 0xffff & x;
    }

  }

}
