// Autogenerated by HotRod -- Do not edit.

package com.myapp.daos.primitives;

import java.io.Serializable;
import org.hotrod.runtime.json.*;

public class ClientVO implements Serializable {

  private static final long serialVersionUID = 1L;

  // VO Properties (table columns)

  protected java.lang.String name = null;
  protected java.lang.Integer vip = null;

  // getters & setters

  public java.lang.String getName() {
    return this.name;
  }

  public void setName(final java.lang.String name) {
    this.name = name;
  }

  public java.lang.Integer getVip() {
    return this.vip;
  }

  public void setVip(final java.lang.Integer vip) {
    this.vip = vip;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append( getClass().getName() + '@' + Integer.toHexString(hashCode()) + "\n");
    sb.append("- name=" + this.name + "\n");
    sb.append("- vip=" + this.vip);
    return sb.toString();
  }

  // to JSON

  public String toJSON() {
    JSONObject obj = new JSONObject();
    obj.addProperty("name", this.name);
    obj.addProperty("vip", this.vip);
    return obj.render();
  }

}