package xsdtests.case05;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "shoesize")
public class ShoeSize {

  private String country = null;
  private String body = null;

  // Getters & Setters

  public String getCountry() {
    return country;
  }

  @XmlAttribute
  public void setCountry(String country) {
    System.out.println("***   " + ShoeSize.class.getSimpleName() + ".setCountry(" + country + ")");
    this.country = country;
  }

  public String getBody() {
    return body;
  }

  @XmlValue
  public void setBody(String body) {
    System.out.println("***   " + ShoeSize.class.getSimpleName() + ".setBody(" + body + ")");
    this.body = body;
  }

  // toString

  public String toString() {
    return "[country=" + this.country + "] body=" + this.body;
  }

}
