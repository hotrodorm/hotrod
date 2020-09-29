package app5.controller;

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

  @GetMapping("stats")
  public String getSQLMetrics() {
    return this.sqlMetrics.render();
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
