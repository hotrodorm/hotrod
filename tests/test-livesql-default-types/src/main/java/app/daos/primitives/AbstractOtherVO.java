// Autogenerated by HotRod -- Do not edit.

package app.daos.primitives;

import java.io.Serializable;
import org.hotrod.runtime.json.*;

public class AbstractOtherVO implements Serializable {

  private static final long serialVersionUID = 1L;

  // VO Properties (table columns)

  protected java.lang.Integer id = null;
  protected java.lang.Boolean boo1 = null;
  protected java.lang.Boolean boo2 = null;
  protected java.lang.Boolean boo3 = null;
  protected java.lang.Object oth1 = null;
  protected byte[] idn1 = null;
  protected byte[] geo1 = null;

  // getters & setters

  public java.lang.Integer getId() {
    return this.id;
  }

  public void setId(final java.lang.Integer id) {
    this.id = id;
  }

  public java.lang.Boolean getBoo1() {
    return this.boo1;
  }

  public void setBoo1(final java.lang.Boolean boo1) {
    this.boo1 = boo1;
  }

  public java.lang.Boolean getBoo2() {
    return this.boo2;
  }

  public void setBoo2(final java.lang.Boolean boo2) {
    this.boo2 = boo2;
  }

  public java.lang.Boolean getBoo3() {
    return this.boo3;
  }

  public void setBoo3(final java.lang.Boolean boo3) {
    this.boo3 = boo3;
  }

  public java.lang.Object getOth1() {
    return this.oth1;
  }

  public void setOth1(final java.lang.Object oth1) {
    this.oth1 = oth1;
  }

  public byte[] getIdn1() {
    return this.idn1;
  }

  public void setIdn1(final byte[] idn1) {
    this.idn1 = idn1;
  }

  public byte[] getGeo1() {
    return this.geo1;
  }

  public void setGeo1(final byte[] geo1) {
    this.geo1 = geo1;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append( getClass().getName() + '@' + Integer.toHexString(hashCode()) + "\n");
    sb.append("- id=" + this.id + "\n");
    sb.append("- boo1=" + this.boo1 + "\n");
    sb.append("- boo2=" + this.boo2 + "\n");
    sb.append("- boo3=" + this.boo3 + "\n");
    sb.append("- oth1=" + this.oth1 + "\n");
    sb.append("- idn1=" + this.idn1 + "\n");
    sb.append("- geo1=" + this.geo1);
    return sb.toString();
  }

  // to JSON

  public String toJSON() {
    JSONObject obj = new JSONObject();
    obj.addProperty("id", this.id);
    obj.addProperty("boo1", this.boo1);
    obj.addProperty("boo2", this.boo2);
    obj.addProperty("boo3", this.boo3);
    obj.addProperty("oth1", this.oth1);
    obj.addProperty("idn1", this.idn1);
    obj.addProperty("geo1", this.geo1);
    return obj.render();
  }

}