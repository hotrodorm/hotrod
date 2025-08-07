package org.hotrod.livesql.util;

public class NSSequence {

  private int nextValue = 1;

  public String next() {
    return formatExcel(this.nextValue++);
  }

  // 0:A, 1:B, 25:Z, 26:AA
  private String formatExcel(int col) {
    StringBuilder sb = new StringBuilder();
    int div = col;
    do {
      div = div - 1;
      int remainder = div % 26;
      char colLetter = (char) ('a' + remainder);
      sb.insert(0, colLetter);
      div = (div / 26);
    } while (div > 0);
    return sb.toString();
  }

}
