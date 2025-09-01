package app5.metrics;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hotrodorm.hotrod.utils.XUtil;
import org.nocrala.tools.lang.collector.listcollector.ListCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RESTMetricsAspect {

  @Autowired
  private RESTMetrics restMetrics;

  @Around("within(app5.rest..*)")
  private Object measureRESTCall(final ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();

    /**
     * <pre>
     Signature methods:
     toString():             ProductVO app5.rest.ProductRestController.getProduct(String)
     toShortString():        ProductRestController.getProduct(..)
     toLongString():         app5.persistence.ProductVO app5.rest.ProductRestController.getProduct(java.lang.String)
     getDeclaringType():     class app5.rest.ProductRestController
     getName():              getProduct
     getDeclaringTypeName(): app5.rest.ProductRestController
     getModifiers():         0
     * </pre>
     */

    String serviceId = joinPoint.getSignature().toString();

    try {
      Object ps = joinPoint.proceed();
      long end = System.currentTimeMillis();
      this.restMetrics.record(serviceId, end - start, null);
      return ps;

    } catch (Throwable t) {
      long end = System.currentTimeMillis();
      this.restMetrics.record(serviceId, end - start, t);
      throw t;
    }
  }

  // Helpers

  public String toString() {
    return this.getClass().getName() + "[" + System.identityHashCode(this) + "]";
  }

  // Inner classes

  @Component
  public static class RESTMetrics {

    private boolean active = true;
    private Map<String, ServiceMetrics> metrics = new HashMap<String, ServiceMetrics>();

    public void activate() {
      this.active = true;
    }

    public void deactivate() {
      this.active = false;
    }

    public boolean isActive() {
      return this.active;
    }

    public void record(final String serviceId, final long elapsedTime, final Throwable t) {
      if (this.active) {
        recordExecution(serviceId, elapsedTime, t);
      }
    }

    private synchronized void recordExecution(final String serviceId, final long elapsedTime, final Throwable t) {
      ServiceMetrics sm = this.metrics.get(serviceId);
      if (sm == null) {
        sm = new ServiceMetrics(serviceId);
        this.metrics.put(serviceId, sm);
      }
      sm.record(elapsedTime, t);
    }

    // Get stats

    public synchronized String getByHighestAvgResponseTime() {
      return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getAverageTime(), b.getAverageTime()))
          .map(s -> "> " + s).collect(ListCollector.concat("\n"));
    }

    public synchronized String getByHighestResponseTime() {
      return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getMaxTime(), b.getMaxTime()))
          .map(s -> "> " + s).collect(ListCollector.concat("\n"));
    }

    public synchronized String getByMostExecuted() {
      return this.metrics.values().stream()
          .sorted((a, b) -> -Long.compare(a.getTotalExecutions(), b.getTotalExecutions())).map(s -> "> " + s)
          .collect(ListCollector.concat("\n"));
    }

    public synchronized String getByMostRecentlyExecuted() {
      return this.metrics.values().stream().sorted((a, b) -> -Long.compare(a.getLastExecuted(), b.getLastExecuted()))
          .map(s -> "> " + s).collect(ListCollector.concat("\n"));
    }

    public synchronized String getByMostErrors() {
      return this.metrics.values().stream()
          .sorted((a, b) -> -Long.compare(a.getExecutionErrors(), b.getExecutionErrors())).map(s -> "> " + s)
          .collect(ListCollector.concat("\n"));
    }

    public synchronized String getErrorsByMostRecent() {
      return this.metrics.values().stream().filter(a -> a.getExecutionErrors() > 0)
          .sorted((a, b) -> -Long.compare(a.getLastExecuted(), b.getLastExecuted())).map(s -> "> " + s)
          .collect(ListCollector.concat("\n"));
    }

  }

  public static class ServiceMetrics {

    private String serviceId;

    private long minTime;
    private long maxTime;

    private long totalExecutions;
    private long executionErrors;
    private long sum;
    private long sumSQ;

    private long lastExecuted;

    private long lastExceptionTimestamp;
    private Throwable lastException;

    public ServiceMetrics(final String serviceId) {
      this.serviceId = serviceId;

      this.minTime = 0;
      this.maxTime = 0;

      this.totalExecutions = 0;
      this.executionErrors = 0;
      this.sum = 0;
      this.sumSQ = 0;

      this.lastExecuted = 0;
      this.lastExceptionTimestamp = 0;
      this.lastException = null;
    }

    public void record(final long elapsedTime, final Throwable t) {
      this.lastExecuted = System.currentTimeMillis();
      if (this.totalExecutions == 0 || elapsedTime < this.minTime) {
        this.minTime = elapsedTime;
      }
      if (this.totalExecutions == 0 || elapsedTime > this.maxTime) {
        this.maxTime = elapsedTime;
      }
      this.totalExecutions++;
      if (t != null) {
        this.executionErrors++;
        this.lastExceptionTimestamp = this.lastExecuted;
        this.lastException = t;
      }
      this.sum += elapsedTime;
      this.sumSQ += elapsedTime * elapsedTime;

    }

    public String toString() {
      String le = this.lastExecuted == 0 ? "never" : new Date(this.lastExecuted).toString();
      if (this.lastException == null) {
        return "" + this.totalExecutions + " exe" + ", " + this.executionErrors + " errors" + ", avg "
            + (this.sum / this.totalExecutions) + " ms, \u03c3 " + Math.round(this.getTimeStandardDeviation()) + " ["
            + this.minTime + "-" + this.maxTime + " ms], last executed: " + le + ", last exception: never -- "
            + this.serviceId;
      } else {
        return "" + this.totalExecutions + " exe" + ", " + this.executionErrors + " errors" + ", avg "
            + (this.sum / this.totalExecutions) + " ms, \u03c3 " + Math.round(this.getTimeStandardDeviation()) + " ["
            + this.minTime + "-" + this.maxTime + " ms], last executed: " + le + ", last exception at "
            + new Date(this.lastExceptionTimestamp) + ": " + XUtil.renderThrowable(this.lastException) + " -- "
            + this.serviceId;
      }
    }

    // Getters

    public String getServiceId() {
      return this.serviceId;
    }

    public long getMinTime() {
      return minTime;
    }

    public long getMaxTime() {
      return maxTime;
    }

    public long getTotalExecutions() {
      return totalExecutions;
    }

    public long getExecutionErrors() {
      return executionErrors;
    }

    public long getLastExecuted() {
      return lastExecuted;
    }

    public long getLastExceptionTimestamp() {
      return lastExceptionTimestamp;
    }

    public Throwable getLastException() {
      return lastException;
    }

    // Extra getters

    public long getAverageTime() {
      return this.totalExecutions == 0 ? -1 : this.sum / this.totalExecutions;
    }

    /**
     * See Welford's online algorithm:
     * https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#Online_algorithm
     * 
     * @return the standard deviation
     */
    public double getTimeStandardDeviation() {
      return this.totalExecutions < 2 ? 0
          : Math.sqrt( //
              (this.sumSQ - 1.0 * this.sum * this.sum / this.totalExecutions) //
                  / //
                  (this.totalExecutions - 0));
    }

  }

}
