package org.hotrodorm.hotrod.utils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TUtil {

  public static String renderStackTrace() {
    return Stream.of(Thread.currentThread().getStackTrace())
        .map(s -> "" + s.getClassName() + "(" + s.getLineNumber() + ")").collect(Collectors.joining("\n"));
  }

}
