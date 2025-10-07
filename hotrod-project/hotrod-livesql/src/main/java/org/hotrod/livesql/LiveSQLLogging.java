package org.hotrod.livesql;

public class LiveSQLLogging {

  public static final LiveSQLLogging NO_LOGGING = new LiveSQLLogging(() -> false, (msg) -> {
  });

  public interface Enabled {
    boolean enabled();
  }

  public interface Logger {
    void log(String msg);
  }

  private Enabled enabled;
  private Logger logger;

  private LiveSQLLogging(Enabled enabled, Logger logger) {
    this.enabled = enabled;
    this.logger = logger;
  }

  public static LiveSQLLogging of(Enabled enabled, Logger logger) {
    return new LiveSQLLogging(enabled, logger);
  }

  public boolean enabled() {
    return this.enabled != null && this.enabled.enabled();
  }

  public void log(String msg) {
    if (this.logger != null) {
      this.logger.log(msg);
    }
  }

}
