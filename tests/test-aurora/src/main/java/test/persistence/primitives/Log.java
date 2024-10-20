// Autogenerated by HotRod -- Do not edit.

package test.persistence.primitives;

import java.io.Serializable;
import org.hotrod.runtime.json.*;

public class Log implements Serializable {

  private static final long serialVersionUID = 1L;

  // VO Properties (table columns)

  protected java.sql.Timestamp recordedAt = null;
  protected java.lang.String notes = null;

  // getters & setters

  public java.sql.Timestamp getRecordedAt() {
    return this.recordedAt;
  }

  public void setRecordedAt(final java.sql.Timestamp recordedAt) {
    this.recordedAt = recordedAt;
    this.getPropertiesChangeLog().recordedAtWasSet = true;
  }

  public java.lang.String getNotes() {
    return this.notes;
  }

  public void setNotes(final java.lang.String notes) {
    this.notes = notes;
    this.getPropertiesChangeLog().notesWasSet = true;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append( getClass().getName() + '@' + Integer.toHexString(hashCode()) + "\n");
    sb.append("- recordedAt=" + this.recordedAt + "\n");
    sb.append("- notes=" + this.notes);
    return sb.toString();
  }

  // to JSON

  public String toJSON() {
    JSONObject obj = new JSONObject();
    obj.addProperty("recordedAt", this.recordedAt);
    obj.addProperty("notes", this.notes);
    return obj.render();
  }

  // Properties change log

  private PropertiesChangeLog propertiesChangeLog = new PropertiesChangeLog();

  protected PropertiesChangeLog getPropertiesChangeLog() {
    return propertiesChangeLog;
  }

  protected class PropertiesChangeLog {
    public boolean recordedAtWasSet = false;
    public boolean notesWasSet = false;
  }

}
