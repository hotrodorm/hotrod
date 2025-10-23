package org.hotrod.livesql;

public class LiveSQLLogging {

  public static final LiveSQLLogging NO_LOGGING = new LiveSQLLogging(() -> false, msg -> {
  }, () -> false, msg -> {
  });

  public interface Enabled {
    boolean enabled();
  }

  public interface Logger {
    void log(String msg);
  }

  private Enabled basicEnabled;
  private Logger basicLogger;
  private Enabled fullEnabled;
  private Logger fullLogger;

  private LiveSQLLogging(Enabled basicEnabled, Logger basicLogger, Enabled fullEnabled, Logger fullLogger) {
    this.basicEnabled = basicEnabled;
    this.basicLogger = basicLogger;
    this.fullEnabled = fullEnabled;
    this.fullLogger = fullLogger;
  }

  public static LiveSQLLogging of(Enabled basicEnabled, Logger basicLogger, Enabled fullEnabled, Logger fullLogger) {
    return new LiveSQLLogging(basicEnabled, basicLogger, fullEnabled, fullLogger);
  }

  public boolean basicEnabled() {
    return this.basicEnabled != null && this.basicEnabled.enabled();
  }

  public void basicLog(String msg) {
    if (this.basicLogger != null) {
      this.basicLogger.log(msg);
    }
  }

  public boolean fullEnabled() {
    return this.fullEnabled != null && this.fullEnabled.enabled();
  }

  public void fullLog(String msg) {
    if (this.fullLogger != null) {
      this.fullLogger.log(msg);
    }
  }

}
