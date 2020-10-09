package app5.factory;

public abstract class Animal {

  public abstract void talk();

  public void salute() {
    System.out.println("Hi!");
    this.talk();
  }

}
