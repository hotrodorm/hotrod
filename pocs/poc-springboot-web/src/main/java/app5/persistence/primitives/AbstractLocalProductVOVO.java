// Autogenerated by HotRod -- Do not edit.

package app5.persistence.primitives;

import java.io.Serializable;

public abstract class AbstractLocalProductVOVO implements Serializable {

  private static final long serialVersionUID = 1L;

  // Expression properties

  protected java.lang.Long id = null;
  protected java.lang.String name = null;
  protected java.lang.Integer price = null;
  protected java.lang.Long sku = null;

  // getters & setters

  public final java.lang.Long getId() {
    return this.id;
  }

  public final void setId(final java.lang.Long id) {
    this.id = id;
    this.propertiesChangeLog.idWasSet = true;
  }

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

  public final java.lang.Long getSku() {
    return this.sku;
  }

  public final void setSku(final java.lang.Long sku) {
    this.sku = sku;
    this.propertiesChangeLog.skuWasSet = true;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append(super.toString() + "\n");
    sb.append("- id=" + this.id + "\n");
    sb.append("- name=" + this.name + "\n");
    sb.append("- price=" + this.price + "\n");
    sb.append("- sku=" + this.sku);
    return sb.toString();
  }

  // Properties change log

  public PropertiesChangeLog propertiesChangeLog = new PropertiesChangeLog();

  public class PropertiesChangeLog {
    public boolean idWasSet = false;
    public boolean nameWasSet = false;
    public boolean priceWasSet = false;
    public boolean skuWasSet = false;
  }

}