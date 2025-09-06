package app.model;

import java.util.Random;

public class R {

  private static Random random;

  public static void seed(long seed) {
    random = new Random(seed);
  }

  public static int nextInt() {
    return random.nextInt();
  }

  public static int nextInt(int bound) {
    return random.nextInt(bound);
  }

  public static double nextDouble() {
    return random.nextDouble();
  }

}
