package org.hotrodorm.hotrod.utils;

import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class XUtil {

  public static String renderThrowable(final Throwable e) {
    return stream(e)
        .map(c -> (c.getClass().equals(ClassNotFoundException.class) ? "Class not found: " : "") + c.getMessage())
        .collect(Collectors.joining(": "));
  }

  public static Stream<Throwable> stream(final Throwable e) {
    IterableThrowable it = new IterableThrowable(e);
    return StreamSupport.stream(it.spliterator(), false);
  }

  private static class IterableThrowable implements Iterable<Throwable> {

    private Throwable e;

    public IterableThrowable(final Throwable e) {
      if (e == null) {
        throw new IllegalArgumentException("Cannot iterate over a null exception");
      }
      this.e = e;
    }

    @Override
    public Iterator<Throwable> iterator() {
      return new Iterator<Throwable>() {

        @Override
        public boolean hasNext() {
          return e != null;
        }

        @Override
        public Throwable next() {
          Throwable aux = e;
          e = e.getCause();
          return aux;
        }
      };
    }

  }

}
