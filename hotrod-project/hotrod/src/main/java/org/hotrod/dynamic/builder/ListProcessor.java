package org.hotrod.dynamic.builder;

public class ListProcessor {

  private ClauseFormatter header;
  private ClauseFormatter separator;
  private String[] removePrefixes;
  private ClauseFormatter tail;

  public ListProcessor(ClauseFormatter header, ClauseFormatter separator, String[] removePrefixes,
      ClauseFormatter tail) {
    this.header = header;
    this.separator = separator;
    this.removePrefixes = removePrefixes;
    this.tail = tail;
  }

  public String getHeader() {
    return header == null ? null : header.getValue();
  }

  public String getSeparator() {
    return separator == null ? null : separator.getValue();
  }

  public String[] getRemovePrefixes() {
    return removePrefixes;
  }

  public String getTail() {
    return tail == null ? null : tail.getValue();
  }

}
