// Autogenerated by HotRod -- Do not edit.

package com.company.daos.primitives;

import java.io.Serializable;
import org.hotrod.runtime.json.*;

public class Domain_price_Prototype implements Serializable {

  private static final long serialVersionUID = 1L;

  // VO Properties (table columns)

  protected java.lang.Integer id = null;
  protected java.lang.Integer value = null;

  // getters & setters

  public final java.lang.Integer getId() {
    return this.id;
  }

  public final void setId(final java.lang.Integer id) {
    this.id = id;
    this.getPropertiesChangeLog().idWasSet = true;
  }

  public final java.lang.Integer getValue() {
    return this.value;
  }

  public final void setValue(final java.lang.Integer value) {
    this.value = value;
    this.getPropertiesChangeLog().valueWasSet = true;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append( getClass().getName() + '@' + Integer.toHexString(hashCode()) + "\n");
    sb.append("- id=" + this.id + "\n");
    sb.append("- value=" + this.value);
    return sb.toString();
  }

  // to JSON

  public String toJSON() {
    JSONObject obj = new JSONObject();
    obj.addProperty("id", this.id);
    obj.addProperty("value", this.value);
    return obj.render();
  }

  // Properties change log

  private PropertiesChangeLog propertiesChangeLog = new PropertiesChangeLog();

  protected PropertiesChangeLog getPropertiesChangeLog() {
    return propertiesChangeLog;
  }

  protected class PropertiesChangeLog {
    public boolean idWasSet = false;
    public boolean valueWasSet = false;
  }

}