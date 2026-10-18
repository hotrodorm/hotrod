package org.hotrod.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.hotrod.interfaces.OrderBy;
import org.junit.jupiter.api.Test;

public class CoreUtilitiesTest {

  @Test
  public void hexadecimalConversionsRoundTrip() {
    assertEquals("00010aff", HexaUtils.toHexa(new byte[] { 0, 1, 10, -1 }));
    assertEquals("ff", HexaUtils.toHexa((byte) -1));
    assertArrayEquals(new byte[] { 0, 1, 10, -1 }, HexaUtils.toByteArray("00010aff"));
    assertEquals("00/41", HexaUtils.charToHexa('A'));
    assertNull(HexaUtils.toHexa((byte[]) null));
    assertNull(HexaUtils.toByteArray(null));
  }

  @Test
  public void hexadecimalConversionRejectsInvalidCharacters() {
    assertThrows(IllegalArgumentException.class, () -> HexaUtils.toByteArray("0g"));
  }

  @Test
  public void arrayConcatenationPreservesContentsAndType() {
    assertArrayEquals(new String[] { "a", "b", "c" }, AUtil.concat(new String[] { "a" }, "b", "c"));
    assertArrayEquals(new String[] { "a", "b", "c" }, AUtil.concat("a", new String[] { "b", "c" }));
    String[] result = AUtil.concat(new String[] { "a" }, new String[] { "b" }, new String[] { "c" });
    assertArrayEquals(new String[] { "a", "b", "c" }, result);
    assertEquals(String[].class, result.getClass());
  }

  @Test
  public void jdbcTypesMapBothDirections() {
    assertEquals(Integer.valueOf(java.sql.Types.INTEGER), JDBCTypes.nameToCode("INTEGER"));
    assertEquals(Integer.valueOf(java.sql.Types.INTEGER), JDBCTypes.nameToCode("java.sql.Types.INTEGER"));
    assertEquals(JDBCTypes.JDBCType.INTEGER, JDBCTypes.nameToType("INTEGER"));
    assertEquals(JDBCTypes.JDBCType.INTEGER, JDBCTypes.codeToType(java.sql.Types.INTEGER));
    assertEquals("java.sql.Types.INTEGER", JDBCTypes.codeToName(java.sql.Types.INTEGER));
    assertEquals("INTEGER", JDBCTypes.codeToShortName(java.sql.Types.INTEGER));
    assertNull(JDBCTypes.nameToType("missing"));
    assertNull(JDBCTypes.codeToName(Integer.MIN_VALUE));
  }

  @Test
  public void separatorRendersPrefixAndSeparators() {
    Separator separator = Separator.of("[", ", ");
    assertEquals("[", separator.render());
    assertEquals(", ", separator.render());
    separator.reset();
    assertEquals("[", separator.render());
    assertEquals("", new Separator().render());
  }

  @Test
  public void sqlOrderingRendersAscendingAndDescendingTerms() {
    OrderBy ascending = new OrderBy() {
      public String getSQLColumnName() { return "name"; }
      public boolean isAscending() { return true; }
    };
    OrderBy descending = new OrderBy() {
      public String getSQLColumnName() { return "created"; }
      public boolean isAscending() { return false; }
    };
    assertEquals("\nORDER BY name, created DESC", SQLUtil.render(new OrderBy[] { ascending, descending }));
    assertEquals("", SQLUtil.render(new OrderBy[0]));
  }

  @Test
  public void sourceLocationRendersFilePosition() {
    SourceLocation location = new SourceLocation(new File("query.sql"), 3, 7, 42);
    assertEquals("query.sql", location.getFile().getPath());
    assertEquals(3, location.getLineNumber());
    assertEquals(7, location.getColumnNumber());
    assertEquals(42, location.getCharacterOffset());
    assertEquals("file 'query.sql' (line 3, col 7)", location.render());
    assertEquals("[file 'query.sql': line 3, col 7 (char 42)]", location.toString());
  }

  @Test
  public void exceptionChainIsStreamedAndTrimmed() {
    IllegalArgumentException cause = new IllegalArgumentException("bad input");
    RuntimeException root = new RuntimeException("outer", cause);
    assertEquals(2, XUtil.stream(root).count());
    assertEquals("outer[5]: bad input[9]", XUtil.trim(root));
  }
}
