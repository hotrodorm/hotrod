package org.hotrodorm.hotrod.utils;

public class Separator {

  private String separator;
  private boolean first;

  public Separator() {
    this.separator = ", ";
    this.first = true;
  }

  public Separator(final String separator) {
    this.separator = separator;
    this.first = true;
  }

  public String render() {
    if (this.first) {
      this.first = false;
      return "";
    } else {
      return this.separator;
    }
  }

}
