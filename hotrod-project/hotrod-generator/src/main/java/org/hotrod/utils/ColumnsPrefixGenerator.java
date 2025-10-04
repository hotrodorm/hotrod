package org.hotrod.utils;

import org.hotrod.database.DatabaseAdapter.UnescapedSQLCase;

public class ColumnsPrefixGenerator {

  // Constants

  private static final String LOWER_CASE_PREFIX = "ns";
  private static final String UPPER_CASE_PREFIX = "NS";
  private static final String SUFFIX = "_";

  // Properties

  private UnescapedSQLCase unescapedSQLCase;
  private int nextInt = 0;

  // Constructor

  public ColumnsPrefixGenerator(final UnescapedSQLCase unescapedSQLCase) {
    this.unescapedSQLCase = unescapedSQLCase;
  }

  // Behavior

  public synchronized String next() {
    if (this.unescapedSQLCase == UnescapedSQLCase.LOWER_CASE || this.unescapedSQLCase == UnescapedSQLCase.ANY_CASE) {
      return LOWER_CASE_PREFIX + this.nextInt++ + SUFFIX;
    } else {
      return UPPER_CASE_PREFIX + this.nextInt++ + SUFFIX;
    }
  }

}
