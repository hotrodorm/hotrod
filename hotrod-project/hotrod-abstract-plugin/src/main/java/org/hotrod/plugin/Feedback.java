package org.hotrod.plugin;

public interface Feedback {

  void info(String line);

  void warn(String line);

  void error(String line);

}
