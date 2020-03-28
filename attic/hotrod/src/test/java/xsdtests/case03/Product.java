package xsdtests.case03;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Product {

  private String prodid = null;
  private String oldid = null;

  // Getters & Setters

  public String getProdid() {
    return prodid;
  }

  @XmlAttribute
  public void setProdid(String prodid) {
    System.out.println("*** setProdid(" + prodid + ")");
    this.prodid = prodid;
  }

  public String getOldid() {
    return oldid;
  }

  @XmlAttribute
  public void setOldid(String oldid) {
    System.out.println("*** setOldid(" + oldid + ")");
    this.oldid = oldid;
  }

  // toString

  public String toString() {
    return "prodid=" + this.prodid + " oldid=" + this.oldid;
  }

}
