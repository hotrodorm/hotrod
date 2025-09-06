package org.hotrod.utils;

public class T {

  private static Timers w = null;

  public static void start(boolean logging) {
    w = new Timers(logging);
  }

  public static void endPhase(String name) {
    w.endPhase(name);
  }

}
