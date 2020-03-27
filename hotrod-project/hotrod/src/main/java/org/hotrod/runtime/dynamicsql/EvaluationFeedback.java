package org.hotrod.runtime.dynamicsql;

public class EvaluationFeedback {

  private boolean contentRendered;

  public EvaluationFeedback(final boolean contentRendered) {
    this.contentRendered = contentRendered;
  }

  public void markProcessed() {
    this.contentRendered = true;
  }

  public boolean wasContentRendered() {
    return contentRendered;
  }

}
