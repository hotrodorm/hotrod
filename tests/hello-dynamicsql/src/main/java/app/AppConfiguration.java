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
    private boolean vip;
    private String title;
    private boolean ordered;

    private Plan(Integer code, boolean vip, String title, boolean ordered) {
      this.code = code;
      this.vip = vip;
      this.title = title;
      this.ordered = ordered;
    }

    public static Plan of(Integer code, boolean vip, String title, boolean ordered) {
      return new Plan(code, vip, title, ordered);
    }

    public final Integer getCode() {
      return code;
    }

    public final boolean isVip() {
      return vip;
    }

    public final String getTitle() {
      return title;
    }

    public final boolean isOrdered() {
      return ordered;
    }

  }

}
