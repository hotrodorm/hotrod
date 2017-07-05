package xsdtests.case00.combinations;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class Yaneyt {

  private String country = null;
  private String body = null;

  // Getters & Setters

  public String getCountry() {
    return country;
  }

  @XmlAttribute
  public void setCountry(String country) {
    this.country = country;
  }

  public String getBody() {
    return body;
  }

  @XmlValue
  public void setBody(String body) {
    this.body = body;
  }

}
