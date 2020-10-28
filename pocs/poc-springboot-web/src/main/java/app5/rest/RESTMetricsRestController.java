package app5.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app5.metrics.RESTMetricsAspect.RESTMetrics;

@RequestMapping("/metrics/rest")
@RestController
public class RESTMetricsRestController {

  @Autowired
  private RESTMetrics restMetrics;

  @GetMapping("by_highest_avg_response_time")
  public String getByHighestAvgResponseTime() {
    return this.restMetrics.getByHighestAvgResponseTime();
  }

  @GetMapping("by_highest_response_time")
  public String getByHighestResponseTime() {
    return this.restMetrics.getByHighestResponseTime();
  }

  @GetMapping("by_most_executed")
  public String getByMostExecuted() {
    return this.restMetrics.getByMostExecuted();
  }

  @GetMapping("by_most_recently_executed")
  public String getByMostRecentlyExecuted() {
    return this.restMetrics.getByMostRecentlyExecuted();
  }

  @GetMapping("by_most_errors")
  public String getByMostErrors() {
    return this.restMetrics.getByMostErrors();
  }

  @GetMapping("errors_by_most_recent")
  public String getErrorsByMostRecent() {
    return this.restMetrics.getErrorsByMostRecent();
  }

  @GetMapping("activate")
  public void activate() {
    this.restMetrics.activate();
  }

  @GetMapping("deactivate")
  public void deactivate() {
    this.restMetrics.deactivate();
  }

  @GetMapping("status")
  public String getIsActive() {
    return this.restMetrics.isActive() ? "Active" : "Inactive";
  }

}
