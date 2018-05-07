package org.hotrod.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Correlator {

  public static <T extends Comparable<T>> List<CorrelatedEntry<T>> correlate(final Collection<T> left,
      final Collection<T> right) {

    List<T> sortedLeft = new ArrayList<T>(left);
    Collections.sort(sortedLeft);

    List<T> sortedRight = new ArrayList<T>(right);
    Collections.sort(sortedRight);

    return correlateSorted(sortedLeft, sortedRight);
  }

  public static <T extends Comparable<T>> List<CorrelatedEntry<T>> correlateSorted(final Collection<T> left,
      final Collection<T> right) {

    List<CorrelatedEntry<T>> result = new ArrayList<CorrelatedEntry<T>>();

    if (left == null && right == null) {
      return result;
    }

    if (left == null && right != null) {
      for (T r : right) {
        result.add(new CorrelatedEntry<T>(null, r));
      }
      return result;
    }

    if (left != null && right == null) {
      for (T l : left) {
        result.add(new CorrelatedEntry<T>(l, null));
      }
      return result;
    }

    Iterator<T> lit = left.iterator();
    Iterator<T> rit = right.iterator();

    T l = lit.hasNext() ? lit.next() : null;
    T r = rit.hasNext() ? rit.next() : null;

    while (l != null && r != null) {
      int comp = l.compareTo(r);
      if (comp == 0) {
        result.add(new CorrelatedEntry<T>(l, r));
        l = lit.hasNext() ? lit.next() : null;
        r = rit.hasNext() ? rit.next() : null;
      } else if (comp < 0) {
        result.add(new CorrelatedEntry<T>(l, null));
        l = lit.hasNext() ? lit.next() : null;
      } else {
        result.add(new CorrelatedEntry<T>(null, r));
        r = rit.hasNext() ? rit.next() : null;
      }
    }

    while (l != null) {
      result.add(new CorrelatedEntry<T>(l, null));
      l = lit.hasNext() ? lit.next() : null;
    }

    while (r != null) {
      result.add(new CorrelatedEntry<T>(null, r));
      r = rit.hasNext() ? rit.next() : null;
    }

    return result;

  }

  public static <T> List<CorrelatedEntry<T>> correlate(final Collection<T> left, final Collection<T> right,
      final Comparator<T> comparator) {

    if (comparator == null) {
      throw new NullPointerException("comparator is null");
    }

    List<T> sortedLeft = new ArrayList<T>(left);
    Collections.sort(sortedLeft, comparator);

    List<T> sortedRight = new ArrayList<T>(right);
    Collections.sort(sortedRight, comparator);

    return correlateSorted(sortedLeft, sortedRight, comparator);

  }

  public static <T> List<CorrelatedEntry<T>> correlateSorted(final Collection<T> left, final Collection<T> right,
      final Comparator<T> comparator) {

    if (comparator == null) {
      throw new NullPointerException("comparator is null");
    }

    List<CorrelatedEntry<T>> result = new ArrayList<CorrelatedEntry<T>>();

    if (left == null && right == null) {
      return result;
    }

    if (left == null && right != null) {
      for (T r : right) {
        result.add(new CorrelatedEntry<T>(null, r));
      }
      return result;
    }

    if (left != null && right == null) {
      for (T l : left) {
        result.add(new CorrelatedEntry<T>(l, null));
      }
      return result;
    }

    Iterator<T> lit = left.iterator();
    Iterator<T> rit = right.iterator();

    Entry<T> l = read(lit);
    Entry<T> r = read(rit);

    while (l.read && r.read) {
      T le = l.element;
      T re = r.element;
      int comp = comparator.compare(le, re);
      if (comp == 0) {
        result.add(new CorrelatedEntry<T>(le, re));
        l = read(lit);
        r = read(rit);
      } else if (comp < 0) {
        result.add(new CorrelatedEntry<T>(le, null));
        l = read(lit);
      } else {
        result.add(new CorrelatedEntry<T>(null, re));
        r = read(rit);
      }
    }

    while (l.read) {
      T le = l.element;
      result.add(new CorrelatedEntry<T>(le, null));
      l = read(lit);
    }

    while (r.read) {
      T re = r.element;
      result.add(new CorrelatedEntry<T>(null, re));
      r = read(rit);
    }

    return result;

  }

  private static <T> Entry<T> read(final Iterator<T> iterator) {
    if (iterator.hasNext()) {
      return new Entry<T>(true, iterator.next());
    } else {
      return new Entry<T>(false, null);
    }
  }

  // Classes

  private static class Entry<T> {

    public boolean read;
    public T element;

    public Entry(boolean read, T t) {
      this.read = read;
      this.element = t;
    }
  }

  public static class CorrelatedEntry<T> {

    private T left;
    private T right;

    private CorrelatedEntry(final T left, final T right) {
      this.left = left;
      this.right = right;
    }

    public T getLeft() {
      return left;
    }

    public T getRight() {
      return right;
    }

  }

}
