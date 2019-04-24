package org.hotrod.runtime.json;

import java.util.ArrayList;
import java.util.List;

public class JSONArray implements JSONValue {

  private List<JSONValue> values = new ArrayList<JSONValue>();

  // Standard JSON types

  public void addValue(final JSONObject o) {
    this.add(o);
  }

  public void addValue(final JSONArray a) {
    this.add(a);
  }

  public void addValue(final String s) {
    this.add(new JSONString(s));
  }

  public void addValue(final Number n) {
    this.add(new JSONNumber(n));
  }

  public void addValue(final boolean b) {
    this.add(new JSONBoolean(b));
  }

  public void addNullValue() {
    this.add(new JSONNullValue());
  }

  // Types not covered by JSON spec

  public void addValue(final java.util.Date d) {
    this.add(d == null ? new JSONNullValue() : new JSONString(JSONObject.formatTimestamp(d)));
  }

  public void addValue(final Object obj) {
    this.add(obj == null ? new JSONNullValue() : new JSONString(obj.toString()));
  }

  // Internal

  private void add(JSONValue v) {
    this.values.add(v != null ? v : new JSONNullValue());
  }

  @Override
  public String render() {
    StringBuilder sb = new StringBuilder();
    sb.append("[ ");
    boolean first = true;
    for (JSONValue v : this.values) {
      if (first) {
        first = false;
      } else {
        sb.append(", ");
      }
      sb.append(v.render());
    }
    sb.append(" ]");
    return sb.toString();
  }
}
