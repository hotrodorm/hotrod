// Autogenerated by HotRod -- Do not edit.

package app.daos.primitives;

import java.io.Serializable;
import org.hotrod.runtime.json.*;

public class AbstractBatchVO implements Serializable {

  private static final long serialVersionUID = 1L;

  // VO Properties (table columns)

  protected java.lang.String skuCode = null;
  protected java.lang.String itemName = null;

  // getters & setters

  public java.lang.String getSkuCode() {
    return this.skuCode;
  }

  public void setSkuCode(final java.lang.String skuCode) {
    this.skuCode = skuCode;
  }

  public java.lang.String getItemName() {
    return this.itemName;
  }

  public void setItemName(final java.lang.String itemName) {
    this.itemName = itemName;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append( getClass().getName() + '@' + Integer.toHexString(hashCode()) + "\n");
    sb.append("- skuCode=" + this.skuCode + "\n");
    sb.append("- itemName=" + this.itemName);
    return sb.toString();
  }

  // to JSON

  public String toJSON() {
    JSONObject obj = new JSONObject();
    obj.addProperty("skuCode", this.skuCode);
    obj.addProperty("itemName", this.itemName);
    return obj.render();
  }

}