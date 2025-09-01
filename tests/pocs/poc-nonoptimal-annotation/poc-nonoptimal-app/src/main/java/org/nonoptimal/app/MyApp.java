package org.nonoptimal.app;

import org.nonoptimal.NonOptimal;

public class MyApp {

  public void run(final String[] args) {
    goodMethod();
    oldMethod();
    badMethod();
  }

  public void goodMethod() {
  }

  @Deprecated
  public void oldMethod() {
  }

  @NonOptimal
  public void badMethod() {
  }

}
