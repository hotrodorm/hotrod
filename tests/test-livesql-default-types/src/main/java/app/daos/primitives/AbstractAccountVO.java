// Autogenerated by HotRod -- Do not edit.

package app.daos.primitives;

import java.io.Serializable;
import org.hotrod.runtime.json.*;

public class AbstractAccountVO implements Serializable {

  private static final long serialVersionUID = 1L;

  // VO Properties (table columns)

  protected java.lang.Integer id = null;
  protected java.lang.Integer parentId = null;
  protected java.lang.Integer branchId = null;

  // getters & setters

  public java.lang.Integer getId() {
    return this.id;
  }

  public void setId(final java.lang.Integer id) {
    this.id = id;
  }

  public java.lang.Integer getParentId() {
    return this.parentId;
  }

  public void setParentId(final java.lang.Integer parentId) {
    this.parentId = parentId;
  }

  public java.lang.Integer getBranchId() {
    return this.branchId;
  }

  public void setBranchId(final java.lang.Integer branchId) {
    this.branchId = branchId;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append( getClass().getName() + '@' + Integer.toHexString(hashCode()) + "\n");
    sb.append("- id=" + this.id + "\n");
    sb.append("- parentId=" + this.parentId + "\n");
    sb.append("- branchId=" + this.branchId);
    return sb.toString();
  }

  // to JSON Object

  public JSONObject toJSONObject() {
    JSONObject obj = new JSONObject();
    obj.addProperty("id", this.id);
    obj.addProperty("parentId", this.parentId);
    obj.addProperty("branchId", this.branchId);
    return obj;
  }

  // to JSON String

  public String toJSON() {
    return toJSONObject().render();
  }

}
