package app;

import java.util.Map;

public class AppConfiguration {

  private Map<String, Plan> plans;

  public final Map<String, Plan> getPlans() {
    return plans;
  }

  public final void setPlans(Map<String, Plan> plans) {
    this.plans = plans;
  }

  public static class Plan {

    private Integer code;
    private String title;
    private boolean vip;
    private boolean ordered;

    private Plan(Integer code, String title, boolean vip, boolean ordered) {
      this.code = code;
      this.title = title;
      this.ordered = ordered;
      this.vip = vip;
    }

    public static Plan of(Integer code, String title, boolean vip, boolean ordered) {
      return new Plan(code, title, vip, ordered);
    }

    public final Integer getCode() {
      return code;
    }

    public final String getTitle() {
      return title;
    }

    public final boolean isVip() {
      return vip;
    }

    public final boolean isOrdered() {
      return ordered;
    }

  }

}
