package org.hotrod.torcs.loggers;

import org.hotrod.torcs.rankings.Query;

public class ConsoleQueryLogger extends QueryLogger {

  @Override
  public String getTitle() {
    return "Console Query Logger";
  }

  @Override
  public void consume(final Query q) {
    System.out.println("[query] " + q.getSQL());
  }

}
