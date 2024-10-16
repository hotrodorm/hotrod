package org.hotrod.runtime.json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

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

  // Collector

  public static Collector<JSONObject, List<JSONObject>, JSONArray> toJSONArray() {
    return new JSONArrayCollector();
  }

  public static class JSONArrayCollector implements Collector<JSONObject, List<JSONObject>, JSONArray> {

    private List<JSONObject> elements = new ArrayList<>();

    @Override
    public Supplier<List<JSONObject>> supplier() {
      return new Supplier<List<JSONObject>>() {
        @Override
        public List<JSONObject> get() {
          return elements;
        }
      };
    }

    @Override
    public BiConsumer<List<JSONObject>, JSONObject> accumulator() {
      return new BiConsumer<List<JSONObject>, JSONObject>() {
        @Override
        public void accept(List<JSONObject> t, final JSONObject u) {
          t.add(u);
        }
      };
    }

    @Override
    public BinaryOperator<List<JSONObject>> combiner() {
      // No need to combine, since this collector is not concurrent
      return null;
    }

    @Override
    public Function<List<JSONObject>, JSONArray> finisher() {
      return new Function<List<JSONObject>, JSONArray>() {
        @Override
        public JSONArray apply(final List<JSONObject> t) {
          JSONArray a = new JSONArray();
          for (JSONObject e : elements) {
            a.add(e);
          }
          return a;
        }
      };

    }

    @Override
    public Set<Characteristics> characteristics() {
      return new HashSet<Characteristics>();
    }

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
