package app;

import java.time.LocalDate;

public class DateRange {

  private LocalDate minDate;
  private LocalDate maxDate;

  private DateRange(LocalDate minDate, LocalDate maxDate) {
    this.minDate = minDate;
    this.maxDate = maxDate;
  }

  public static DateRange of(LocalDate minDate, LocalDate maxDate) {
    return new DateRange(minDate, maxDate);
  }

  public final LocalDate getMinDate() {
    return minDate;
  }

  public final LocalDate getMaxDate() {
    return maxDate;
  }

}
