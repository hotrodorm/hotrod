// Autogenerated by HotRod -- Do not edit.

package test.persistence.primitives;

import java.io.Serializable;
import org.hotrod.runtime.json.*;

public class Properties implements Serializable {

  private static final long serialVersionUID = 1L;

  // VO Properties (table columns)

  protected java.lang.String application = null;
  protected java.lang.String name = null;
  protected java.lang.String propValue = null;

  // getters & setters

  public java.lang.String getApplication() {
    return this.application;
  }

  public void setApplication(final java.lang.String application) {
    this.application = application;
    this.getPropertiesChangeLog().applicationWasSet = true;
  }

  public java.lang.String getName() {
    return this.name;
  }

  public void setName(final java.lang.String name) {
    this.name = name;
    this.getPropertiesChangeLog().nameWasSet = true;
  }

  public java.lang.String getPropValue() {
    return this.propValue;
  }

  public void setPropValue(final java.lang.String propValue) {
    this.propValue = propValue;
    this.getPropertiesChangeLog().propValueWasSet = true;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append( getClass().getName() + '@' + Integer.toHexString(hashCode()) + "\n");
    sb.append("- application=" + this.application + "\n");
    sb.append("- name=" + this.name + "\n");
    sb.append("- propValue=" + this.propValue);
    return sb.toString();
  }

  // to JSON

  public String toJSON() {
    JSONObject obj = new JSONObject();
    obj.addProperty("application", this.application);
    obj.addProperty("name", this.name);
    obj.addProperty("propValue", this.propValue);
    return obj.render();
  }

  // Properties change log

  private PropertiesChangeLog propertiesChangeLog = new PropertiesChangeLog();

  protected PropertiesChangeLog getPropertiesChangeLog() {
    return propertiesChangeLog;
  }

  protected class PropertiesChangeLog {
    public boolean applicationWasSet = false;
    public boolean nameWasSet = false;
    public boolean propValueWasSet = false;
  }

}