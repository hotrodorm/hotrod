// Autogenerated by HotRod -- Do not edit.

package com.company.daos.primitives;

import java.io.Serializable;
import org.hotrod.runtime.json.*;

public class DomainLowAccountPrototype implements Serializable {

  private static final long serialVersionUID = 1L;

  // VO Properties (view columns)

  protected java.lang.Integer id = null;
  protected java.lang.String name = null;
  protected java.lang.String type = null;
  protected java.lang.Integer currentBalance = null;
  protected java.sql.Timestamp createdOn = null;
  protected java.lang.Integer active = null;

  // getters & setters

  public final java.lang.Integer getId() {
    return this.id;
  }

  public final void setId(final java.lang.Integer id) {
    this.id = id;
    this.getPropertiesChangeLog().idWasSet = true;
  }

  public final java.lang.String getName() {
    return this.name;
  }

  public final void setName(final java.lang.String name) {
    this.name = name;
    this.getPropertiesChangeLog().nameWasSet = true;
  }

  public final java.lang.String getType() {
    return this.type;
  }

  public final void setType(final java.lang.String type) {
    this.type = type;
    this.getPropertiesChangeLog().typeWasSet = true;
  }

  public final java.lang.Integer getCurrentBalance() {
    return this.currentBalance;
  }

  public final void setCurrentBalance(final java.lang.Integer currentBalance) {
    this.currentBalance = currentBalance;
    this.getPropertiesChangeLog().currentBalanceWasSet = true;
  }

  public final java.sql.Timestamp getCreatedOn() {
    return this.createdOn;
  }

  public final void setCreatedOn(final java.sql.Timestamp createdOn) {
    this.createdOn = createdOn;
    this.getPropertiesChangeLog().createdOnWasSet = true;
  }

  public final java.lang.Integer getActive() {
    return this.active;
  }

  public final void setActive(final java.lang.Integer active) {
    this.active = active;
    this.getPropertiesChangeLog().activeWasSet = true;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append( getClass().getName() + '@' + Integer.toHexString(hashCode()) + "\n");
    sb.append("- id=" + this.id + "\n");
    sb.append("- name=" + this.name + "\n");
    sb.append("- type=" + this.type + "\n");
    sb.append("- currentBalance=" + this.currentBalance + "\n");
    sb.append("- createdOn=" + this.createdOn + "\n");
    sb.append("- active=" + this.active);
    return sb.toString();
  }

  // to JSON

  public String toJSON() {
    JSONObject obj = new JSONObject();
    obj.addProperty("id", this.id);
    obj.addProperty("name", this.name);
    obj.addProperty("type", this.type);
    obj.addProperty("currentBalance", this.currentBalance);
    obj.addProperty("createdOn", this.createdOn);
    obj.addProperty("active", this.active);
    return obj.render();
  }

  // Properties change log

  private PropertiesChangeLog propertiesChangeLog = new PropertiesChangeLog();

  protected PropertiesChangeLog getPropertiesChangeLog() {
    return propertiesChangeLog;
  }

  protected class PropertiesChangeLog {
    public boolean idWasSet = false;
    public boolean nameWasSet = false;
    public boolean typeWasSet = false;
    public boolean currentBalanceWasSet = false;
    public boolean createdOnWasSet = false;
    public boolean activeWasSet = false;
  }

}