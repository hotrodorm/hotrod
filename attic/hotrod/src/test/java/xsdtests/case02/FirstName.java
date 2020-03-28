package xsdtests.case02;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "firstname")
public class FirstName {

  public FirstName() {
    System.out.println("*** [Constructor] FirstName");
  }

  private String body = null;

  // Getters & Setters

  public String getBody() {
    return body;
  }

  @XmlValue
  public void setBody(String body) {
    System.out.println("*** setBody(" + body + ")");
    this.body = body;
  }

  // toString

  public String toString() {
    return this.body == null ? "<null>" : this.body;
  }

}
