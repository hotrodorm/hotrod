package org.hotrod.eclipseplugin.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Matcher {

  public List<MatcherResultEntry<Comparable>> match(final Collection<Comparable> leftCollection,
      final Collection<Comparable> rightCollection) {

    List<MatcherResultEntry<Comparable>> result = new ArrayList<MatcherResultEntry<Comparable>>();
    Iterator<Comparable> lit = leftCollection.iterator();
    Iterator<Comparable> rit = leftCollection.iterator();

    Comparable left = lit.hasNext() ? lit.next() : null;
    Comparable right = rit.hasNext() ? rit.next() : null;

    while (left != null && right != null) {
      int comp = left.compareTo(right);
      if (comp == 0) {
        result.add(new MatcherResultEntry<Comparable>(left, right));
        left = lit.hasNext() ? lit.next() : null;
        right = rit.hasNext() ? rit.next() : null;
      } else if (comp < 0) {
        result.add(new MatcherResultEntry<Comparable>(left, null));
        left = lit.hasNext() ? lit.next() : null;
      } else {
        result.add(new MatcherResultEntry<Comparable>(null, right));
        right = rit.hasNext() ? rit.next() : null;
      }
    }

    while (left != null) {
      result.add(new MatcherResultEntry<Comparable>(left, null));
      left = lit.hasNext() ? lit.next() : null;
    }

    while (right != null) {
      result.add(new MatcherResultEntry<Comparable>(null, right));
      right = rit.hasNext() ? rit.next() : null;
    }

    return result;

  }

  // Classes

  public static class MatcherResultEntry<Comparable> {

    private Comparable left;
    private Comparable right;

    private MatcherResultEntry(final Comparable left, final Comparable right) {
      super();
      this.left = left;
      this.right = right;
    }

    public Comparable getLeft() {
      return left;
    }

    public Comparable getRight() {
      return right;
    }

  }

}
