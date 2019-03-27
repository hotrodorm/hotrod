package shell;

import model.ManagerCuentas;

public class App1 {

  public static void main(final String[] args) {
    App1 app1 = new App1();
    app1.transferir();
  }

  private void transferir() {

    ManagerCuentas managerCuentas = SpringBeanRetriever.getBean("managerCuentas");

    try {

      String desdeCta = "C02";
      String haciaCta = "A08";
      Integer monto = 5000;

      System.out.println(" ");
      System.out.println("Transfiriendo $" + monto + " - desde " + desdeCta + " - hacia " + haciaCta + " ...");

      managerCuentas.transferir(desdeCta, haciaCta, monto);

      System.out.println(" ");
      System.out.println("Transferencia exitosa.");

    } catch (RuntimeException e) {
      System.out.println(" ");
      System.out.println("Transferencia fallida (motivo: " + e.getClass().getSimpleName() + ").");
    }

    System.out.println(" ");
    managerCuentas.listarCuentas();

  }

}
