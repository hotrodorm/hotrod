package org.hotrod.livesql.util;

import org.hotrod.utils.SUtil;

public class ToString {

  private int level = 0;
  private String prompt = null;

  public void prompt(String p) {
    this.prompt = p;
  }

  public void indent() {
    this.level++;
  }

  public void unindent() {
    this.level--;
  }

  public void println() {
    System.out.println();
  }

  public void printObject(Object obj, String title) {
    String p = this.prompt;
    this.prompt = null;
    System.out.println(SUtil.getFiller(' ', this.level * 2) + "+ " + (p == null ? "" : "" + p + ": ")
        + (obj == null ? "null"
            : "[" + obj.getClass().getSimpleName() + "@" + String.format("%08x", System.identityHashCode(obj)) + "] ")
        + title);
  }

  public void printProperty(String name, Object value) {
    String p = this.prompt;
    this.prompt = null;
    System.out
        .println(
            SUtil.getFiller(' ', (this.level + 1) * 2) + "- " + (p == null ? "" : p + ": ") + name + ": "
                + (value == null ? ""
                    : "[" + value.getClass().getSimpleName() + "@"
                        + String.format("%08x", System.identityHashCode(value)) + "] ")
                + (value == null ? "null" : value));
  }

}
