package org.hotrod.runtime.json;

public class JSONBoolean implements JSONValue {

  private boolean value;

  public JSONBoolean(final boolean value) {
    this.value = value;
  }

  @Override
  public String render() {
    return this.value ? "true" : "false";
  }

}
