package org.hotrod.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Timers {

  private static final Logger log = Logger.getLogger(Timers.class.getName());

  private long start;
  private long lastEnd;
  private List<Phase> phases;
  private boolean logging;

  public Timers(boolean logging) {
    this.start = System.currentTimeMillis();
    this.lastEnd = -1;
    this.phases = new ArrayList<>();
    this.logging = logging;
  }

  public static class Phase {

    private String name;
    private long end;
    private long duration;

    public Phase(String name, long le, long end) {
      this.name = name;
      this.end = end;
      this.duration = end - le;
    }

    public final String getName() {
      return name;
    }

    public final long getEnd() {
      return end;
    }

    public final long getDuration() {
      return duration;
    }

    public String toString() {
      return this.name + ": " + duration + " ms";
    }

  }

  public void endPhase(String name) {
    long le = this.lastEnd == -1 ? this.start : this.lastEnd;
    long end = System.currentTimeMillis();
    Phase p = new Phase(name, le, end);
    this.phases.add(p);
    this.lastEnd = end;
    if (this.logging) {
      log.info(p.toString());
    }
  }

}