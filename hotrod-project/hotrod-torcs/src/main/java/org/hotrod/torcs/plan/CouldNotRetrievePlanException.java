package org.hotrod.torcs.plan;

public abstract class CouldNotRetrievePlanException extends Exception {

  private static final long serialVersionUID = 1L;

  public CouldNotRetrievePlanException(String message) {
    super(message);
  }

}
