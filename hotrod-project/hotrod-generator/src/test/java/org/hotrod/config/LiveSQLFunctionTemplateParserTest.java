package org.hotrod.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.hotrod.config.LiveSQLFunctionTemplateParser.InvalidLiveSQLFunctionTemplateException;
import org.junit.jupiter.api.Test;

public class LiveSQLFunctionTemplateParserTest {

  @Test
  public void nullPatternIsRejected() {
    InvalidLiveSQLFunctionTemplateException exception = assertThrows(InvalidLiveSQLFunctionTemplateException.class,
        () -> new LiveSQLFunctionTemplateParser().parse(null));

    assertEquals("Function cannot have an empty pattern.", exception.getMessage());
  }

  @Test
  public void emptyPatternIsRejected() {
    InvalidLiveSQLFunctionTemplateException exception = assertThrows(InvalidLiveSQLFunctionTemplateException.class,
        () -> new LiveSQLFunctionTemplateParser().parse(""));

    assertEquals("Function cannot have an empty pattern.", exception.getMessage());
  }

  @Test
  public void literalPatternsProduceOneLiteralSection() throws Exception {
    LiveSQLFunctionTemplateParser parser = new LiveSQLFunctionTemplateParser();

    List<?> sections = parser.parse("localtimestamp");
    assertEquals(1, sections.size());
    assertEquals("LiteralSection", sections.get(0).getClass().getSimpleName());
    assertEquals("localtimestamp", invoke(sections.get(0), "getLiteral"));
  }

  @Test
  public void positionalArgumentsAreParsedBetweenLiteralSections() throws Exception {
    List<?> sections = new LiveSQLFunctionTemplateParser().parse("left(#{}, #{})");

    assertEquals(5, sections.size());

    assertEquals("LiteralSection", sections.get(0).getClass().getSimpleName());
    assertEquals("left(", invoke(sections.get(0), "getLiteral"));

    assertEquals("ArgSection", sections.get(1).getClass().getSimpleName());

    assertEquals("LiteralSection", sections.get(2).getClass().getSimpleName());
    assertEquals(", ", invoke(sections.get(2), "getLiteral"));

    assertEquals("ArgSection", sections.get(3).getClass().getSimpleName());

    assertEquals("LiteralSection", sections.get(4).getClass().getSimpleName());
    assertEquals(")", invoke(sections.get(4), "getLiteral"));
  }

  @Test
  public void oneMarkerVarargHasPrefixAndSuffix() throws Exception {
    List<?> sections = new LiveSQLFunctionTemplateParser().parse("coalesce(#{?, ?})");

    assertEquals(3, sections.size());
    Object vararg = sections.get(1);
    assertEquals("VarArgSection", vararg.getClass().getSimpleName());
    assertEquals("", invoke(vararg, "getFirstPrefix"));
    assertEquals(", ", invoke(vararg, "getPrefix"));
    assertEquals("", invoke(vararg, "getSuffix"));
  }

  @Test
  public void twoMarkerVarargHasFirstPrefixPrefixAndSuffix() throws Exception {
    List<?> sections = new LiveSQLFunctionTemplateParser().parse("format(#{first, ?; ?.})");

    Object vararg = sections.get(1);
    assertEquals("VarArgSection", vararg.getClass().getSimpleName());
    assertEquals("first, ", invoke(vararg, "getFirstPrefix"));
    assertEquals("; ", invoke(vararg, "getPrefix"));
    assertEquals(".", invoke(vararg, "getSuffix"));
  }

  @Test
  public void placeholdersAtPatternBoundariesAndConsecutivePlaceholdersAreParsed() throws Exception {
    List<?> sections = new LiveSQLFunctionTemplateParser().parse("#{}#{}");

    assertEquals(2, sections.size());
    assertEquals("ArgSection", sections.get(0).getClass().getSimpleName());
    assertEquals("ArgSection", sections.get(1).getClass().getSimpleName());
  }

