package org.hotrod.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
