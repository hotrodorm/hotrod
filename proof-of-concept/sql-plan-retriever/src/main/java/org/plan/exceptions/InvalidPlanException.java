package org.plan.exceptions;

public class InvalidPlanException extends Exception {

  private static final long serialVersionUID = 1L;

  public InvalidPlanException(final String message) {
    super(message);
  }

}