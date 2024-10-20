// Autogenerated by HotRod -- Do not edit.

package test.persistence.primitives;

import java.io.Serializable;
import org.hotrod.runtime.json.*;

public class TestIdentity1 implements Serializable {

  private static final long serialVersionUID = 1L;

  // VO Properties (table columns)

  protected java.lang.Integer id = null;
  protected java.lang.String name = null;

  // getters & setters

  public java.lang.Integer getId() {
    return this.id;
  }

  public void setId(final java.lang.Integer id) {
    this.id = id;
    this.getPropertiesChangeLog().idWasSet = true;
  }

  public java.lang.String getName() {
    return this.name;
  }

  public void setName(final java.lang.String name) {
    this.name = name;
    this.getPropertiesChangeLog().nameWasSet = true;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append( getClass().getName() + '@' + Integer.toHexString(hashCode()) + "\n");
    sb.append("- id=" + this.id + "\n");
    sb.append("- name=" + this.name);
    return sb.toString();
  }

  // to JSON

  public String toJSON() {
    JSONObject obj = new JSONObject();
    obj.addProperty("id", this.id);
    obj.addProperty("name", this.name);
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
  }

}