  @Test
  public void malformedPatternsAreRejected() {
    LiveSQLFunctionTemplateParser parser = new LiveSQLFunctionTemplateParser();

    assertThrows(InvalidLiveSQLFunctionTemplateException.class, () -> parser.parse("sin(#{"));
    assertThrows(InvalidLiveSQLFunctionTemplateException.class, () -> parser.parse("sin(#{value})"));
    assertThrows(InvalidLiveSQLFunctionTemplateException.class, () -> parser.parse("#{?} #{}"));
  }

  @Test
  public void goodVarArgs() throws Exception {
    List<?> sections = new LiveSQLFunctionTemplateParser().parse("#{}#{?}");
    assertEquals(2, sections.size());
    assertEquals("ArgSection", sections.get(0).getClass().getSimpleName());

    Object vararg = sections.get(1);
    assertEquals("VarArgSection", vararg.getClass().getSimpleName());
    assertEquals("", invoke(vararg, "getFirstPrefix"));
    assertEquals("", invoke(vararg, "getPrefix"));
    assertEquals("", invoke(vararg, "getSuffix"));

    sections = new LiveSQLFunctionTemplateParser().parse("#{a?}");
    assertEquals(1, sections.size());
    vararg = sections.get(0);
    assertEquals("VarArgSection", vararg.getClass().getSimpleName());
    assertEquals("", invoke(vararg, "getFirstPrefix"));
    assertEquals("a", invoke(vararg, "getPrefix"));
    assertEquals("", invoke(vararg, "getSuffix"));

    sections = new LiveSQLFunctionTemplateParser().parse("#{a?b}");
    assertEquals(1, sections.size());
    vararg = sections.get(0);
    assertEquals("VarArgSection", vararg.getClass().getSimpleName());
    assertEquals("", invoke(vararg, "getFirstPrefix"));
    assertEquals("a", invoke(vararg, "getPrefix"));
    assertEquals("b", invoke(vararg, "getSuffix"));

    sections = new LiveSQLFunctionTemplateParser().parse("#{a?b?c}");
    assertEquals(1, sections.size());
    vararg = sections.get(0);
    assertEquals("VarArgSection", vararg.getClass().getSimpleName());
    assertEquals("a", invoke(vararg, "getFirstPrefix"));
    assertEquals("b", invoke(vararg, "getPrefix"));
    assertEquals("c", invoke(vararg, "getSuffix"));

    sections = new LiveSQLFunctionTemplateParser().parse("#{??}");
    assertEquals(1, sections.size());
    vararg = sections.get(0);
    assertEquals("VarArgSection", vararg.getClass().getSimpleName());
    assertEquals("", invoke(vararg, "getFirstPrefix"));
    assertEquals("", invoke(vararg, "getPrefix"));
    assertEquals("", invoke(vararg, "getSuffix"));

    sections = new LiveSQLFunctionTemplateParser().parse("#{ax?bx?cx}");
    assertEquals(1, sections.size());
    vararg = sections.get(0);
    assertEquals("VarArgSection", vararg.getClass().getSimpleName());
    assertEquals("ax", invoke(vararg, "getFirstPrefix"));
    assertEquals("bx", invoke(vararg, "getPrefix"));
    assertEquals("cx", invoke(vararg, "getSuffix"));

  }

  @Test
  public void varargMustBeTheLastParameter() {
    InvalidLiveSQLFunctionTemplateException exception = assertThrows(InvalidLiveSQLFunctionTemplateException.class,
        () -> new LiveSQLFunctionTemplateParser().parse("concat(#{?, }, #{})"));

    assertTrue(exception.getMessage().contains("there can be no more parameters after a vararg parameter"));
  }

  private static Object invoke(Object section, String methodName) throws Exception {
    Method method = section.getClass().getDeclaredMethod(methodName);
    method.setAccessible(true);
    try {
      return method.invoke(section);
    } catch (InvocationTargetException exception) {
      throw new AssertionError(exception.getCause());
    }
  }

}
