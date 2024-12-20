package org.hotrod.utils;

public class Separator {

  private String prefix;
  private String separator;
  private boolean first;

  public Separator() {
    initialize(null, ", ");
  }

  public Separator(final String separator) {
    initialize(null, separator);
  }

  public Separator(final String prefix, final String separator) {
    initialize(prefix, separator);
  }

  private void initialize(final String prefix, final String separator) {
    this.prefix = prefix;
    this.separator = separator;
    this.first = true;
  }

  public void reset() {
    this.first = true;
  }

  public static Separator of(final String s) {
    return new Separator(s);
  }

  public static Separator of(final String prefix, final String s) {
    return new Separator(prefix, s);
  }

  public String render() {
    if (this.first) {
      this.first = false;
      return this.prefix != null ? this.prefix : "";
    } else {
      return this.separator;
    }
  }

}
