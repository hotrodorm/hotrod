package org.hotrod.livesql.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NSSequenceTest {

  @Test
  void formatsSequentialValuesAsExcelColumnNames() {
    NSSequence sequence = new NSSequence();

    for (int i = 1; i <= 25; i++) {
      assertEquals(String.valueOf((char) ('a' + i - 1)), sequence.next());
    }
    assertEquals("z", sequence.next());
    assertEquals("aa", sequence.next());
    assertEquals("ab", sequence.next());

    for (int i = 0; i < 23; i++) {
      sequence.next();
    }
    assertEquals("az", sequence.next());
    assertEquals("ba", sequence.next());
  }

  @Test
  void startsEachSequenceAtTheFirstColumn() {
    assertEquals("a", new NSSequence().next());
    assertEquals("a", new NSSequence().next());
  }
}