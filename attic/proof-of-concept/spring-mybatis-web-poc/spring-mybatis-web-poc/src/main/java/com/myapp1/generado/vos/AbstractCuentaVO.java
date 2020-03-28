package com.myapp1.generado.vos;

import java.io.Serializable;

public class AbstractCuentaVO implements Serializable {

  private static final long serialVersionUID = 1L;

  // VO Properties (table columns)

  protected java.lang.Integer id = null;
  protected java.lang.String numero = null;
  protected java.lang.String tipo = null;
  protected java.lang.Integer saldo = null;
  protected java.sql.Date creadaEn = null;

  // getters & setters

  public final java.lang.Integer getId() {
    return this.id;
  }

  public final void setId(final java.lang.Integer id) {
    this.id = id;
    this.propertiesChangeLog.idWasSet = true;
  }

  public final java.lang.String getNumero() {
    return this.numero;
  }

  public final void setNumero(final java.lang.String numero) {
    this.numero = numero;
    this.propertiesChangeLog.numeroWasSet = true;
  }

  public final java.lang.String getTipo() {
    return this.tipo;
  }

  public final void setTipo(final java.lang.String tipo) {
    this.tipo = tipo;
    this.propertiesChangeLog.tipoWasSet = true;
  }

  public final java.lang.Integer getSaldo() {
    return this.saldo;
  }

  public final void setSaldo(final java.lang.Integer saldo) {
    this.saldo = saldo;
    this.propertiesChangeLog.saldoWasSet = true;
  }

  public final java.sql.Date getCreadaEn() {
    return this.creadaEn;
  }

  public final void setCreadaEn(final java.sql.Date creadaEn) {
    this.creadaEn = creadaEn;
    this.propertiesChangeLog.creadaEnWasSet = true;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append(getClass().getName() + '@' + Integer.toHexString(hashCode()) + "\n");
    sb.append("- id=" + this.id + "\n");
    sb.append("- numero=" + this.numero + "\n");
    sb.append("- tipo=" + this.tipo + "\n");
    sb.append("- saldo=" + this.saldo + "\n");
    sb.append("- creadaEn=" + this.creadaEn);
    return sb.toString();
  }

  // Properties change log

  public PropertiesChangeLog propertiesChangeLog = new PropertiesChangeLog();

  public class PropertiesChangeLog {
    public boolean idWasSet = false;
    public boolean numeroWasSet = false;
    public boolean tipoWasSet = false;
    public boolean saldoWasSet = false;
    public boolean creadaEnWasSet = false;
  }

}
