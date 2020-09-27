package app5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app5.metrics.SQLMetricsAspect.SQLMetrics;

@RequestMapping("/metrics")
@RestController
public class SQLMetricsRestController {

  @Autowired
  private SQLMetrics sqlMetrics;

  @GetMapping("sql")
  String getSQLMetrics() {
    return this.sqlMetrics.render();
  }

}
