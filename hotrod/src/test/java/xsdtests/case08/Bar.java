package xsdtests.case08;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class Bar {

  public Bar() {
    System.out.println("*** [Constructor] Bar");
  }

  private String body = null;

  // Getters & Setters

  public String getBody() {
    return body;
  }

  @XmlValue
  public void setBody(String body) {
    System.out.println("***   Bar.setBody(" + body + ")");
    this.body = body;
  }

  // toString

  public String toString() {
    return "{Bar:" + (this.body == null ? "<null>" : this.body) + "}";
  }

}