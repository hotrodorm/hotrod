// Autogenerated by HotRod -- Do not edit.

package test.persistence.primitives;

import java.io.Serializable;
import org.hotrod.runtime.json.*;

public class CursorExample1 implements Serializable {

  private static final long serialVersionUID = 1L;

  // VO Properties (table columns)

  protected java.lang.Long id = null;
  protected java.lang.String data = null;

  // getters & setters

  public java.lang.Long getId() {
    return this.id;
  }

  public void setId(final java.lang.Long id) {
    this.id = id;
    this.getPropertiesChangeLog().idWasSet = true;
  }

  public java.lang.String getData() {
    return this.data;
  }

  public void setData(final java.lang.String data) {
    this.data = data;
    this.getPropertiesChangeLog().dataWasSet = true;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append( getClass().getName() + '@' + Integer.toHexString(hashCode()) + "\n");
    sb.append("- id=" + this.id + "\n");
    sb.append("- data=" + this.data);
    return sb.toString();
  }

  // to JSON

  public String toJSON() {
    JSONObject obj = new JSONObject();
    obj.addProperty("id", this.id);
    obj.addProperty("data", this.data);
    return obj.render();
  }

  // Properties change log

  private PropertiesChangeLog propertiesChangeLog = new PropertiesChangeLog();

  protected PropertiesChangeLog getPropertiesChangeLog() {
    return propertiesChangeLog;
  }

  protected class PropertiesChangeLog {
    public boolean idWasSet = false;
    public boolean dataWasSet = false;
  }

}
