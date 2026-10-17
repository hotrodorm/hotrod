package org.hotrod.json;

public class JSONNumber implements JSONValue {

  private Number value;

  public JSONNumber(final Number value) {
    this.value = value;
  }

  @Override
  public String render() {
    if (this.isValidJSON()) {
      return this.value == null ? "null" : "" + this.value;
    }
    throw new IllegalArgumentException("JSON does not support NaN or Infinity values.");
  }

  private boolean isValidJSON() {
    if (value instanceof Double) {
      Double d = (Double) value;
      return !d.isNaN() && !d.isInfinite();
    } else if (value instanceof Float) {
      Float f = (Float) value;
      return !f.isNaN() && !f.isInfinite();
    }
    return true; // Other number types are valid
  }
}
