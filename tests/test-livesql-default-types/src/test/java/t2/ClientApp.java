package t2;

public class ClientApp {

  public static void main(String[] args) {
    Entity a = newEnt();
    if (a == null) {
      
    }
  }

  private static Entity<Integer> newEnt() {
    return new Entity<Integer>();
  }

}
