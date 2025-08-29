package org.hotrod.dynamicsql;

public class DynamicExpressionException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public DynamicExpressionException(String message) {
    super(message);
  }

  public DynamicExpressionException(String message, Throwable cause) {
    super(message, cause);
  }

}
