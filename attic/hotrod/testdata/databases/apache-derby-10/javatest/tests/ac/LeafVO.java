package tests.ac;

public class LeafVO {

  private Integer id;
  private Double weight;
  private String color;
  private Integer branchId;

  public String toString() {
    return "id=" + id + ", weight=" + weight + ", color=" + color + ", branchId=" + branchId;
  }

  public Integer getId() {
    return id;
  }

  public Double getWeight() {
    return weight;
  }

  public String getColor() {
    return color;
  }

  public Integer getBranchId() {
    return branchId;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public void setBranchId(Integer branchId) {
    this.branchId = branchId;
  }

}
