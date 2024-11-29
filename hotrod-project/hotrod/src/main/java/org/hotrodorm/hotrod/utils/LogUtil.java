package org.hotrodorm.hotrod.utils;

public class LogUtil {

  public static String getCaller() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    return stackTrace[3].getClassName() + "." + stackTrace[3].getMethodName() + "(" + stackTrace[3].getLineNumber()
        + ")";
  }

  public static String renderStack() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    StringBuilder sb = new StringBuilder();
    for (int i = 3; i < stackTrace.length; i++) {
      StackTraceElement e = stackTrace[i];
      String line = String.format(" * %s.%s(%s:%s)%n", e.getClassName(), e.getMethodName(), e.getFileName(),
          e.getLineNumber());
      sb.append(line);
    }
    return sb.toString();
  }

}
