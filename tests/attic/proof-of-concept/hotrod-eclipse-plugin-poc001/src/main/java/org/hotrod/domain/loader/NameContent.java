package org.hotrod.domain.loader;

public class NameContent {

  private String name;
  private String content;

  public NameContent(final String line, final String prompt) {
    String tail = line.substring(prompt.length()).trim();
    int colon = tail.indexOf(':');
    if (colon != -1) {
      this.name = tail.substring(0, colon).trim();
      this.content = tail.substring(colon + 1).trim();
    } else {
      this.name = tail;
      this.content = "";
    }
  }

  public String getName() {
    return name;
  }

  public String getContent() {
    return content;
  }

}
