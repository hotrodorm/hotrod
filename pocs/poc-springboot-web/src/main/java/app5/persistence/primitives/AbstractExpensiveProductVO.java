// Autogenerated by HotRod -- Do not edit.

package app5.persistence.primitives;

import java.io.Serializable;

public class AbstractExpensiveProductVO implements Serializable {

  private static final long serialVersionUID = 1L;

  // Expression properties

  protected java.lang.String name = null;
  protected java.lang.Integer price = null;
  protected java.lang.String kind = null;

  // getters & setters

  public final java.lang.String getName() {
    return this.name;
  }

  public final void setName(final java.lang.String name) {
    this.name = name;
    this.propertiesChangeLog.nameWasSet = true;
  }

  public final java.lang.Integer getPrice() {
    return this.price;
  }

  public final void setPrice(final java.lang.Integer price) {
    this.price = price;
    this.propertiesChangeLog.priceWasSet = true;
  }

  public final java.lang.String getKind() {
    return this.kind;
  }

  public final void setKind(final java.lang.String kind) {
    this.kind = kind;
    this.propertiesChangeLog.kindWasSet = true;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append(super.toString() + "\n");
    sb.append("- name=" + this.name + "\n");
    sb.append("- price=" + this.price + "\n");
    sb.append("- kind=" + this.kind);
    return sb.toString();
  }

  // Properties change log

  public PropertiesChangeLog propertiesChangeLog = new PropertiesChangeLog();

  public class PropertiesChangeLog {
    public boolean nameWasSet = false;
    public boolean priceWasSet = false;
    public boolean kindWasSet = false;
  }

}
