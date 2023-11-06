package org.hotrod.torcs.loggers;

import org.hotrod.torcs.rankings.QueryExecution;

public class ConsoleQueryLogger extends QueryLogger {

  @Override
  public String getTitle() {
    return "Console Query Logger";
  }

  @Override
  public void consume(final QueryExecution q) {
    System.out.println("[query] " + q.getSQL());
  }

}
