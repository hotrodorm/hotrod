package org.hotrodorm.hotrod.utils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TUtil {

  public static String renderStackTrace() {
    return Stream.of(Thread.currentThread().getStackTrace()).skip(2)
        .map(s -> "" + s.getClassName() + "(" + s.getLineNumber() + ")").collect(Collectors.joining("\n"));
  }

  private static String simpleName(String cn) {
    int ix = cn.lastIndexOf('.');
    return ix == -1 ? cn : cn.substring(ix + 1);
  }

  public static String compactStackTrace() {
    return Stream.of(Thread.currentThread().getStackTrace()).skip(2)
        .map(s -> "" + simpleName(s.getClassName()) + "(" + s.getLineNumber() + ")").collect(Collectors.joining(" < "));
  }

}
