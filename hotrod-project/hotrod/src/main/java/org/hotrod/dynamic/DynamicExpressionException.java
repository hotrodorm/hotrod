package org.hotrod.dynamic;

public class DynamicExpressionException extends Exception {

  private static final long serialVersionUID = 1L;

  public DynamicExpressionException(String message) {
    super(message);
  }

  public DynamicExpressionException(String message, Throwable cause) {
    super(message, cause);
  }

}
