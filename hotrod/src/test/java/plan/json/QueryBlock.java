package plan.json;

public class QueryBlock {

  public String select_id;
  public String cost_info;

  public String toString() {
    return "select_i=" + this.select_id + ", cost_info=" + this.cost_info;
  }

  public String getSelect_id() {
    return select_id;
  }

  public void setSelect_id(String select_id) {
    this.select_id = select_id;
  }

  public String getCost_info() {
    return cost_info;
  }

  public void setCost_info(String cost_info) {
    this.cost_info = cost_info;
  }

}
