// Autogenerated by HotRod -- Do not edit.

package test.persistence.primitives;

import java.io.Serializable;
import org.hotrod.runtime.json.*;

public class TestDefault2 implements Serializable {

  private static final long serialVersionUID = 1L;

  // VO Properties (table columns)

  protected java.lang.String name = null;
  protected java.lang.Integer price = null;
  protected java.lang.Integer branchId = null;

  // getters & setters

  public java.lang.String getName() {
    return this.name;
  }

  public void setName(final java.lang.String name) {
    this.name = name;
    this.getPropertiesChangeLog().nameWasSet = true;
  }

  public java.lang.Integer getPrice() {
    return this.price;
  }

  public void setPrice(final java.lang.Integer price) {
    this.price = price;
    this.getPropertiesChangeLog().priceWasSet = true;
  }

  public java.lang.Integer getBranchId() {
    return this.branchId;
  }

  public void setBranchId(final java.lang.Integer branchId) {
    this.branchId = branchId;
    this.getPropertiesChangeLog().branchIdWasSet = true;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append( getClass().getName() + '@' + Integer.toHexString(hashCode()) + "\n");
    sb.append("- name=" + this.name + "\n");
    sb.append("- price=" + this.price + "\n");
    sb.append("- branchId=" + this.branchId);
    return sb.toString();
  }

  // to JSON

  public String toJSON() {
    JSONObject obj = new JSONObject();
    obj.addProperty("name", this.name);
    obj.addProperty("price", this.price);
    obj.addProperty("branchId", this.branchId);
    return obj.render();
  }

  // Properties change log

  private PropertiesChangeLog propertiesChangeLog = new PropertiesChangeLog();

  protected PropertiesChangeLog getPropertiesChangeLog() {
    return propertiesChangeLog;
  }

  protected class PropertiesChangeLog {
    public boolean nameWasSet = false;
    public boolean priceWasSet = false;
    public boolean branchIdWasSet = false;
  }

}