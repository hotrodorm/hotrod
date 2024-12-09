package app.gen;

public class Account {

  public Account(Integer id, Integer balance) {
    this.id = id;
    this.balance = balance;
  }

  private Integer id;
  private Integer balance;

  public Integer getId() {
    return id;
  }

  public Integer getBalance() {
    return balance;
  }

}
