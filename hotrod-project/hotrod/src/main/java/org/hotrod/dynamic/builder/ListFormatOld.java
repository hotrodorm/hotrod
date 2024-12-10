package org.hotrod.dynamic.builder;

public class ListFormatOld {

  private String prefix;
  private String separator;
  private String suffix;
  private String[] removePrefixes;

  private ListFormatOld(String prefix, String separator, String suffix, String[] removePrefixes) {
    this.prefix = prefix;
    this.separator = separator;
    this.suffix = suffix;
    this.removePrefixes = removePrefixes;
  }

  public static ListFormatOld of(String prefix, String separator, String suffix, String... removePrefixes) {
    return new ListFormatOld(prefix, separator, suffix, removePrefixes);
  }

  public String getPrefix() {
    return prefix;
  }

  public String getSeparator() {
    return separator;
  }

  public String getSuffix() {
    return suffix;
  }

  public String[] getRemovePrefixes() {
    return removePrefixes;
  }

  public static class Decorators {

    private String before;
    private String after;

    public Decorators(String before, String after) {
      this.before = before;
      this.after = after;
    }

    public String getBefore() {
      return before;
    }

    public String getAfter() {
      return after;
    }

  }

}
