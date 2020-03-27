package org.hotrod.runtime.json;

public class JSONString implements JSONValue {

  private String value;

  public JSONString(final String value) {
    this.value = value;
  }

  @Override
  public String render() {
    if (this.value == null) {
      return "null";
    } else {
      StringBuilder sb = new StringBuilder();
      sb.append("\"");
      for (int i = 0; i < this.value.length(); i++) {
        char c = this.value.charAt(i);
        if (c == '"') { // "
          sb.append("\\\"");
        } else if (c == '\\') { // \
          sb.append("\\\\");
        } else if (c == '/') { // /
          sb.append("\\/");
        } else if (c == '\b') { // backspace
          sb.append("\\b");
        } else if (c == '\f') { // formfeed
          sb.append("\\f");
        } else if (c == '\n') { // newline
          sb.append("\\n");
        } else if (c == '\r') { // carriage return
          sb.append("\\r");
        } else if (c == '\t') { // horizontal tab
          sb.append("\\t");
        } else if (c < 32) { // control character
          sb.append(escapeChar(c));
        } else {
          sb.append(c);
        }
      }
      sb.append("\"");
      return sb.toString();
    }
  }

  public static String escapeChar(final char c) {
    int i = c;
    return "\\u" + String.format("%04x", i);
  }

  // Indexable

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    JSONString other = (JSONString) obj;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }

}
