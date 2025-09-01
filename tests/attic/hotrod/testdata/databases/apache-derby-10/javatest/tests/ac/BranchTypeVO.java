package tests.ac;

public class BranchTypeVO {

  private Integer id;
  private String name;

  public String toString() {
    return "id=" + id + ", name=" + name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
