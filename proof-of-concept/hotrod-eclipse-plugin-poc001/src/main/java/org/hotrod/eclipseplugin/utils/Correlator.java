package org.hotrod.eclipseplugin.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Correlator {

  public static <T extends Comparable<T>> List<CorrelatedEntry<T>> correlateUnsorted(final Collection<T> left,
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
      // display("left=" + left + " right=" + right + " comp=" + comp);
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

  // Classes

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

  // private static void display(final String txt) {
  // System.out.println("[Correlator] " + txt);
  // }

}
