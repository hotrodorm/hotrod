package org.hotrod.runtime.json;

public class JSONNumber implements JSONValue {

  private Number value;

  public JSONNumber(final Number value) {
    this.value = value;
  }

  @Override
  public String render() {
    return this.value == null ? "null" : "" + this.value;
  }

}
