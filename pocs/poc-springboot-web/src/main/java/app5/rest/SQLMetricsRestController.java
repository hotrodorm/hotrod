package app5.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app5.metrics.SQLMetricsAspect.SQLMetrics;

@RequestMapping("/metrics/sql")
@RestController
public class SQLMetricsRestController {

  @Autowired
  private SQLMetrics sqlMetrics;

  @GetMapping("by_highest_avg_response_time")
  public String getByHighestAvgResponseTime() {
    return this.sqlMetrics.getByHighestAvgResponseTime();
  }

  @GetMapping("by_highest_response_time")
  public String getByHighestResponseTime() {
    return this.sqlMetrics.getByHighestResponseTime();
  }

  @GetMapping("by_most_executed")
  public String getByMostExecuted() {
    return this.sqlMetrics.getByMostExecuted();
  }

  @GetMapping("by_most_recently_executed")
  public String getByMostRecentlyExecuted() {
    return this.sqlMetrics.getByMostRecentlyExecuted();
  }

  @GetMapping("by_most_errors")
  public String getByMostErrors() {
    return this.sqlMetrics.getByMostErrors();
  }

  @GetMapping("errors_by_most_recent")
  public String getErrorsByMostRecent() {
    return this.sqlMetrics.getErrorsByMostRecent();
  }

  @GetMapping("activate")
  public void activate() {
    this.sqlMetrics.activate();
  }

  @GetMapping("deactivate")
  public void deactivate() {
    this.sqlMetrics.deactivate();
  }

  @GetMapping("status")
  public String getIsActive() {
    return this.sqlMetrics.isActive() ? "Active" : "Inactive";
  }

}
