package tests.ac;

public class BranchVO {

  private Integer id;
  private String name;
  private Integer treeId;

  public String toString() {
    return "id=" + id + ", name=" + name + ", treeId=" + treeId;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getTreeId() {
    return treeId;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setTreeId(Integer treeId) {
    this.treeId = treeId;
  }

}
