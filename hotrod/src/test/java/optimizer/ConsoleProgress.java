package optimizer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ConsoleProgress {

  private static final double UPDATE_STEP = 0.1; // Update every 10% progress

  private String title;
  private int total;

  private long start;
  private double nextUpdate;
  private int nextUpdateInt;

  public ConsoleProgress(final String title, final int total) {
    this.title = title;
    this.total = total;
    this.start = System.currentTimeMillis();
    this.nextUpdate = UPDATE_STEP;
    this.nextUpdateInt = (int) Math.round(this.nextUpdate * this.total);
    this.displayStart();
  }

  public void update(final int current) {
    int currentLimited = Math.min(current, this.total);
    if (currentLimited >= this.nextUpdateInt) {
      this.displayUpdate(currentLimited);
      this.nextUpdate = 1.0 * currentLimited / this.total + UPDATE_STEP;
      this.nextUpdateInt = (int) Math.round(this.nextUpdate * this.total);
    }
  }

  private void displayStart() {
    System.out.println(this.title //
        // + " {" + new Date() + "}" //
        + ": 0%...");
  }

  private void displayUpdate(final double current) {
    System.out.println(" " + Math.round(100 * current / this.total) + "%...");
  }

  public void complete() {
    long end = System.currentTimeMillis();
    System.out.println(" 100% [" + formatElapsed(end - this.start) + "]");
    // System.out.println(new Date());
  }

  // m:ss or h:mm:ss
  public static String formatElapsed(long elapsed) {
    long hours = TimeUnit.MILLISECONDS.toHours(elapsed);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed) - TimeUnit.HOURS.toMinutes(hours);
    long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed)
        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsed));
    return hours == 0 ? String.format("%d:%02d", minutes, seconds)
        : String.format("%d:%02d:%02d", hours, minutes, seconds);
  }

}
