package xsdtests.case09;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class Wxyz {

  public Wxyz() {
    System.out.println("*** [Constructor] " + Wxyz.class.getSimpleName());
  }

  private String body = null;

  // Getters & Setters

  public String getBody() {
    return body;
  }

  @XmlValue
  public void setBody(String body) {
    System.out.println("***   " + Wxyz.class.getSimpleName() + ".setBody(" + body + ")");
    this.body = body;
  }

  // toString

  public String toString() {
    return "{" + Wxyz.class.getSimpleName() + ":" + (this.body == null ? "<null>" : this.body) + "}";
  }

}