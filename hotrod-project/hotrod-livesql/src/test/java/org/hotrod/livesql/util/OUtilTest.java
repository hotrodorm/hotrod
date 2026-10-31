package org.hotrod.livesql.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OUtilTest {

  @Test
  void representsNullAsAnEmptyString() {
    assertEquals("", OUtil.hc(null));
  }

  @Test
  void representsAnObjectUsingItsClassAndIdentityHash() {
    Object value = new Object();

    String result = OUtil.hc(value);

    assertTrue(result.startsWith(Object.class.getName() + "@"));
    assertEquals(Object.class.getName() + "@" + String.format("%08x", System.identityHashCode(value)), result);
  }
}