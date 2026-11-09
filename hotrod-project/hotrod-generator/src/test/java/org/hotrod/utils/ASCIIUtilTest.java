package org.hotrod.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ASCIIUtilTest {

  @Test
  public void visibleAsciiCharactersArePreserved() {
    assertEquals(" !~", ASCIIUtil.sanitize(" !~"));
  }

  @Test
  public void charactersOutsideVisibleAsciiAreReplaced() {
    assertEquals("___A__", ASCIIUtil.sanitize("\u0000\t\nA\u007f\u00e9"));
  }

  @Test
  public void supplementaryCharactersAreReplacedPerCharacter() {
    assertEquals("__", ASCIIUtil.sanitize("\uD83D\uDE00"));
  }

  @Test
  public void nullIsPreserved() {
    assertNull(ASCIIUtil.sanitize(null));
  }

  @Test
  public void emptyStringIsPreserved() {
    assertEquals("", ASCIIUtil.sanitize(""));
  }

}