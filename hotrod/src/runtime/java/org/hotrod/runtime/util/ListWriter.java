package org.hotrod.runtime.util;

import java.util.Arrays;
import java.util.List;

public class ListWriter {

  private String prefix;
  private String elemPrefix;
  private String elemSuffix;
  private String separator;
  private String lastSeparator;
  private String suffix;

  private int rendered;
  private int added;
  private String last;
  private StringBuilder sb;
  private String result;

  public ListWriter(final String prefix, final String elemPrefix, final String elemSuffix, final String separator,
      final String lastSeparator, final String suffix) {
    initialize(prefix, elemPrefix, elemSuffix, separator, lastSeparator, suffix);
  }

  public ListWriter(final String separator) {
    initialize("", "", "", separator, separator, "");
  }

  public ListWriter(final String separator, final String lastSeparator) {
    initialize("", "", "", separator, lastSeparator, "");
  }

  private void initialize(final String prefix, final String elemPrefix, final String elemSuffix, final String separator,
      final String lastSeparator, final String suffix) {
    this.prefix = prefix;
    this.elemPrefix = elemPrefix;
    this.elemSuffix = elemSuffix;
    this.separator = separator;
    this.lastSeparator = lastSeparator;
    this.suffix = suffix;

    this.rendered = 0;
    this.last = null;
    this.sb = new StringBuilder();
    this.result = null;
    this.added = 0;

    this.sb.append(this.prefix);
  }

  public synchronized void add(final String txt) {
    if (this.result != null) {
      throw new IllegalStateException("The String was already rendered and cannot be modified.");
    }
    if (txt == null) {
      throw new IllegalArgumentException("Cannot add a null element to a " + ListWriter.class.getName() + ".");
    }
    if (this.last == null) {
      this.last = txt;
    } else {
      renderElement(this.last, false);
      this.last = txt;
    }
    this.added++;
  }

  private void renderElement(final String txt, final boolean isLast) {
    if (this.rendered > 0) {
      this.sb.append(isLast ? this.lastSeparator : this.separator);
    }
    this.sb.append(this.elemPrefix);
    this.sb.append(txt);
    this.sb.append(this.elemSuffix);
    this.rendered++;
  }

  public synchronized String toString() {
    if (this.result == null) {
      if (this.last != null) {
        renderElement(this.last, true);
      }
      this.sb.append(this.suffix);
      this.result = this.sb.toString();
    }
    return this.result;
  }

  // Utility methods

  public static String render(final String[] list) {
    List<String> asList = Arrays.asList("");
    return render(asList);
  }

  public static String render(final String[] list, final String prefix, final String elemPrefix,
      final String elemSuffix, final String separator, final String lastSeparator, final String suffix) {
    List<String> asList = Arrays.asList(list);
    String render = render(asList, prefix, elemPrefix, elemSuffix, separator, lastSeparator, suffix);
    return render;
  }

  public static String render(final List<String> list) {
    return render(list, "", "'", "'", ", ", ", ", "");
  }

  public static String render(final List<String> list, final String prefix, final String elemPrefix,
      final String elemSuffix, final String separator, final String lastSeparator, final String suffix) {
    ListWriter r = new ListWriter(prefix, elemPrefix, elemSuffix, separator, lastSeparator, suffix);
    for (String e : list) {
      r.add(e);
    }
    return r.toString();
  }

  public int getCount() {
    return this.added;
  }

}
