package org.hotrod.runtime.json;

public class JSONNullValue implements JSONValue {

  @Override
  public String render() {
    return "null";
  }

}
