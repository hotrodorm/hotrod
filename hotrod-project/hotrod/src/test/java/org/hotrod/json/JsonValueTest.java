package org.hotrod.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;

public class JsonValueTest {

  @Test
  public void stringsEscapeJsonCharacters() {
    assertEquals("\"quote=\\\" slash=\\\\ newline=\\n\"",
        new JSONString("quote=\" slash=\\ newline=\n").render());
    assertEquals("\\u0001", JSONString.escapeChar((char) 1));
    assertEquals("null", new JSONString(null).render());
  }

  @Test
  public void stringsHaveValueEquality() {
    assertEquals(new JSONString("same"), new JSONString("same"));
    assertEquals(new JSONString("same").hashCode(), new JSONString("same").hashCode());
    assertNotEquals(new JSONString("same"), new JSONString("other"));
  }

  @Test
  public void primitiveValuesRender() {
    assertEquals("12.5", new JSONNumber(12.5).render());
    assertEquals("true", new JSONBoolean(true).render());
    assertEquals("false", new JSONBoolean(false).render());
    assertEquals("null", new JSONNullValue().render());
  }

  @Test
  public void numbersRenderSupportedTypesAndNull() {
    assertEquals("42", new JSONNumber(42).render());
    assertEquals("9999999999", new JSONNumber(9999999999L).render());
    assertEquals("3.14", new JSONNumber(3.14f).render());
    assertEquals("-5.5", new JSONNumber(-5.5).render());
    assertEquals("123.456", new JSONNumber(new BigDecimal("123.456")).render());
    assertEquals("null", new JSONNumber(null).render());
  }

  @Test(expected = IllegalArgumentException.class)
  public void numbersRejectDoubleNaN() {
    new JSONNumber(Double.NaN).render();
  }

  @Test(expected = IllegalArgumentException.class)
  public void numbersRejectDoublePositiveInfinity() {
    new JSONNumber(Double.POSITIVE_INFINITY).render();
  }

  @Test(expected = IllegalArgumentException.class)
  public void numbersRejectDoubleNegativeInfinity() {
    new JSONNumber(Double.NEGATIVE_INFINITY).render();
  }

  @Test(expected = IllegalArgumentException.class)
  public void numbersRejectFloatNaN() {
    new JSONNumber(Float.NaN).render();
  }

  @Test(expected = IllegalArgumentException.class)
  public void numbersRejectFloatPositiveInfinity() {
    new JSONNumber(Float.POSITIVE_INFINITY).render();
  }

  @Test(expected = IllegalArgumentException.class)
  public void numbersRejectFloatNegativeInfinity() {
    new JSONNumber(Float.NEGATIVE_INFINITY).render();
  }

  @Test
  public void objectRendersTypedPropertiesInInsertionOrder() {
    JSONObject object = new JSONObject();
    object.addProperty("name", "Ada");
    object.addProperty("age", 36);
    object.addProperty("active", true);
    object.addNullProperty("missing");
    assertEquals("{ \"name\": \"Ada\", \"age\": 36, \"active\": true, \"missing\": null }", object.render());
  }

  @Test(expected = IllegalArgumentException.class)
  public void objectRejectsDuplicateProperties() {
    JSONObject object = new JSONObject();
    object.addProperty("name", "first");
    object.addProperty("name", "second");
  }

  @Test
  public void arraysRenderNestedValuesAndCollectObjects() {
    JSONArray array = new JSONArray();
    array.addValue("one");
    array.addValue(2);
    array.addNullValue();
    assertEquals("[ \"one\", 2, null ]", array.render());

    JSONArray collected = Arrays.asList(object("a"), object("b")).stream().collect(JSONArray.toJSONArray());
    assertEquals("[ { \"value\": \"a\" }, { \"value\": \"b\" } ]", collected.render());
  }

  @Test
  public void arraysCanBeCollectedInParallel() {
    JSONArray collected = Arrays.asList(object("a"), object("b"), object("c"), object("d")).parallelStream()
        .collect(JSONArray.toJSONArray());
    assertEquals("[ { \"value\": \"a\" }, { \"value\": \"b\" }, { \"value\": \"c\" }, { \"value\": \"d\" } ]",
        collected.render());
  }

  private static JSONObject object(String value) {
    JSONObject object = new JSONObject();
    object.addProperty("value", value);
    return object;
  }
}
