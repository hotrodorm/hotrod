package org.hotrod.runtime.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.TimeZone;

public class JSONObject implements JSONValue {

  private LinkedHashMap<JSONString, JSONValue> properties = new LinkedHashMap<JSONString, JSONValue>();

  // Standard JSON types

  public void addProperty(final String name, final JSONObject o) {
    this.add(name, o);
  }

  public void addProperty(final String name, final JSONArray a) {
    this.add(name, a);
  }

  public void addProperty(final String name, final String s) {
    this.add(name, s != null ? new JSONString(s) : new JSONNullValue());
  }

  public void addProperty(final String name, final Number n) {
    this.add(name, n != null ? new JSONNumber(n) : new JSONNullValue());
  }

  public void addProperty(final String name, final boolean b) {
    this.add(name, new JSONBoolean(b));
  }

  public void addNullProperty(final String name) {
    this.add(name, new JSONNullValue());
  }

  // Types not covered by JSON spec

  public void addProperty(final String name, final java.util.Date d) {
    this.add(name, d == null ? new JSONNullValue() : new JSONString(formatTimestamp(d)));
  }

  public void addProperty(final String name, final Object obj) {
    this.add(name, obj == null ? new JSONNullValue() : new JSONString(obj.toString()));
  }

  // Internal

  private void add(final String name, final JSONValue v) {
    if (name == null) {
      throw new IllegalArgumentException("Cannot add property with empty name.");
    }
    JSONString key = new JSONString(name);
    if (this.properties.containsKey(key)) {
      throw new IllegalArgumentException("Cannot add property '" + name + "' since it already exists.");
    }
    this.properties.put(key, v != null ? v : new JSONNullValue());
  }

  @Override
  public String render() {
    StringBuilder sb = new StringBuilder();
    sb.append("{ ");
    boolean first = true;
    for (JSONString key : this.properties.keySet()) {
      if (first) {
        first = false;
      } else {
        sb.append(", ");
      }
      JSONValue v = this.properties.get(key);
      sb.append(key.render());
      sb.append(": ");
      sb.append(v.render());
    }
    sb.append(" }");
    return sb.toString();
  }

  // Utilities

  static String formatTimestamp(final java.util.Date d) {
    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    df.setTimeZone(tz);
    String formatted = df.format(d);
    return formatted;
  }

}
