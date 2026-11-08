package org.hotrod.functions;

public class VarArgSection implements Section {

  private String firstPrefix;
  private String prefix;
  private String suffix;

  public VarArgSection(String firstPrefix, String prefix, String suffix) {
    this.firstPrefix = firstPrefix == null ? "" : firstPrefix;
    this.prefix = prefix == null ? "" : prefix;
    this.suffix = suffix == null ? "" : suffix;
  }

  public String getFirstPrefix() {
    return firstPrefix;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getSuffix() {
    return suffix;
  }

}
