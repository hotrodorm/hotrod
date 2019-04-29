package org.hotrod.runtime.sql.expressions;

import java.util.ArrayDeque;
import java.util.Deque;

public class ExpressionWriter {

  private boolean multiLineexpressions;
  private int level;
  private boolean atStartOfLine;

  private StringBuilder sb;
  private Deque<Marker> stack;
  private boolean separatorWritten;
  private String leftMargin;

  public ExpressionWriter(final boolean multiLineExpressions) {
    this.multiLineexpressions = multiLineExpressions;
    this.level = 0;
    this.atStartOfLine = true;
    this.sb = new StringBuilder();
    this.stack = new ArrayDeque<Marker>();
    this.separatorWritten = true;
    this.leftMargin = "";
    log("::constructor #1: this.multiLine=" + this.multiLineexpressions);
  }

  public ExpressionWriter(final boolean multiLine, final int leftIndent, final boolean marginFirstLine) {
    this.multiLineexpressions = multiLine;
    this.level = 0;
    this.atStartOfLine = marginFirstLine;
    this.sb = new StringBuilder();
    this.stack = new ArrayDeque<Marker>();
    this.separatorWritten = true;
    this.leftMargin = this.repeat('x', leftIndent);
    log("::constructor #2: this.multiLine=" + this.multiLineexpressions);
  }

  public void enterLevel(final boolean parenthesis, final boolean inLine) {
    log("enterLevel(" + parenthesis + ")");
    if (parenthesis) {
      this.text("(");
    }
    this.stack.push(new Marker(parenthesis, inLine));
    this.level++;
    if (this.multiLineexpressions && !inLine) {
      this.newLine();
      this.atStartOfLine = true;
    }
  }

  public void exitLevel() {
    log("exitLevel()");
    Marker marker = this.stack.pop();
    this.level--;
    if (this.multiLineexpressions && !marker.inLine) {
      this.newLine();
      this.indent();
    }
    if (marker.parenthesis) {
      this.text(")");
    }
  }

  private void separator() {
    log(" --- separator() " + (this.separatorWritten ? "-" : "X") + " ---");
    if (!this.separatorWritten) {
      if (this.atStartOfLine) {
        if (this.multiLineexpressions) {
          this.indent();
        }
      }
      this.sb.append(" ");
      this.separatorWritten = true;
    }
  }

  public void connector(final String txt) {
    log("connector(" + txt + ")");
    if (txt == null || txt.isEmpty()) {
      return;
    }
    this.separator();
    this.text(txt);
    this.separator();
  }

  public void text(final String txt) {
    log("'" + txt + "'");
    if (txt == null || txt.isEmpty()) {
      return;
    }
    String[] lines = txt.split("\n");
    if (lines != null) {
      for (int i = 0; i < lines.length; i++) {
        if (i > 0) {
          newLine();
          this.indent();
        }
        String l = lines[i].trim();
        if (this.atStartOfLine) {
          if (this.multiLineexpressions) {
            this.indent();
          }
        }
        this.sb.append(l);
        this.separatorWritten = false;
      }
    }
  }

  private void newLine() {
    log("-newLine()");
    this.sb.append("\n");
    this.separatorWritten = false;
  }

  private void indent() {
    this.atStartOfLine = false;
    this.sb.append(this.leftMargin);
    for (int i = 0; i < this.level * 2; i++) {
      this.sb.append(".");
    }
    this.separatorWritten = true;
  }

  private String repeat(final char c, final int times) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < times; i++) {
      sb.append(c);
    }
    return sb.toString();
  }

  public String render() {
    log("render()");
    return sb.toString();
  }

  private void log(final String txt) {
    // System.out.println("[log] -> " + txt);
  }

  private static class Marker {

    boolean parenthesis;
    boolean inLine;

    public Marker(final boolean parenthesis, final boolean inLine) {
      this.parenthesis = parenthesis;
      this.inLine = inLine;
    }

  }

}
