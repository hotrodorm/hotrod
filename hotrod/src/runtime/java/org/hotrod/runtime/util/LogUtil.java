package org.hotrod.runtime.util;

public class LogUtil {

  public static String getCaller() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    return stackTrace[3].getClassName() + "." + stackTrace[3].getMethodName() + "(" + stackTrace[3].getLineNumber()
        + ")";
  }

  public static String renderStack() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    ListWriter w = new ListWriter("\n");
    for (int i = 3; i < stackTrace.length; i++) {
      StackTraceElement e = stackTrace[i];
      w.add(" * " + e.getClassName() + "." + e.getMethodName() + "(" + e.getLineNumber() + ")");
    }
    return w.toString();
  }

}
