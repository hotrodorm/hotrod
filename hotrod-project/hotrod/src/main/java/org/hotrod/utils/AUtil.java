package org.hotrod.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AUtil {

  @SafeVarargs
  public static <T> T[] concat(final T... a) {
    return a;
  }

  @SafeVarargs
  public static <T> T[] concat(final T[] a, final T... b) {
    T[] r = Arrays.copyOf(a, a.length + b.length);
    System.arraycopy(b, 0, r, a.length, b.length);
    return r;
  }

  public static <T> T[] concat(final T a, final T[] b) {
    T[] r = Arrays.copyOfRange(b, b.length, b.length * 2 + 1);
    r[0] = a;
    System.arraycopy(b, 0, r, 1, b.length);
    return r;
  }

  @SuppressWarnings("unchecked")
  @SafeVarargs
  public static <T> T[] concat(final T[]... arrays) {
    if (arrays.length == 0) {
      return (T[]) new Object[0];
    }
    List<T> l = new ArrayList<T>();
    for (T[] a : arrays) {
      l.addAll(Arrays.asList(a));
    }
    return l.toArray(Arrays.copyOf(arrays[0], l.size()));
  }

}
