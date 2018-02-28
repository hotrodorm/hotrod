package tests.ac;

public class FlowerVO {

  private Integer id;
  private Double size;
  private Integer branchId;

  public String toString() {
    return "id=" + id + ", size=" + size + ", branchId=" + branchId;
  }

  public Integer getId() {
    return id;
  }

  public Double getSize() {
    return size;
  }

  public Integer getBranchId() {
    return branchId;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setSize(Double size) {
    this.size = size;
  }

  public void setBranchId(Integer branchId) {
    this.branchId = branchId;
  }

}
