package org.hotrod.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SUtilTest {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Test
  public void stringPredicatesAndSelection() {
    assertTrue(SUtil.isEmpty(null));
    assertTrue(SUtil.isEmpty("  \t"));
    assertFalse(SUtil.isEmpty("value"));

    assertEquals("first", SUtil.coalesce(null, "first", "second"));
    assertNull(SUtil.coalesce(null, null));
    assertTrue(SUtil.equals(null, null));
    assertFalse(SUtil.equals(null, "value"));
    assertTrue(SUtil.equals("value", "value"));
  }

  @Test
  public void capitalizationAndCaseChecks() {
    assertNull(SUtil.capitalize(null));
    assertEquals("Hello World", SUtil.capitalize("  hello world  "));
    assertEquals("", SUtil.capitalize("  "));
    assertEquals("Hello world", SUtil.sentenceFormat("hello world"));
    assertEquals("", SUtil.sentenceFormat(""));
    assertNull(SUtil.sentenceFormat(null));
    assertEquals("hello", SUtil.lowerFirst("Hello"));
    assertEquals("Hello", SUtil.upperFirst("hello"));
    assertEquals("", SUtil.lowerFirst(""));
    assertEquals("", SUtil.upperFirst(""));

    assertTrue(SUtil.startsWithUpperCaseLetter("Value"));
    assertFalse(SUtil.startsWithUpperCaseLetter("value"));
    assertFalse(SUtil.startsWithUpperCaseLetter(""));
    assertTrue(SUtil.startsWithLowerCaseLetter("value"));
    assertFalse(SUtil.startsWithLowerCaseLetter("Value"));
    assertTrue(SUtil.startsWithIgnoreCase("Value", "va"));
    assertFalse(SUtil.startsWithIgnoreCase("Value", "lu"));
    assertTrue(SUtil.endsWithIgnoreCase("Value", "LUE"));
    assertFalse(SUtil.endsWithIgnoreCase("Value", "val"));
  }

  @Test
  public void repetitionIndentationAndAlignment() {
    assertEquals("...", SUtil.filler('.', 3));
    assertEquals("aa", SUtil.repeat('a', 2));
    assertEquals("ababab", SUtil.getFiller("ab", 3));
    assertEquals("  first\n  second", SUtil.indent("first\nsecond", 2));
    assertNull(SUtil.indent(null, 2));
    assertEquals("  value", SUtil.alignRight("value", 7));
    assertEquals("value  ", SUtil.alignLeft("value", 7));
    assertEquals("value", SUtil.alignLeft("value", 3));
    assertNull(SUtil.alignRight(null, 5));
  }

  @Test
  public void escapingProducesExpectedText() {
    assertEquals("quote=\\\" slash=\\\\ tab=\\t newline=\\n", 
        SUtil.escapeJavaString("quote=\" slash=\\ tab=\t newline=\n"));
    assertEquals("&lt;tag> &amp;", SUtil.escapeXmlBody("<tag> &"));
    assertEquals("&lt;a x=&quot;1&quot; y=&apos;2&apos;>", SUtil.escapeXmlAttribute("<a x=\"1\" y='2'>"));
    assertNull(SUtil.escapeJavaString(null));
    assertNull(SUtil.escapeXmlBody(null));
    assertNull(SUtil.escapeXmlAttribute(null));
  }

  @Test
  public void countMatchesAndSliceHandleBoundaries() {
    assertEquals(4, SUtil.countMatches("aaaa", "a"));
    assertEquals(2, SUtil.countMatches("aaaa", "aa"));
    assertEquals(0, SUtil.countMatches("aaaa", ""));
    assertEquals(0, SUtil.countMatches("aaaa", null));

    assertEquals(Arrays.asList("abc", "def", "g"), SUtil.slice("abcdefg", 3));
    assertEquals(Collections.emptyList(), SUtil.slice("", 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void sliceRejectsZeroWidth() {
    SUtil.slice("abc", 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void sliceRejectsNegativeWidth() {
    SUtil.slice("abc", -1);
  }

  @Test
  public void fileRoundTripPreservesLines() throws IOException {
    File file = temporaryFolder.newFile("sutil.txt");
    SUtil.saveStringToFile("one\ntwo", file);
    assertEquals("one\ntwo\n", SUtil.loadFileAsString(file));
    assertEquals("one\ntwo\n", SUtil.loadFileAsString(file, "UTF-8"));
  }

  @Test
  public void formatsDateTime() {
    assertEquals("Jan 2, 2020 at 03:04:05", SUtil.format(LocalDateTime.of(2020, 1, 2, 3, 4, 5)));
  }
}
