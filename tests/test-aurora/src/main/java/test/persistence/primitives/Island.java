// Autogenerated by HotRod -- Do not edit.

package test.persistence.primitives;

import java.io.Serializable;
import org.hotrod.runtime.json.*;

public class Island implements Serializable {

  private static final long serialVersionUID = 1L;

  // VO Properties (table columns)

  protected java.lang.Integer id = null;
  protected java.lang.Integer segment = null;
  protected java.lang.Integer xStart = null;
  protected java.lang.Integer xEnd = null;
  protected java.lang.Integer height = null;

  // getters & setters

  public java.lang.Integer getId() {
    return this.id;
  }

  public void setId(final java.lang.Integer id) {
    this.id = id;
    this.getPropertiesChangeLog().idWasSet = true;
  }

  public java.lang.Integer getSegment() {
    return this.segment;
  }

  public void setSegment(final java.lang.Integer segment) {
    this.segment = segment;
    this.getPropertiesChangeLog().segmentWasSet = true;
  }

  public java.lang.Integer getXStart() {
    return this.xStart;
  }

  public void setXStart(final java.lang.Integer xStart) {
    this.xStart = xStart;
    this.getPropertiesChangeLog().xStartWasSet = true;
  }

  public java.lang.Integer getXEnd() {
    return this.xEnd;
  }

  public void setXEnd(final java.lang.Integer xEnd) {
    this.xEnd = xEnd;
    this.getPropertiesChangeLog().xEndWasSet = true;
  }

  public java.lang.Integer getHeight() {
    return this.height;
  }

  public void setHeight(final java.lang.Integer height) {
    this.height = height;
    this.getPropertiesChangeLog().heightWasSet = true;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append( getClass().getName() + '@' + Integer.toHexString(hashCode()) + "\n");
    sb.append("- id=" + this.id + "\n");
    sb.append("- segment=" + this.segment + "\n");
    sb.append("- xStart=" + this.xStart + "\n");
    sb.append("- xEnd=" + this.xEnd + "\n");
    sb.append("- height=" + this.height);
    return sb.toString();
  }

  // to JSON

  public String toJSON() {
    JSONObject obj = new JSONObject();
    obj.addProperty("id", this.id);
    obj.addProperty("segment", this.segment);
    obj.addProperty("xStart", this.xStart);
    obj.addProperty("xEnd", this.xEnd);
    obj.addProperty("height", this.height);
    return obj.render();
  }

  // Properties change log

  private PropertiesChangeLog propertiesChangeLog = new PropertiesChangeLog();

  protected PropertiesChangeLog getPropertiesChangeLog() {
    return propertiesChangeLog;
  }

  protected class PropertiesChangeLog {
    public boolean idWasSet = false;
    public boolean segmentWasSet = false;
    public boolean xStartWasSet = false;
    public boolean xEndWasSet = false;
    public boolean heightWasSet = false;
  }

}
