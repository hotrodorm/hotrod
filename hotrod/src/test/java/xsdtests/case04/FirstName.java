package xsdtests.case04;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class FirstName {

  public FirstName() {
    System.out.println("*** [Constructor] " + FirstName.class.getSimpleName());
  }

  private String body = null;

  // Getters & Setters

  public String getBody() {
    return body;
  }

  @XmlValue
  public void setBody(String body) {
    System.out.println("***   " + FirstName.class.getSimpleName() + ".setBody(" + body + ")");
    this.body = body;
  }

  // toString

  public String toString() {
    return "{" + FirstName.class.getSimpleName() + ":" + (this.body == null ? "<null>" : this.body) + "}";
  }

}