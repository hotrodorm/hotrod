package org.hotrod.runtime.util;

public class LogUtils {

  public static String getCaller() {
    final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    return stackTrace[3].getClassName() + "." + stackTrace[3].getMethodName() + "(" + stackTrace[3].getLineNumber()
        + ")";
  }

}
