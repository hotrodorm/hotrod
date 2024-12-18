package gen;

public class Account {

  private Integer id;
  private String name;
  private String type;
  private Integer balance;

  public Account() {
  }

  public Account(Integer id, String name, String type, Integer balance) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.balance = balance;
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getBalance() {
    return balance;
  }

  public void setBalance(Integer balance) {
    this.balance = balance;
  }

  @Override
  public String toString() {
    return "Account [id=" + id + ", name=" + name + ", type=" + type + ", balance=" + balance + "]";
  }

}
