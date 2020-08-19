package com.example.springboot;

public class Metrics {

  public static void record(final String method, final long elapsedTime, final boolean succeeded) {
    System.out.println("--> Call: " + method + " (" + elapsedTime + " ms) -- " + (succeeded ? "succeeded" : "failed"));
  }

}
