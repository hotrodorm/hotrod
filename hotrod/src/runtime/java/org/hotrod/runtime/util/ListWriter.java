package org.hotrod.runtime.util;

import java.util.Arrays;
import java.util.Collection;
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

  public ListWriter(final String prefix, final String elemPrefix, final String elemSuffix, final String separator,
      final String suffix) {
    initialize(prefix, elemPrefix, elemSuffix, separator, separator, suffix);
  }

  public ListWriter(final String elemPrefix, final String elemSuffix, final String separator,
      final String lastSeparator) {
    initialize(null, elemPrefix, elemSuffix, separator, lastSeparator, null);
  }

  public ListWriter(final String elemPrefix, final String elemSuffix, final String separator) {
    initialize(null, elemPrefix, elemSuffix, separator, separator, null);
  }

  public ListWriter(final String separator) {
    initialize("", "", "", separator, separator, "");
  }

  public ListWriter(final String separator, final String lastSeparator) {
    initialize("", "", "", separator, lastSeparator, "");
  }

  private void initialize(final String prefix, final String elemPrefix, final String elemSuffix, final String separator,
      final String lastSeparator, final String suffix) {
    this.prefix = prefix == null ? "" : prefix;
    this.elemPrefix = elemPrefix == null ? "" : elemPrefix;
    this.elemSuffix = elemSuffix == null ? "" : elemSuffix;
    this.separator = separator == null ? "" : separator;
    this.lastSeparator = lastSeparator == null ? "" : lastSeparator;
    this.suffix = suffix == null ? "" : suffix;

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

  public static String render(final String[] array, final String separator) {
    List<String> asList = Arrays.asList(array);
    return render(asList, separator);
  }

  public static String render(final String[] array, final String prefix, final String elemPrefix,
      final String elemSuffix, final String separator, final String lastSeparator, final String suffix) {
    List<String> asList = Arrays.asList(array);
    String render = render(asList, prefix, elemPrefix, elemSuffix, separator, lastSeparator, suffix);
    return render;
  }

  public static String render(final Collection<String> collection, final String separator) {
    return render(collection, "", "", "", separator, separator, "");
  }

  public static String render(final Collection<String> collection, final String elemPrefix, final String elemSuffix,
      final String separator, final String lastSeparator) {
    return render(collection, "", elemPrefix, elemSuffix, separator, lastSeparator, "");
  }

  public static String render(final Collection<String> collection, final String prefix, final String elemPrefix,
      final String elemSuffix, final String separator, final String lastSeparator, final String suffix) {
    ListWriter r = new ListWriter(prefix, elemPrefix, elemSuffix, separator, lastSeparator, suffix);
    for (String e : collection) {
      r.add(e);
    }
    return r.toString();
  }

  public static String render(final Collection<String> collection, final String elemPrefix, final String elemSuffix,
      final String separator) {
    return render(collection, "", elemPrefix, elemSuffix, separator, "");
  }

  public static String render(final Collection<String> collection, final String prefix, final String elemPrefix,
      final String elemSuffix, final String separator, final String suffix) {
    return render(collection, prefix, elemPrefix, elemSuffix, separator, separator, suffix);
  }

  public int getCount() {
    return this.added;
  }

}
