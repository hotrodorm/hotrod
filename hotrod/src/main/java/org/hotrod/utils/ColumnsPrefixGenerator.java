package org.hotrod.utils;

public class ColumnsPrefixGenerator {

  private static final String PREFIX = "c";
  private static final String SUFFIX = "_";

  private int nextInt = 0;

  public synchronized String next() {
    return PREFIX + this.nextInt++ + SUFFIX;
  }
  
}
