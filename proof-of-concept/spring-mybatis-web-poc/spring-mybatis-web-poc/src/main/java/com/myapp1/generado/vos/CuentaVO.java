package com.myapp1.generado.vos;

public class CuentaVO extends AbstractCuentaVO {

  private static final long serialVersionUID = 1L;

  public String getTitulo() {
    return "(" + this.tipo + ") " + this.numero + " - $" + this.saldo;
  }

}
