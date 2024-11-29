package org.hotrodorm.hotrod.utils;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class XUtil {

  public static String trim(final Throwable e) {
    return stream(e).map(c -> renderMessage(c)).collect(Collectors.joining(": "));
  }

  private static String renderMessage(final Throwable e) {
    return (e.getClass().equals(ClassNotFoundException.class) ? "Class not found: "
        : (e.getClass().equals(FileNotFoundException.class) ? "File not found: " : "")) + e.getMessage();
  }

  public static Stream<Throwable> stream(final Throwable e) {
    IterableThrowable it = new IterableThrowable(e);
    return StreamSupport.stream(it.spliterator(), false);
  }

  private static class IterableThrowable implements Iterable<Throwable> {

    private Throwable e;

    public IterableThrowable(final Throwable e) {
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
          if (e == null) {
            throw new NoSuchElementException();
          }
          Throwable aux = e;
          e = e.getCause();
          return aux;
        }
      };
    }

  }

}
