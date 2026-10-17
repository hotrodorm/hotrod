package org.hotrod.utils;

public class LogUtil {

  public static String getCaller() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    int ix = findCurrentClassIndex(stackTrace);
    return ix == -1 ? null
        : stackTrace[ix + 1].getClassName() + "." + stackTrace[ix + 1].getMethodName() + "("
            + stackTrace[ix + 1].getLineNumber() + ")";
  }

  private static int findCurrentClassIndex(StackTraceElement[] stackTrace) {
    for (int i = 0; i < stackTrace.length; i++) {
      StackTraceElement e = stackTrace[i];
      if (e.getClassName().equals(LogUtil.class.getName())) {
        return i;
      }
    }
    return -1;
  }

  public static String renderStack() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    int ix = findCurrentClassIndex(stackTrace);
    if (ix == -1) {
      return "Stack trace not available";
    }
    StringBuilder sb = new StringBuilder();
    for (int i = ix + 1; i < stackTrace.length; i++) {
      StackTraceElement e = stackTrace[i];
      String line = String.format(" * %s.%s(%s:%s)%n", e.getClassName(), e.getMethodName(), e.getFileName(),
          e.getLineNumber());
      sb.append(line);
    }
    return sb.toString();
  }

}
